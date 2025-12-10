package com.portfolio.inverted;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Analyser {

    public List<String> onlyWords(String line){
        line=line.toLowerCase();
        Pattern noSymbols= Pattern.compile("[a-z]{2,}");
        Matcher m= noSymbols.matcher(line);
        List<String> document=new ArrayList<>();
        while(m.find())
            document.add(m.group());
        return document;
    }
    public void dateFinder(String line){
        //for future implementation
    }
}
