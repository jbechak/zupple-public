package com.zupple.dto;

public class SudokoGenerateDto {
    private String title;
    private boolean showTitle;
    private int difficulty;
    private boolean showDifficulty;

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getShowTitle() {
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

    public boolean getShowDifficulty() {
        return showDifficulty;
    }
    public void setShowDifficulty(Boolean showDifficulty) {
        this.showDifficulty = showDifficulty;
    }
}
