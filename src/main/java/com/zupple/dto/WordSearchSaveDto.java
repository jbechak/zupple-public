package com.zupple.dto;

import java.util.ArrayList;
import java.util.List;

public class WordSearchSaveDto {

    private int id = 0;
    private int userId = 0;
    private String title;
    private String description = "";
    private String difficulty;
    private Boolean showDifficulty;
    private int width;
    private int height;
    private String genre = "";
    private String instructions = "";
    private String gridString;
    private String htmlPath;
    private List<String> wordCollection = new ArrayList<>();
    private List<String> usedWords = new ArrayList<>();
    private List<String> unusedWords = new ArrayList<>();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean getShowDifficulty() {
        return showDifficulty;
    }
    public void setShowDifficulty(Boolean showDifficulty) {
        this.showDifficulty = showDifficulty;
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

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getGridString() { return gridString; }
    public void setGridString(String gridString) { this.gridString = gridString; }

    public List<String> getWordCollection() {
        return wordCollection;
    }
    public void setWordCollection(List<String> wordCollection) {
        this.wordCollection = wordCollection;
    }

    public List<String> getUnusedWords() {
        return unusedWords;
    }
    public void setUnusedWords(List<String> unusedWords) {
        this.unusedWords = unusedWords;
    }

    public List<String> getUsedWords() {
        return usedWords;
    }
    public void setUsedWords(List<String> usedWords) {
        this.usedWords = usedWords;
    }
}
