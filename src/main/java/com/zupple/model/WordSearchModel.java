package com.zupple.model;

import com.zupple.Instructions;
import com.zupple.puzzle.Grid;
import com.zupple.puzzle.WordList;
import java.util.ArrayList;
import java.util.List;

public class WordSearchModel {

    private String gridString;
    private int id = 0;
    private int userId = 0;
    private String title;
    private int width;
    private int height;
    private List<String> wordCollection = new ArrayList<>();
    private List<String> unusedWords = new ArrayList<>();
    private WordList wordList = new WordList();
    private int wordDirections = 1;
    private int wordCount;
    private String difficulty;
    private String genre = "";
    private String instructions = "";
    private String description = "";
    private String creator = "";
    private String gridPath;
    private String htmlPath;

    private final int FULL_WIDTH = 81;

    public WordSearchModel(String title) {
        this.title = title;
    }

    public WordSearchModel() {}

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

    public String getHtmlPath() {
        return htmlPath;
    }
    public void setHtmlPath(String htmlPath) {
        this.htmlPath = htmlPath;
    }

    public int getWordDirections() {
        return wordDirections;
    }
    public void setWordDirections(int wordDirections) {
        this.wordDirections = wordDirections;
    }

    public void setGridString(String gridString) { this.gridString = gridString; }
    public String getGridString() { return gridString; }

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

    public void populateWordList(List<String> wordCollection) {
        wordList.populateFromStringList(wordCollection);
    }
    public void setWordList(WordList wordList) {
        this.wordList = wordList;
    }
    public WordList getWordList() {
        return wordList;
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

    public void setDifficulty(Grid grid) {
        if (getCredits(grid) >= 8) {
            this.difficulty = "Level 5 - Very Difficult";
        }
        else if (getCredits(grid) >= 6) {
            this.difficulty = "Level 4 - Difficult";
        }
        else if (getCredits(grid) >= 3) {
            this.difficulty = "Level 3 - Medium";
        }
        else if (getCredits(grid) >= 1) {
            this.difficulty = "Level 2 - Easy";
        }
        else this.difficulty = "Level 1 - Very Easy";
    }

    private int getCredits(Grid grid) {
        int credits = 0;
        credits += grid.remainingSpaces() / 50;
        credits += wordDirections - 1;
        credits += (wordCount - 1) / 10;
        return credits;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
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

    public void createInstructions() {
        Instructions instructMaker = new Instructions();
        if (wordDirections == 1) {
            this.instructions = instructMaker.getINSTRUCTIONS_1();
        }
        if (wordDirections == 2) {
            this.instructions = instructMaker.getINSTRUCTIONS_2();
        }
        if (wordDirections == 3) {
            this.instructions = instructMaker.getINSTRUCTIONS_3();
        }
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