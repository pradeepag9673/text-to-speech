package com.polly.model;

/**
 * @author pradeep_ga
 *
 */
public class Pipeline {
    
    private String id;
    
    private String name;
    
    /**
     * @return - Transcoder Pipeline's Id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id - Sets Transcoder Pipeline's Id
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return - Transcoder Pipeline's Name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name - Sets Transcoder Pipeline's Name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
}
