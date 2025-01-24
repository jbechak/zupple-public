package com.zupple.utilities.crossword;

import com.zupple.puzzle.Grid;
import com.zupple.puzzle.Puzzle;
import com.zupple.puzzle.Word;
import com.zupple.puzzle.WordList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrosswordPuzzle extends Puzzle {
    private Map<String, String> wordClues;
    private List<String> wordCollection = new ArrayList<>();
    private WordList wordList = new WordList();
    private WordList finalWordList = new WordList();
    private WordList sortedWordList;
    private int wordCount;
    private final int GRID_SIZE = 120;
    private Grid grid = new Grid(GRID_SIZE, GRID_SIZE);
    private List<String> downClueList;
    private List<String> acrossClueList;



    public CrosswordPuzzle(String title) {
        super(title);
    }

    public CrosswordPuzzle(String title, Map<String, String> wordClues,
                           List<String> wordCollection, WordList wordList, int wordCount) {
        super(title);
        this.wordClues = wordClues;
        this.wordCollection = wordCollection;
        this.wordList = wordList;
        this.wordCount = wordCount;
    }

    public WordList getSortedWordList() {
        return sortedWordList;
    }

    public void setSortedWordList(WordList newWordList) {
        WordList sortedWordList = new WordList();
        for (Word word : newWordList.getWords()) {
            sortedWordList.addWord(word);
        }
        this.sortedWordList = sortedWordList;
    }

    public int getArea() {
        return grid.getWidth() * grid.getHeight();
    }

    public Map<String, String> getWordClues() {
        return wordClues;
    }

    public void setWordClues(Map<String, String> wordClues) {
        this.wordClues = wordClues;
    }

    public void populateWordCollection() {
        wordCollection.clear();

        for (String key : wordClues.keySet()) {
            wordCollection.add(key);
        }
    }

    public void populateWordList() {
        wordCollection.clear();
        wordList.getWords().clear();
        for (String key : wordClues.keySet()) {
            wordCollection.add(key);

            String upperCaseWord = key.toUpperCase();
            String[] brokenWord = upperCaseWord.split("");
            Word newWord = new Word(brokenWord);
            newWord.setClue(wordClues.get(key));

            wordList.getWords().add(newWord);
        }
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    @Override
    public WordList getWordList() {
        return wordList;
    }

    public void setWordList(WordList wordList) {
        this.wordList = wordList;
    }

    @Override
    public WordList getFinalWordList() {
        return finalWordList;
    }

    @Override
    public Grid getGrid() {
        return grid;
    }

    @Override
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    @Override
    public String toString() {
         return grid.toString() + finalWordList.toString();
    }

    public void createClueLists() {
        downClueList = new ArrayList<>();
        acrossClueList = new ArrayList<>();
        for (Word word : sortedWordList.getWords()) {
            if (word.getDirection().equals("down")) {
                downClueList.add(word.getNumber() + ". " + word.getClue());
            } else if (word.getDirection().equals("across")) {
                acrossClueList.add(word.getNumber() + ". " + word.getClue());
            }
        }
    }

    public List<String> getDownClueList() {
        return downClueList;
    }

    public List<String> getAcrossClueList() {
        return acrossClueList;
    }
}