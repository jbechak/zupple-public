package com.zupple.dto;

import java.util.ArrayList;
import java.util.List;

public class WordSearchGenerateDto {
    private String title;
    private int width;
    private int height;
    private int wordDirections;
    private List<String> wordCollection = new ArrayList<>();
    private Boolean showDifficulty;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getWordDirections() {
        return wordDirections;
    }
    public void setWordDirections(int wordDirections) {
        this.wordDirections = wordDirections;
    }

    public List<String> getWordCollection() {
        return wordCollection;
    }
    public void setWordCollection(List<String> wordCollection) {
        this.wordCollection = wordCollection;
    }

    public Boolean getShowDifficulty() {
        return showDifficulty;
    }
    public void setShowDifficulty(Boolean showDifficulty) {
        this.showDifficulty = showDifficulty;
    }

}
