package com.zupple.dto;

public class SudokuSaveDto {

    private int id = 0;
    private int userId = 0;
    private String title;
    private Boolean showTitle;
    private int difficulty;
    private Boolean showDifficulty;
    private String gridString;
    private String instructions;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getShowTitle() {
        return showTitle;
    }
    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
    }

    public void createTitle() {
        title = "Level " + difficulty + " Puzzle";
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

    public int getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean getShowDifficulty() {
        return showDifficulty;
    }
    public void setShowDifficulty(Boolean showDifficulty) {
        this.showDifficulty = showDifficulty;
    }
    public String getGridString() {
        return gridString;
    }
    public void setGridString(String gridString) {
        this.gridString = gridString;
    }

    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

}
