package com.zupple.utilities.sudoku.shapes;

import com.zupple.utilities.sudoku.Board;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Row {
    private List<Block> blockRow = new ArrayList<>();
    private int rowIndex;
    private static List<Integer> listOf9 = Stream.of(1,2,3,4,5,6,7,8,9).collect(Collectors.toList());
    private Random generate = new Random();

    public Row(int rowIndex) {
        this.rowIndex = rowIndex;
        this.blockRow = blockRow;

        List<Integer> tempList = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            tempList.add(i);
        }
        Collections.shuffle(tempList);
        for (int i = 0; i < 9; i++) {
            Block block = new Block(tempList.get(i));
            block.setColumnIndex(i);
            block.setRowIndex(rowIndex);
            blockRow.add(block);
        }
    }

    public void shuffle9Blocks() {
        List<Block> shuffledBlocks = new ArrayList<>();
        Collections.shuffle(listOf9);
        for (int i = 0; i < 9; i++) {
            Block block = new Block(listOf9.get(i));
            shuffledBlocks.add(block);
        }
        blockRow = shuffledBlocks;
    }

    public boolean shuffleRowUntilGood(Board board) {
        int currentRow = getRowIndex();
        if ((currentRow + 1) % 3 == 0) {
            return tryToGetThirdRow(board, currentRow);
        } else {
            shuffle9Blocks();
            int attempts = 0;
            boolean isUnique;
            do {
                isUnique = (isUniqueAgainstColumns(board, currentRow) && isUniqueAgainstSquares(board, currentRow));
                attempts++;
            } while (!isUnique && attempts < currentRow * 1000);
            return isUnique;
        }
    }

    private boolean tryToGetThirdRow(Board board, int currentRow) {
        int firstRowInSquare = currentRow / 3 * 3;
        var thirdRowValues = Stream.of(0,0,0,0,0,0,0,0,0).collect(Collectors.toList());
        Collections.shuffle(listOf9);
        shuffle9Blocks();
        for (int i = firstRowInSquare; i < firstRowInSquare + 3; i++) {
            Square currentSquare = board.getBlockSquare(i);
            var firstSixSquareValues = getFirstSixValuesFromSquare(currentSquare, currentRow);
            var newSquareValues = listOf9.stream().filter(x -> !firstSixSquareValues.contains(x) && !thirdRowValues.contains(x)).collect(Collectors.toList());
            if (newSquareValues.size() < 3 || !addThirdRowToSquare(board, currentRow, newSquareValues, thirdRowValues, i % 3 * 3)) {
                return false;
            }
        }
        return true;
    }

    private boolean addThirdRowToSquare(Board board, int currentRow, List<Integer> newSquareValues, List<Integer> thirdRowValues, int startColumn) {
        List<List<Integer>> allCombinationsOfNewValues = new ArrayList<>(Arrays.asList(
            newSquareValues,
            new ArrayList<Integer>(List.of(newSquareValues.get(0), newSquareValues.get(2), newSquareValues.get(1))),
            new ArrayList<Integer>(List.of(newSquareValues.get(1), newSquareValues.get(0), newSquareValues.get(2))),
            new ArrayList<Integer>(List.of(newSquareValues.get(1), newSquareValues.get(2), newSquareValues.get(0))),
            new ArrayList<Integer>(List.of(newSquareValues.get(2), newSquareValues.get(0), newSquareValues.get(1))),
            new ArrayList<Integer>(List.of(newSquareValues.get(2), newSquareValues.get(1), newSquareValues.get(0)))
        ));

        for (var valueList : allCombinationsOfNewValues) {
            int currentColumn = startColumn;
            for (var value : valueList) {
                Block block = new Block(value);
                blockRow.set(currentColumn, block);
                thirdRowValues.set(currentColumn, value);
                currentColumn++;
            }
            if (isUniqueAgainstColumnsInSquare(board, currentRow, startColumn))
                return true;
        }
        return false;
    }

    private List<Integer> getFirstSixValuesFromSquare(Square square, int currentRow) {
        var result = new ArrayList<Integer>();
        int rowsToIterate = currentRow - (currentRow / 3 * 3);
        for (int squareIndex = 0; squareIndex < rowsToIterate * 3; squareIndex++) {
            int blockValue = square.getBlockValue(squareIndex);
            result.add(blockValue);
        }
        return result;
    }

    private boolean isUniqueAgainstColumns(Board board, int currentRow) {
        for (int i = 0; i < 9; i++) {
            Column currentColumn = board.getColumn(i);
            for (int row = 0; row < currentRow; row++) {
                if (getNum(i) == currentColumn.getBlockValue(row)) {
                    shuffle9Blocks();
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isUniqueAgainstColumnsInSquare(Board board, int currentRow, int startColumn) {
        for (int i = startColumn; i < startColumn + 3; i++) {
            Column currentColumn = board.getColumn(i);
            for (int row = 0; row < currentRow; row++) {
                if (getNum(i) == currentColumn.getBlockValue(row)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isUniqueAgainstSquares(Board board, int currentRow) {
        if (currentRow % 3 == 0) {
            return true;
        }
        int firstRowInSquare = currentRow / 3 * 3;
        for (int i = firstRowInSquare; i < firstRowInSquare + 3; i++) {
            Square currentSquare = board.getBlockSquare(i);
            if (!isUniqueAgainst1Square(currentSquare, currentRow)) {
                shuffle9Blocks();
                return false;
            }
        }
        return true;
    }

    private boolean isUniqueAgainst1Square(Square square, int currentRow) {
        int rowsToIterate = currentRow - (currentRow / 3 * 3);
        for (int currentIndex = square.getX(); currentIndex < square.getX() + 3; currentIndex++) {
            for (int squareIndex = 0; squareIndex < rowsToIterate * 3; squareIndex++) {
                if (getNum(currentIndex) == square.getBlockValue(squareIndex)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void alternateRow9Builder(Board board) {
        for (int i = 0; i < 9; i++) {
            Column column = board.getColumn(i);
            int missingNum = 45;
            for (int j = 0; j < 8; j++) {
                missingNum -= column.getBlockValue(j);
            }
            setNum(i, missingNum);
            column.setBlockValue(8, missingNum);
        }
    }

    public Integer getNum(int index) {
        return blockRow.get(index).getValue();
    }

    public Block getBlock(int index) {
        return blockRow.get(index);
    }

    public void setNum(int index, int value) {
        blockRow.get(index).setValue(value);
    }

    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public String toString() {
        String rowString = "";
        for (int i = 0; i < 9; i++) {
            rowString += getNum(i);
        }
        return rowString;
    }
}
