package com.polly.model;

import java.net.URL;
import java.util.Date;

/**
 * @author pradeep_ga
 *
 */
public class S3AudioMetadata {

    private String key;
    private URL url;
    private Date lastModified;
    
    /**
     * default constructor
     */
    public S3AudioMetadata() {}
    
    /**
     * @param key - File Name
     * @param url - s3 file url
     * @param lastModified - file last modified
     */
    public S3AudioMetadata(String key, URL url, Date lastModified) {
        this.key = key;
        this.url = url;
        this.lastModified = lastModified;        
    }
    
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
    public URL getUrl() {
        return url;
    }
    /**
     * @param url - set s3 object url
     */
    public void setUrl(URL url) {
        this.url = url;
    }
    /**
     * @return - last modified date
     */
    public Date getLastModified() {
        return lastModified;
    }
    /**
     * @param lastModified - set last modified date
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }  
    
}
