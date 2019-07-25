package com.ethan.io.socketIO.socketIODemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"**.com.ethan.io.**","**.org.ethan.io.**"})
public class SocketIoDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketIoDemoApplication.class, args);
	}

}
