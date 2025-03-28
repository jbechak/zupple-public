package com.zupple.model;

public class SudokuModel {

    public SudokuModel() {}
    public SudokuModel(String title, boolean showTitle, int difficulty, boolean showDifficulty, String gridString) {
        this.title = title;
        this.showTitle = showTitle;
        this.difficulty = difficulty;
        this.showDifficulty = showDifficulty;
        this.gridString = gridString;
    }
    private int id = 0;
    private int userId = 0;
    private String title;
    private boolean showTitle;
    private int difficulty;
    private boolean showDifficulty;
    private String gridString;
    private String instructions = "Fill in each blank with the correct numbers so that every column contains the numbers 1 thru 9 " +
            "with no duplicates, every row contains the numbers 1 thru 9 with no duplicates, and every 3x3 box " +
            "contains the numbers 1 thru 9 with no duplicates.";

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

//    public void createTitle() {
//        title = "Level " + difficulty;
//    }

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
