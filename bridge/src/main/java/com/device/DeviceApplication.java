/*
 * @@file: 
 * @@brief: 
 * @@author: Vivek
 * @@date: 
 */
package com.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication(scanBasePackages = {
		"com.device",
		"com.sucurity"
})
@EnableScheduling
public class DeviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceApplication.class, args);
	}

}
