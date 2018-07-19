package com.polly.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author pradeep_ga
 *
 */
public class S3AudioMetadata {

    private String key;
    private List<URL> url = new ArrayList<>();
    private String lastModified;
    
    /**
     * default constructor
     */
    public S3AudioMetadata() {}
      
    /**
     * @return - s3 object name
     */
    public String getKey() {
        return key;
    }
    /**
     * @param key - set s3 object name
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * @return - s3 object url
     */
    public List<URL> getUrl() {
        return url;
    }
    /**
     * @param url - set s3 object url
     */
    public void setUrl(List<URL> url) {
        this.url = url;
    }
    /**
     * @return - last modified date
     */
    public String getLastModified() {
        return lastModified;
    }
    /**
     * @param lastModified - set last modified date
     */
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }  
    
}
