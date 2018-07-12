package com.polly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

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
