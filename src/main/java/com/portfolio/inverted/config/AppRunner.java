package com.portfolio.inverted.config;

import com.portfolio.inverted.service.FileProcessor;
import com.portfolio.inverted.service.Search;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

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

    }

}
