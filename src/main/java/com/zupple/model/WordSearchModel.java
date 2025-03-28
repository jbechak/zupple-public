package com.zupple.model;

import java.util.ArrayList;
import java.util.List;

public class WordSearchModel {
    private String gridString;
    private int id = 0;
    private int userId = 0;
    private String title;
    private int width;
    private int height;

    //rename these once the front end is ready
    private List<String> usedWords = new ArrayList<>();
    private List<String> unusedWords = new ArrayList<>();

    //get rid of this, once the front end is ready
    private List<String> wordCollection = new ArrayList<>();
    private int wordDirections = 1;
    private String difficulty;
    private Boolean showDifficulty;
    private String genre = "";
    private String instructions = "";
    private String description = "";
    private String creator = "";


    public WordSearchModel(String title) {
        this.title = title;
    }

    public WordSearchModel(
            String title,
            int width,
            int height,
            int wordDirections,
            List<String> usedWords,
            List<String> unusedWords,
            String gridString,
            String difficulty,
            boolean showDifficulty,
            String instructions,
            List<String> wordCollection
    ) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.wordDirections = wordDirections;
        this.usedWords = usedWords;
        this.unusedWords = unusedWords;
        this.gridString = gridString;
        this.difficulty = difficulty;
        this.showDifficulty = showDifficulty;
        this.instructions = instructions;
        this.wordCollection = wordCollection;
    }

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

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean getShowDifficulty() { return showDifficulty; }
    public void setShowDifficulty(Boolean showDifficulty) { this.showDifficulty = showDifficulty; }

    public int getWordDirections() {
        return wordDirections;
    }
    public void setWordDirections(int wordDirections) {
        this.wordDirections = wordDirections;
    }

    public void setGridString(String gridString) { this.gridString = gridString; }
    public String getGridString() { return gridString; }

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

    public List<String> getWordCollection() {
        return wordCollection;
    }
    public void setWordCollection(List<String> wordCollection) {
        this.wordCollection = wordCollection;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return this.difficulty;
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

    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
}