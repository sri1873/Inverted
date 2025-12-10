package com.portfolio.inverted;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class InvertedApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvertedApplication.class, args);
        List<String> paths = new ArrayList<>();

		FileReader fileReader= new FileReader();
		fileReader.readFile(paths);
	}


}
