package com.zupple.dto;

import java.util.List;
import java.util.Map;

public class CrosswordSaveDto {

    private int id = 0;
    private int userId = 0;
    private String title;
    private String description = "";
    private String difficulty;
    private int width;
    private int height;
    private String genre = "";
    private String instructions = "";
    private String gridString;
    private Map<String, String> wordClues;
    private List<String> downClueList;
    private List<String> acrossClueList;

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

    public String getTitle() { return title; }
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

    public String getGridString() {
        return gridString;
    }
    public void setGridString(String gridString) {
        this.gridString = gridString;
    }

    public Map<String, String> getWordClues() {
        return wordClues;
    }
    public void setWordClues(Map<String, String> wordClues) {
        this.wordClues = wordClues;
    }

    public List<String> getDownClueList() { return downClueList; }
    public void setDownClueList(List<String> downClueList) {
        this.downClueList = downClueList;
    }

    public List<String> getAcrossClueList() {
        return acrossClueList;
    }
    public void setAcrossClueList(List<String> acrossClueList) {
        this.acrossClueList = acrossClueList;
    }
}