package com.polly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.Bucket;
import com.polly.configuration.AmazonProperties;
import com.polly.configuration.AmazonS3Template;
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
    AmazonS3Template amazonS3Template;
    
    /**
     * @param audioMetadata - contains audio metadata
     * @return - success message "Audio created successfully"
     */
    @RequestMapping(method=RequestMethod.POST)
    public String createVoices(@RequestBody AudioMetadata audioMetadata) {
        System.out.println(audioMetadata.getPlaintext() +", "+ audioMetadata.getVoice() +", "+ audioMetadata.getFormat());
        System.out.println(amazonProperties.getAws().getAccessKeyId()+", "+amazonProperties.getAws().getAccessKeySecret());
        List<Bucket> buckets = amazonS3Template.s3Client().listBuckets();
        System.out.println("Your Amazon S3 buckets are:");
        for (Bucket b : buckets) {
            System.out.println("* " + b.getName());
        }        
        return "Audio created successfully";
    }   

}
