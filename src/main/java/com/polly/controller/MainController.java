package com.polly.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.polly.configuration.AmazonPollyTemplate;
import com.polly.configuration.AmazonProperties;
import com.polly.configuration.AmazonS3Template;
import com.polly.configuration.CommonProperties;
import com.polly.model.AudioMetadata;

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
   
    private InputStream synthesize(String plaintext, String voice, OutputFormat format) {
        SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest().withText(plaintext).withVoiceId(voice).withOutputFormat(format);
        SynthesizeSpeechResult synthRes = amazonPollyTemplate.pollyClient().synthesizeSpeech(synthReq);
        return synthRes.getAudioStream();        
    }   

}
