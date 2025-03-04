package com.zupple.utilities.crossword;

import com.zupple.model.CrosswordModel;
import com.zupple.puzzleParts.Grid;
import com.zupple.puzzleParts.Word;
import com.zupple.puzzleParts.WordList;
import com.zupple.utilities.spaceFinder.HorizontalCrosswordSpaceFinder;
import com.zupple.utilities.spaceFinder.SpaceFinder;
import com.zupple.utilities.spaceFinder.VerticalCrosswordSpaceFinder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrosswordBuildingTools {
    private SpaceFinder spaceFinder = new SpaceFinder();
    private HorizontalCrosswordSpaceFinder horizontalSpaceFinder = new HorizontalCrosswordSpaceFinder();
    private VerticalCrosswordSpaceFinder verticalSpaceFinder = new VerticalCrosswordSpaceFinder();

    public CrosswordModel createGrid(CrosswordPuzzle puzzle) {
        puzzle.setWordCount(puzzle.getWordList().size());
        var thisPuzzle = buildCrosswordPuzzle(puzzle);

        var crossword = new CrosswordModel(puzzle.getTitle());
        updateWordClues(crossword, thisPuzzle);
        crossword.setWidth(thisPuzzle.getGrid().getWidth());
        crossword.setHeight(thisPuzzle.getGrid().getHeight());
        crossword.setWordClues(thisPuzzle.getWordClues());
        crossword.setDownClueList(thisPuzzle.getDownClueList());
        crossword.setAcrossClueList(thisPuzzle.getAcrossClueList());
        crossword.setGridString(thisPuzzle.getGrid().toString());

        return crossword;
    }

    public CrosswordPuzzle buildCrosswordPuzzle(CrosswordPuzzle puzzle) {
        List<CrosswordPuzzle> partialPuzzleList = new ArrayList<>();
        var bestPuzzle = new CrosswordPuzzle(puzzle.getTitle());
        int smallestArea = 10000;
        for (int i = 0; i < puzzle.getWordList().size() + 3; i++) {
            var newPuzzle = createNewTrialPuzzle(puzzle);

            if (newPuzzle.getFinalWordList().size() == newPuzzle.getWordList().size()) {
                if (newPuzzle.getArea() < smallestArea) {
                    smallestArea = newPuzzle.getArea();
                    bestPuzzle = newPuzzle.clone();
                }
            } else {
                partialPuzzleList.add(newPuzzle);
            }
        }
        if (bestPuzzle.getFinalWordList().size() < 1) {
            bestPuzzle = getBestPartialPuzzle(partialPuzzleList);
        }
        handleClueLists(bestPuzzle);
        return bestPuzzle;
    }

    private CrosswordPuzzle getBestPartialPuzzle(List<CrosswordPuzzle> puzzleList) {
        int maxWordCount = puzzleList.stream()
            .map(puzzle -> puzzle.getWordCount())
            .max((p1, p2) -> p1 - p2)
            .orElse(-1);

        var bestPuzzles = puzzleList.stream()
            .filter(puzzle -> puzzle.getFinalWordList().size() == maxWordCount)
            .collect(Collectors.toList());

        return Collections.min(bestPuzzles, Comparator.comparingInt(CrosswordPuzzle::getArea));
    }

    private void handleClueLists(CrosswordPuzzle puzzle) {
        puzzle.getFinalWordList().sortWordList();
        puzzle.setSortedWordList(puzzle.getFinalWordList());
        puzzle.createClueLists();
    }

    private void updateWordClues(CrosswordModel crosswordModel, CrosswordPuzzle puzzle) {
        Map<String, String> usedWordClues = puzzle.getWordClues().entrySet().stream()
            .filter(entry -> puzzle.getFinalWordList().getWords().stream()
                .anyMatch(word -> word.toString().equals(entry.getKey()) &&
                    word.getClue().equals(entry.getValue())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, String> unusedWordClues = puzzle.getWordClues().entrySet().stream()
            .filter(entry -> !usedWordClues.containsKey(entry.getKey()) ||
                !usedWordClues.get(entry.getKey()).equals(entry.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        crosswordModel.setUsedWordClues(usedWordClues);
        crosswordModel.setUnusedWordClues(unusedWordClues);
    }

    private CrosswordPuzzle createNewTrialPuzzle(CrosswordPuzzle puzzle) {
        var newPuzzle = new CrosswordPuzzle(
            puzzle.getTitle(),
            puzzle.getWordClues(),
            puzzle.getWordCollection(),
            puzzle.getWordList(),
            puzzle.getWordCount()
        );
        newPuzzle.getWordList().resetInGrid();
        generateCrosswordPuzzle(newPuzzle);
        newPuzzle.setWordCount(newPuzzle.getFinalWordList().size());
        trimPuzzle(newPuzzle);
        return newPuzzle;
    }

    private CrosswordPuzzle trimPuzzle(CrosswordPuzzle puzzle) {
        int topY = topRow(puzzle.getGrid());
        int leftX = leftColumn(puzzle.getGrid());
        int bottomY = bottomRow(puzzle.getGrid(), topY);
        int rightX = rightColumn(puzzle.getGrid(), leftX);
        int newWidth = rightX - leftX;
        int newHeight = bottomY - topY;

        var grid = new Grid(newWidth, newHeight);

        for (int y = topY, newY = 0; y < bottomY; y++, newY++){
            for (int x = leftX, newX = 0; x < rightX; x++, newX++) {
                grid.setBlock(newX, newY, puzzle.getGrid().getBlock(x, y));
            }
        }
        puzzle.setGrid(grid);

        for (Word word : puzzle.getFinalWordList().getWords()) {
            word.setStartingY(word.getStartingY() - topY);
            word.setStartingX(word.getStartingX() - leftX);
        }
        return puzzle;
    }

    private int topRow(Grid grid) {
        int width = grid.getWidth();

        return IntStream.range(0, grid.getHeight())
            .filter(y -> IntStream.range(0, width).anyMatch(x -> !spaceFinder.isBlank(x, y, grid)))
            .findFirst()
            .orElse(0);
    }

    private int leftColumn(Grid grid) {
        int height = grid.getHeight();

        return IntStream.range(0, grid.getWidth())
            .filter(x -> IntStream.range(0, height).anyMatch(y -> !spaceFinder.isBlank(x, y, grid)))
            .findFirst()
            .orElse(0);
    }

    private int bottomRow(Grid grid, int topY) {
        int width = grid.getWidth();
        int height = grid.getHeight();

        return IntStream.range(topY, height)
            .filter(y -> IntStream.range(0, width).allMatch(x -> spaceFinder.isBlank(x, y, grid)))
            .findFirst()
            .orElse(height);
    }

    private int rightColumn(Grid grid, int leftX) {
        int width = grid.getWidth();
        int height = grid.getHeight();

        return IntStream.range(leftX, width)
            .filter(x -> IntStream.range(0, height).allMatch(y -> spaceFinder.isBlank(x, y, grid)))
            .findFirst()
            .orElse(width);
    }

    public void generateCrosswordPuzzle(CrosswordPuzzle puzzle) {
        Collections.shuffle(puzzle.getWordList().getWords());

        PlacementResults results = setFirstWord(getBlanklessWord(puzzle, 0), puzzle.getGrid());
        logWordEntry(results, puzzle, 0, false);
        int[] xy = {results.getStartingX(), results.getStartingY()};
        boolean goingDown = true;

        for (int j = 0; j < puzzle.getWordList().size(); j++) {
            if (!puzzle.getWordList().getWords().stream().anyMatch(w -> !w.isInGrid())) {
                break;
            }
            for (int i = 1; i < puzzle.getWordList().size(); i++) {
                var currentWord = getBlanklessWord(puzzle, i);
                if (!puzzle.getWordList().getWord(i).isInGrid()) {
                    setNextWord(puzzle, currentWord, i, goingDown, xy);
                }
                uncheckAllWords(puzzle.getWordList());
                goingDown = !goingDown;
            }
        }
    }

    private void setNextWord(CrosswordPuzzle puzzle, Word currentWord, int i, boolean goingDown, int[] xy) {
        PlacementResults results = placeWord(currentWord, puzzle.getGrid(), goingDown, xy);

        if (results != null) {
            logWordEntry(results, puzzle, i, goingDown);
            xy = setXY(results.getStartingX(), results.getStartingY());

        } else {
            for (Word word : puzzle.getWordList().getWords()) {
                if (!word.equals(puzzle.getWordList().getWord(i)) && !word.isChecked() && !puzzle.getWordList().getWord(i).isInGrid()) {
                    xy = setXY(word.getStartingX(), word.getStartingY());
                    word.setChecked(true);
                    setNextWord(puzzle, currentWord, i, goingDown, xy);
                }
            }
        }
    }

    private void uncheckAllWords(WordList wordList) {
        for (Word word : wordList.getWords()) {
            word.setChecked(false);
        }
    }

    private int[] setXY(int x, int y) {
        return new int[] { x, y };
    }

    private CrosswordPuzzle logWordEntry(PlacementResults results, CrosswordPuzzle puzzle, int i, boolean goingDown) {
        puzzle.getWordList().getWord(i).setInGrid(true);
        puzzle.getWordList().getWord(i).setStartingX(results.getStartingX());
        puzzle.getWordList().getWord(i).setStartingY(results.getStartingY());
        if (goingDown) {
            puzzle.getWordList().getWord(i).setDirection("down");
        } else {
            puzzle.getWordList().getWord(i).setDirection("across");
        }
        puzzle.getFinalWordList().add(puzzle.getWordList().getWord(i));
        return puzzle;
    }

    private Word getBlanklessWord(CrosswordPuzzle puzzle, int index) {
        Word newWord =new Word(puzzle.getWordList().getWord(index).getLetterArray());
        newWord.withoutSpace();
        return newWord;
    }

    public PlacementResults setFirstWord(Word word, Grid grid) {
        int startingX = grid.getWidth() / 2 - word.length() / 2;
        int startingY = grid.getHeight() / 2;
        horizontalSpaceFinder.addWord(word, grid, startingX, startingY);
        PlacementResults placementResults = new PlacementResults(true, startingX, startingY);
        return placementResults;
    }

    private PlacementResults placeWord(Word word, Grid grid, boolean goingDown, int[] xy) {
        if (!goingDown) {
            return horizontalSpaceFinder.writeWordInEmptySpace(word, grid, xy);
        }
        else {
            return verticalSpaceFinder.writeWordInEmptySpace(word, grid, xy);
        }
    }
}
