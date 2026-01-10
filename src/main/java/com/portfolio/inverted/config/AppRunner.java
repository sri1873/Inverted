package com.portfolio.inverted.config;

import com.portfolio.inverted.FileProcessor;
import com.portfolio.inverted.Search;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

@Component
public class AppRunner implements CommandLineRunner {
    private final FileProcessor fileProcessor;
    private final Search search;

    public AppRunner(FileProcessor fileProcessor, Search search) {
        this.fileProcessor = fileProcessor;
        this.search = search;
    }

    @Override
    public void run(String... args) {
        List<String> paths = List.of(
                "O://Portfolio//Inverted//src//main//resources//test-1.txt",
                "O://Portfolio//Inverted//src//main//resources//test-2.txt",
                "O://Portfolio//Inverted//src//main//resources//test-3.txt",
                "O://Portfolio//Inverted//src//main//resources//test-4.txt",
                "O://Portfolio//Inverted//src//main//resources//test-5.txt"

        );

        try {
            fileProcessor.readFile(paths);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("enter query:");
        String query = scanner.nextLine();

        List<Integer> res = search.booleanAndSearch(query);
        if (res.isEmpty()) {
            System.out.println("not found");
        } else {
            res.forEach(System.out::println);
        }
    }

}
