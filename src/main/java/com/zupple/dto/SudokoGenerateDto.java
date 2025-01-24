package com.zupple.dto;

public class SudokoGenerateDto {
    private String title;
    private Boolean showTitle;
    private int difficulty;
    private Boolean showDifficulty;

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getShowTitle() {
        return showTitle;
    }
    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
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
}
