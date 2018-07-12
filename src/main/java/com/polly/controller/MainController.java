package com.polly.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.polly.model.AudioMetadata;

/**
 * @author pradeep_ga
 *
 */
@RestController
@RequestMapping("/voices")
public class MainController {
    /**
     * @param audioMetadata - contains audio metadata
     * @return - success message "Audio created successfully"
     */
    @RequestMapping(method=RequestMethod.POST)
    public String createVoices(@RequestBody AudioMetadata audioMetadata) {
        System.out.println(audioMetadata.getPlaintext() +", "+ audioMetadata.getVoice() +", "+ audioMetadata.getFormat());
        return "Audio created successfully";
    }   

}
