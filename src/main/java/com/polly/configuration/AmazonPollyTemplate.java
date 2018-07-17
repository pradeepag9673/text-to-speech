package com.polly.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;

/**
 * @author pradeep_ga
 *
 */
@Component
public class AmazonPollyTemplate {

    @Autowired
    AmazonProperties amazonProperties;
    
    /**
     * @return - Amazon polly client
     */
    public AmazonPolly pollyClient() {
        AWSCredentials credentials = new BasicAWSCredentials(amazonProperties.getAws().getAccessKeyId(), amazonProperties.getAws().getAccessKeySecret());        
        return AmazonPollyClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTH_1).build();        
    }
}
