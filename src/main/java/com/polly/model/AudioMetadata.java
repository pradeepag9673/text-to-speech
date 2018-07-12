package com.polly.model;

import com.polly.utils.CommonUtils;

/**
 * @author pradeep_ga
 * AudioMetadata contains metadata
 */
public class AudioMetadata {

    private String voice;
    private String plaintext;
    private String format = CommonUtils.AUDIOFORMAT_MP3; 
    
    /**
     * @return - voice
     */
    public String getVoice() {
        return voice;
    }
    /**
     * @param voice - voice id for generating specific voice related to gender.
     */
    public void setVoice(String voice) {
        this.voice = voice;
    }
    /**
     * @return - plaintext
     */
    public String getPlaintext() {
        return plaintext;
    }
    /**
     * @param plaintext - plaintext to be converted to audio file 
     */
    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }
    /**
     * @return - format
     */
    public String getFormat() {
        return format;
    }
    /**
     * @param format - format of an audio file
     */
    public void setFormat(String format) {
        this.format = format;
    }    
    
    
}
