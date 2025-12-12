package com.portfolio.inverted;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class InvertedApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvertedApplication.class, args);
        List<String> paths = new ArrayList<>();

		FileReader fileReader= new FileReader();
		fileReader.readFile(paths);

		Indexer indexer=new Indexer();
		Scanner scanner=new Scanner(System.in);
		System.out.println("enter query:");
		String query= scanner.nextLine();
		System.out.println(query);
		List<Integer> search = indexer.search(query);
		for (Integer s:search){
			System.out.println(s);
		}
	}


}
