package com.zupple.utilities.wordsearch;

import com.zupple.model.WordSearchModel;
import com.zupple.puzzleParts.Grid;
import com.zupple.puzzleParts.Word;
import com.zupple.puzzleParts.WordList;
import com.zupple.utilities.spaceFinder.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class BuildingTools {
    private SpaceFinder spaceFinder = new SpaceFinder();
    private VerticalSpaceFinder verticalSpaceFinder = new VerticalSpaceFinder();
    private HorizontalSpaceFinder horizontalSpaceFinder = new HorizontalSpaceFinder();
    private DiagonalDownSpaceFinder diagonalSpaceFinder = new DiagonalDownSpaceFinder();
    private DiagonalUpSpaceFinder diagonalUpSpaceFinder = new DiagonalUpSpaceFinder();

    public void createWordSearch(WordSearchModel wordSearch) {
        var grid = new Grid(wordSearch.getWidth(), wordSearch.getHeight());
        wordSearch.populateWordList(wordSearch.getWordCollection());
        wordSearch.setWordCount(wordSearch.getWordList().size());
        generateWordSearch(wordSearch, grid);
        updateWordCollection(wordSearch);

        grid.fillWithRandomLetters();
        wordSearch.setDifficulty(grid);
        wordSearch.setGridString(grid.toString());
        wordSearch.setWordCount(wordSearch.getWordList().size());
        wordSearch.createInstructions();
    }

    private void updateWordCollection(WordSearchModel wordSearch) {
        var usedWords = wordSearch
            .getWordList()
            .getWords()
            .stream()
            .map(Object::toString)
            .collect(Collectors.toList());

        var unusedWords = new ArrayList<String>();
        for (var word : wordSearch.getWordCollection()) {
            if (!usedWords.contains(word))
                unusedWords.add(word);
        }

        wordSearch.setUsedWords(usedWords);
        wordSearch.setUnusedWords(unusedWords);
    }

    public void generateWordSearch(WordSearchModel puzzle, Grid grid) {
        var wordList = new WordList();
        boolean isAllWordsInPuzzle = false;
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < puzzle.getWordList().size(); i++) {
                boolean success = false;
                var currentWord = getBlanklessWord(puzzle, i);
                if (!puzzle.getWordList().getWord(i).isInGrid()) {
                    if (puzzle.getWordDirections() == 1) {
                        success = easyWord(currentWord, grid);
                    }
                    if (puzzle.getWordDirections() == 2) {
                        success = mediumWord(currentWord, grid);
                    }
                    if (puzzle.getWordDirections() == 3) {
                        success = hardWord(currentWord, grid);
                    }
                }
                if (success) {
                    grid.updateUsedSpaces(currentWord.length());
                    puzzle.getWordList().getWord(i).setInGrid(true);
                    wordList.add(puzzle.getWordList().getWord(i));
                }

                isAllWordsInPuzzle = puzzle.getWordList().size() == wordList.size();
                if (isAllWordsInPuzzle) break;
            }
            if (isAllWordsInPuzzle) break;
        }
        puzzle.setWordList(wordList);
    }

    private Word getBlanklessWord(WordSearchModel puzzle, int index) {
        var newWord = new Word(puzzle.getWordList().getWord(index).getLetterArray());
        newWord.withoutSpace();
        return newWord;
    }

    private boolean easyWord(Word word, Grid grid) {
        var generate = new Random();
        int direction = generate.nextInt(2);
        return placeEasy(word, grid, direction);
    }

    private boolean mediumWord(Word word, Grid grid) {
        var generate = new Random();
        int direction = generate.nextInt(4);
        return placeEasy(word, grid, direction)
            ? true
            : placeMedium(word, grid, direction);
    }

    private boolean hardWord(Word word, Grid grid) {
        var generate = new Random();
        int flip = generate.nextInt(2);
        if (flip == 1) {
            word = word.flip();
        }
        int direction = generate.nextInt(4);
        return placeEasy(word, grid, direction)
            ? true
            : placeMedium(word, grid, direction);
    }

    private boolean placeEasy(Word word, Grid grid, int direction) {
        if (direction == 0) {
            return horizontalSpaceFinder.writeWordInEmptySpace(word, grid);
        }
        if (direction == 1) {
            return verticalSpaceFinder.writeWordInEmptySpace(word, grid);
        }
        return false;
    }

    private boolean placeMedium(Word word, Grid grid, int direction) {
        if (direction == 2) {
            return diagonalSpaceFinder.writeWordInEmptySpace(word, grid);
        }
        if (direction == 3) {
            return diagonalUpSpaceFinder.writeWordInEmptySpace(word, grid);
        }
        return false;
    }
}