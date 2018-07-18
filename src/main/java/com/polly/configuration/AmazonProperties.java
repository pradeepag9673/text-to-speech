package com.polly.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration property group for Amazon S3 and AWS
 *
 * @author pradeep_ga
 */
@Configuration
@ConfigurationProperties(prefix = "amazon")
public class AmazonProperties {

    @NestedConfigurationProperty
    private Aws aws;

    @NestedConfigurationProperty
    private S3 s3;
    
    @NestedConfigurationProperty
    private Transcoder transcoder;

    /**
     * A property group for Amazon Web Service (AWS) configurations
     *
     * @return a property group for AWS configurations
     */
    public Aws getAws() {
        return aws;
    }

    /**
     * A property group for Amazon Web Service (AWS) configurations
     *
     * @param aws is a property group for AWS configurations
     */
    public void setAws(Aws aws) {
        this.aws = aws;
    }

    /**
     * A property group for Amazon S3 configurations
     *
     * @return a property group for Amazon S3 configurations
     */
    public S3 getS3() {
        return s3;
    }

    /**
     * A property group for Amazon S3 configurations
     *
     * @param s3 is a property group for Amazon S3 configurations
     */
    public void setS3(S3 s3) {
        this.s3 = s3;
    }
    
    /**
     * A property group for Amazon Transcoder configurations
     * 
     * @return a property group for Amazon Transcoder configurations
     */
    public Transcoder getTranscoder() {
        return transcoder;
    }

    /**
     * A property group for Amazon Transcoder configuration
     * 
     * @param transcoder is a property group for Amazon Transcoder configurations
     */
    public void setTranscoder(Transcoder transcoder) {
        this.transcoder = transcoder;
    }



    /**
     * A property group for Amazon Web Service (AWS) configurations
     */
    public static class Aws {

        private String accessKeyId;
        private String accessKeySecret;

        /**
         * A valid AWS account's access key id.
         *
         * @return an AWS access key id
         */
        public String getAccessKeyId() {
            return accessKeyId;
        }

        /**
         * A valid AWS account's access key id.
         *
         * @param accessKeyId is a valid AWS account's access key id.
         */
        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        /**
         * A valid AWS account's secret access token.
         *
         * @return an AWS account's secret access key
         */
        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        /**
         * A valid AWS account's secret access token.
         *
         * @param accessKeySecret is a valid AWS account's secret access token.
         */
        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

        @Override
        public String toString() {
            return "Aws{" +
                    "accessKeyId='" + accessKeyId + '\'' +
                    ", accessKeySecret='" + accessKeySecret + '\'' +
                    '}';
        }
    }

    /**
     * A property group for Amazon Web Service (AWS) configurations
     */
    public static class S3 {

        private String defaultBucket;
    
        private String wavFolder;
        
        private String m4aFolder;

        /**
         * The Amazon S3 bucket name for this application.
         *
         * @return a default Amazon S3 bucket name for this application.
         */
        public String getDefaultBucket() {
            return defaultBucket;
        }

        /**
         * The Amazon S3 bucket name for this application.
         *
         * @param defaultBucket is the default Amazon S3 bucket name for this application.
         */
        public void setDefaultBucket(String defaultBucket) {
            this.defaultBucket = defaultBucket;
        }        

        /**
         * The Amazon S3 bucket's folder name for this application.
         * 
         * @return - The Amazon S3 buckets wav folder name
         */
        public String getWavFolder() {
            return wavFolder;
        }

        /**
         * The Amazon S3 bucket's folder name for this application.
         * 
         * @param wavFolder - The Amazon S3 buckets wav foldername
         */
        public void setWavFolder(String wavFolder) {
            this.wavFolder = wavFolder;
        }       
        
        /**
         * The Amazon S3 bucket's folder name for this application.
         * 
         * @return - The Amazon S3 buckets m4a folder name
         */
        public String getM4aFolder() {
            return m4aFolder;
        }

        /**
         * The Amazon S3 bucket's folder name for this application.
         * 
         * @param m4aFolder - The Amazon S3 buckets m4a foldername
         */
        public void setM4aFolder(String m4aFolder) {
            this.m4aFolder = m4aFolder;
        }

        @Override
        public String toString() {
            return "S3{" +
                    "defaultBucket='" + defaultBucket + '\'' +
                    "wavFolder='" + wavFolder + '\'' +
                    "m4aFolder='" + m4aFolder + '\'' +
                    '}';
        }
    }
    
    /**
     * A property group for Amazon Web Service (AWS) configurations
     *
     */
    public static class Transcoder {
        
        private String pipelineName;

        /**
         * The Amazon Transcoder pipeline name for this application.
         * 
         * @return - piprline name
         */
        public String getPipelineName() {
            return pipelineName;
        }

        /**
         * The Amazon Transcoder pipeline name for this application.
         * 
         * @param pipelineName - sets pipeline name
         */
        public void setPipelineName(String pipelineName) {
            this.pipelineName = pipelineName;
        }
        
        @Override
        public String toString() {
            return "Transcoder{" +
                    "pipelinename='" + pipelineName + '\'' +
                    '}';
        }
    }
    

    @Override
    public String toString() {
        return "AmazonProperties{" +
                "aws=" + aws +
                ", amazon.s3=" + s3 +
                ", amazon.transcoder=" + transcoder +
                '}';
    }
}