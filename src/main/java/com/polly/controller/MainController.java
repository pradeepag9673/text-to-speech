package com.polly.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.polly.configuration.AmazonPollyTemplate;
import com.polly.configuration.AmazonProperties;
import com.polly.configuration.AmazonS3Template;
import com.polly.configuration.CommonProperties;
import com.polly.model.AudioMetadata;
import com.polly.model.S3AudioMetadata;

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
        return "Audio created successfully";
    }
    
    /**
     * @return - List of Audio Files and Metadata
     */
    @RequestMapping(value="audio", method=RequestMethod.GET)
    public List<S3AudioMetadata> getAudio() {
        List<S3AudioMetadata> listOfS3AudioMetadata = new ArrayList<>();
        ListObjectsV2Result result = amazonS3Template.s3Client().listObjectsV2(amazonProperties.getS3().getDefaultBucket());
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os: objects) {
            S3AudioMetadata s3AudioMetadata = new S3AudioMetadata();
            s3AudioMetadata.setKey(os.getKey());
            s3AudioMetadata.setUrl(amazonS3Template.s3Client().getUrl(amazonProperties.getS3().getDefaultBucket(), os.getKey()));
            s3AudioMetadata.setLastModified(os.getLastModified());
            listOfS3AudioMetadata.add(s3AudioMetadata);
        }        
        return listOfS3AudioMetadata;
    }
    
    /**
     * @param id - Audio key
     * @return - Delete audio file
     */
    @RequestMapping(value="audio/{id}", method=RequestMethod.DELETE)
    public String deleteAudio(@PathVariable String id) {
        amazonS3Template.s3Client().deleteObject(amazonProperties.getS3().getDefaultBucket(), id);
        return "Audio deleted successfully";                
    }
   
    private InputStream synthesize(String plaintext, String voice, OutputFormat format) {
        SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest().withText(plaintext).withVoiceId(voice).withOutputFormat(format);
        SynthesizeSpeechResult synthRes = amazonPollyTemplate.pollyClient().synthesizeSpeech(synthReq);
        return synthRes.getAudioStream();        
    }   

}
