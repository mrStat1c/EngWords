package com.example.englishwordslearning;

import java.util.HashMap;
import java.util.Map;

public class WordsHolder {

    public static final Map<String, String> WORDS = new HashMap<>();

    static {
        WORDS.put("cat", "кот");
        WORDS.put("dog", "собака");
        WORDS.put("parrot", "попугай");
    }


}
