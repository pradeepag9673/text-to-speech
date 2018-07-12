package com.polly.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * @author pradeep_ga
 *
 */
@Component
public class AmazonS3Template {

    @Autowired
    AmazonProperties amazonProperties;

    /**
     * @return - Amazon s3 client
     */
    public AmazonS3 s3Client() {        
        AWSCredentials credentials = new BasicAWSCredentials(amazonProperties.getAws().getAccessKeyId(), amazonProperties.getAws().getAccessKeySecret());
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTH_1).build();
    }    
}
