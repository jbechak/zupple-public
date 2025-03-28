package com.zupple.businessObjects;

import com.zupple.dto.SudokoGenerateDto;
import com.zupple.model.SudokuModel;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Sudoku implements ISudoku {
    @Override
    public SudokuModel generateSudoku(SudokoGenerateDto dto) {
        var gridString = getGridString(dto.getDifficulty());
        return new SudokuModel(dto.getTitle(), dto.getShowTitle(), dto.getDifficulty(), dto.getShowDifficulty(), gridString);
    }

    private String getGridString(int difficulty) {
        int[][] matrix = getMatrix(difficulty);
        String gridString = "";

        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 9; col++)
                gridString += matrix[row][col];

        return gridString;
    }

    private int[][] getMatrix(int difficulty) {
        int[][] matrix = new int[9][9];
        for (var row : matrix)
            Arrays.fill(row, 0);

        fillMatrix(matrix, 0, 0);
        addBlanks(matrix, difficulty);
        return matrix;
    }

    private boolean fillMatrix(int[][] matrix, int row, int col) {
        if (row == 8 && col == 9)
            return true;

        if (col == 9) {
            row++;
            col = 0;
        }

        var random = new Random();
        int randomFactor = random.nextInt(9);

        for (int value = 1; value <= 9; value++) {
            value += randomFactor;
            if (value > 9) value -= 9;

            if (isSafe(matrix, row, col, value)) {
                matrix[row][col] = value;
                 if (fillMatrix(matrix, row, col + 1))
                     return true;

                 matrix[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isSafe(int[][] matrix, int row, int col, int value) {
        //check row
        for (int x = 0; x < 9; x++)
            if (matrix[row][x] == value)
                return false;

        //check column
        for (int y = 0; y < 9; y++)
            if (matrix[y][col] == value)
                return false;

        //check submatrix
        int startRow = row - (row % 3);
        int startCol = col - (col% 3);

        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                if (matrix[y + startRow][x + startCol] == value)
                    return false;

        return true;
    }

    private void addBlanks(int[][] matrix, int difficulty) {
        int blanks = difficulty * 6;
        var random = new Random();
        List<Integer> gridIndexes = Stream.iterate(0, n -> n + 1)
            .limit(81)
            .collect(Collectors.toList());

        for (int i = 0; i < blanks; i++) {
            int listIndex = random.nextInt(gridIndexes.size());
            int row = gridIndexes.get(listIndex) / 9;
            int col = gridIndexes.get(listIndex) % 9;
            matrix[row][col] = 0;
            gridIndexes.remove(listIndex);
        }
    }
}