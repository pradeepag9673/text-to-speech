package com.polly.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

/**
 * @author pradeep_ga
 *
 */
@Component
public class AmazonSimpleNotificationServiceTemplate {

    @Autowired
    AmazonProperties amazonProperties;
    
    /**
     * @return - Amazon Simple Notification Service Client
     */
    public AmazonSNS snsClient() {
        AWSCredentials credentials = new BasicAWSCredentials(amazonProperties.getAws().getAccessKeyId(), amazonProperties.getAws().getAccessKeySecret());
        return AmazonSNSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTH_1).build(); 
    }      
}
