package com.polly.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.elastictranscoder.model.CreateJobOutput;
import com.amazonaws.services.elastictranscoder.model.CreateJobRequest;
import com.amazonaws.services.elastictranscoder.model.JobInput;
import com.amazonaws.services.elastictranscoder.model.ListPipelinesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.util.IOUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.polly.configuration.AmazonElasticTranscoderTemplate;
import com.polly.configuration.AmazonPollyTemplate;
import com.polly.configuration.AmazonProperties;
import com.polly.configuration.AmazonS3Template;
import com.polly.configuration.AmazonSimpleNotificationServiceTemplate;
import com.polly.configuration.CommonProperties;
import com.polly.model.AudioMetadata;
import com.polly.model.Pipeline;
import com.polly.model.S3AudioMetadata;
import com.polly.model.TranscoderPipeline;

/**
 * @author pradeep_ga
 *
 */
@RestController
@RequestMapping("/voices")
public class MainController {

    @Autowired
    AmazonProperties amazonProperties;

    @Autowired
    CommonProperties commonProperties;

    @Autowired
    AmazonS3Template amazonS3Template;

    @Autowired
    AmazonPollyTemplate amazonPollyTemplate;
    
    @Autowired
    AmazonSimpleNotificationServiceTemplate amazonSnsTemplate;

    @Autowired
    AmazonElasticTranscoderTemplate amazonElasticTranscoderTemplate;
    
    private static final String invalid = "INVALID";

    /**
     * @param audioMetadata - contains audio metadata
     * @return - success message "Audio created successfully"
     * @throws IOException - exception
     */
    @RequestMapping(method=RequestMethod.POST)
    public String createVoices(@RequestBody AudioMetadata audioMetadata) throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        InputStream speechInputStream = synthesize(audioMetadata.getPlaintext(), audioMetadata.getVoice(), OutputFormat.Mp3);        
        OutputStream speechOutputStream = new FileOutputStream(commonProperties.getPath()+timestamp.getTime()+"."+OutputFormat.Mp3); 
        IOUtils.copy(speechInputStream, speechOutputStream);
        amazonS3Template.s3Client().putObject(new PutObjectRequest(amazonProperties.getS3().getDefaultBucket(), timestamp.getTime()+"."+OutputFormat.Mp3, new File(commonProperties.getPath()+timestamp.getTime()+"."+OutputFormat.Mp3)).withCannedAcl(CannedAccessControlList.PublicRead));
        Gson gson = new Gson();
        ListPipelinesResult listPipelinesResult = amazonElasticTranscoderTemplate.transcoderClient().listPipelines();
        TranscoderPipeline pipeline = gson.fromJson(gson.toJson(listPipelinesResult), new TypeToken<TranscoderPipeline>(){}.getType());
        createJobForFileTranscoder(getTranscoderPipelineId(pipeline), timestamp, ".wav", amazonProperties.getS3().getWavFolder().concat("/"), "1351620000001-300300");
        createJobForFileTranscoder(getTranscoderPipelineId(pipeline), timestamp, ".m4a", amazonProperties.getS3().getM4aFolder().concat("/"), "1351620000001-100110");
        String msg = "Media file named '"+timestamp.getTime()+"' have been transcoded successfully in to different formats('M4A','MP3','WAV').";
        PublishRequest publishRequest = new PublishRequest(amazonProperties.getSns().getTopic(), msg);
        amazonSnsTemplate.snsClient().publish(publishRequest);
        return "Audio files created and moved to s3 bucket";
    }

    private void createJobForFileTranscoder(String id, Timestamp timestamp, String mediaType, String outputPrefix, String presetId) {
        // TODO Auto-generated method stub
        CreateJobRequest jobRequest = new CreateJobRequest();
        jobRequest.setPipelineId(id);
        jobRequest.setOutputKeyPrefix(outputPrefix);
        JobInput input = new JobInput();
        input.setKey(timestamp.getTime()+"."+OutputFormat.Mp3);
        input.setAspectRatio("auto");
        input.setFrameRate("auto");
        input.setResolution("auto");
        input.setContainer("mp3");
        jobRequest.setInput(input);
        CreateJobOutput output = new CreateJobOutput();
        output.setKey(timestamp.getTime()+mediaType);        
        output.setPresetId(presetId);
        jobRequest.setOutput(output);        
        amazonElasticTranscoderTemplate.transcoderClient().createJob(jobRequest);
    }

    private String getTranscoderPipelineId(TranscoderPipeline pipelines) {
        Pipeline  p = pipelines.getPipelines().stream().filter(pipeline -> amazonProperties.getTranscoder().getPipelineName().equals(pipeline.getName())).findAny().orElse(null);
        return p.getId();
    }

    /**
     * @return - List of Audio Files and Metadata
     */
    @RequestMapping(value="audio", method=RequestMethod.GET)
    public List<S3AudioMetadata> getAudio() {
        ListObjectsV2Result result = amazonS3Template.s3Client().listObjectsV2(amazonProperties.getS3().getDefaultBucket());
        return populateS3AudioMetadata(result);
    }

    private List<S3AudioMetadata> populateS3AudioMetadata(ListObjectsV2Result result) {
        S3AudioMetadata s3AudioMetadata = null;
        Map<String, S3AudioMetadata> map = new HashMap<>();
        for(int i = 0;i<result.getObjectSummaries().size();i++) {
            String key = getKey(result.getObjectSummaries().get(i).getKey()); 
            if(!key.equals(invalid)){
                s3AudioMetadata = map.get(key) != null ? map.get(key) : populateS3Metadata(result.getObjectSummaries().get(i));
                List<URL> url = s3AudioMetadata.getUrl();
                url.add(amazonS3Template.s3Client().getUrl(amazonProperties.getS3().getDefaultBucket(), result.getObjectSummaries().get(i).getKey()));
                s3AudioMetadata.setUrl(url);
                map.put(key, s3AudioMetadata);
            }            
        }
        return new ArrayList<>(map.values()); 
    }

    private S3AudioMetadata populateS3Metadata(S3ObjectSummary s3ObjectSummary) {
        S3AudioMetadata s3AudioMetadata = new S3AudioMetadata();
        s3AudioMetadata.setKey(getKey(s3ObjectSummary.getKey()));        
        s3AudioMetadata.setLastModified(convertDatetimeToDate(s3ObjectSummary.getLastModified()));        
        return s3AudioMetadata;
    }

    private String convertDatetimeToDate(Date lastModified) {
        return new SimpleDateFormat ("yyyy/MM/dd").format(lastModified);
    }

    private String getKey(String key) {
        String fileName = "";
        if(key.contains(".mp3") || key.contains(".wav")|| key.contains(".m4a")) {
            if(key.contains(".mp3")) {
                fileName = key.substring(0, key.indexOf(".")); 
            }else {
                fileName = key.substring(key.indexOf("/")+1, key.indexOf("."));
            }           
            return fileName;
        } 
        return invalid;
    }

    /**
     * @param id - Audio key
     * @return - Delete audio file
     */
    @RequestMapping(value="audio/{id}", method=RequestMethod.DELETE)
    public String deleteAudio(@PathVariable String id) {
        amazonS3Template.s3Client().deleteObject(amazonProperties.getS3().getDefaultBucket(), id.concat(".mp3"));
        amazonS3Template.s3Client().deleteObject(amazonProperties.getS3().getDefaultBucket(), amazonProperties.getS3().getM4aFolder().concat("/").concat(id).concat(".m4a"));
        amazonS3Template.s3Client().deleteObject(amazonProperties.getS3().getDefaultBucket(), amazonProperties.getS3().getWavFolder().concat("/").concat(id).concat(".wav"));
        return "Audio deleted from s3 bucket";                
    }

    private InputStream synthesize(String plaintext, String voice, OutputFormat format) {
        return amazonPollyTemplate.pollyClient().synthesizeSpeech(new SynthesizeSpeechRequest().withText(plaintext).withVoiceId(voice).withOutputFormat(format)).getAudioStream();                
    } 

}
