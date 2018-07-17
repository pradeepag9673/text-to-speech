package com.polly.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClientBuilder;

/**
 * @author pradeep_ga
 *
 */
@Component
public class AmazonElasticTranscoderTemplate {

    @Autowired
    AmazonProperties amazonProperties;
    
    /**
     * @return - Amazon Elastic Transcoder Client
     */
    public AmazonElasticTranscoder transcoderClient() {
        AWSCredentials credentials = new BasicAWSCredentials(amazonProperties.getAws().getAccessKeyId(), amazonProperties.getAws().getAccessKeySecret());
        return AmazonElasticTranscoderClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTH_1).build();
    }
}
