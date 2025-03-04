package com.zupple.repository;

import com.zupple.dto.SudokuSaveDto;
import com.zupple.model.SudokuModel;

import java.util.List;

public interface ISudokuRepository {
    List<SudokuModel> getAll();
    List<SudokuModel> getByUser(int userId);
    SudokuModel getSudoku(int sudokuId);
    SudokuModel createSudoku(SudokuSaveDto sudoku);
    SudokuModel updateSudoku(SudokuSaveDto sudoku);
    void deleteSudoku(int id);
}
