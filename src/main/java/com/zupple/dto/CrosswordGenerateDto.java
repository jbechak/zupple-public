package com.zupple.dto;

import java.util.Map;

public class CrosswordGenerateDto {
    private String title;
    private Map<String, String> wordClues;

    public Map<String, String> getWordClues() {
        return wordClues;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}