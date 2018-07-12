package com.polly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author pradeep_ga
 * This application consumes plaintext and converts text to speech using AmazonPolly.
 *
 */
@SpringBootApplication
public class TextToSpeechApplication {

	/**
	 * @param args - command line args...
	 */
	public static void main(String[] args) {
		SpringApplication.run(TextToSpeechApplication.class, args);
	}
}
