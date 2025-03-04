package com.zupple.model;

import java.util.List;
import java.util.Map;

public class CrosswordModel {
    private int id =0;
    private int userId = 0;
    private String title;
    private int width;
    private int height;
    private Map<String, String> wordClues;
    private Map<String, String> usedWordClues;
    private Map<String, String> unusedWordClues;
    private String gridString;
    private List<String> downClueList;
    private List<String> acrossClueList;
    private String difficulty;
    private String genre = "";
    private String instructions = "For each clue, find the word in the puzzle that it is referring to.";
    private String description = "";
    private String gridPath = "";

    public CrosswordModel() {
    }

    public CrosswordModel(String title) {
        this.title = title;
    }

    public String getGridPath() {
        return gridPath;
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

    public List<String> getDownClueList() {
        return downClueList;
    }
    public void setDownClueList(List<String> downClueList) {
        this.downClueList = downClueList;
    }

    public List<String> getAcrossClueList() {
        return acrossClueList;
    }
    public void setAcrossClueList(List<String> acrossClueList) {
        this.acrossClueList = acrossClueList;
    }

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

    public String getGridString() {
        return gridString;
    }
    public void setGridString(String gridString) {
        this.gridString = gridString;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getWordClues() {
        return wordClues;
    }
    public void setWordClues(Map<String, String> wordClues) {
        this.wordClues = wordClues;
    }

    public Map<String, String> getUsedWordClues() {
        return usedWordClues;
    }
    public void setUsedWordClues(Map<String, String> usedWordClues) {
        this.usedWordClues = usedWordClues;
    }

    public Map<String, String> getUnusedWordClues() {
        return unusedWordClues;
    }
    public void setUnusedWordClues(Map<String, String> unusedWordClues) {
        this.unusedWordClues = unusedWordClues;
    }
}