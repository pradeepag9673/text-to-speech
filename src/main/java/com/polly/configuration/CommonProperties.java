package com.polly.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration property group for common
 *
 * @author pradeep_ga
 */
@Configuration
@ConfigurationProperties(prefix = "common")
public class CommonProperties {

    private String path;
    
    /**
     * @return - outputstream filepath
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path - set outputstream filepath
     */
    public void setPath(String path) {
        this.path = path;
    }
 
}
