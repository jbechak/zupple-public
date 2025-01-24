package com.zupple.controller;

import com.zupple.dao.SudokuDao;
import com.zupple.dto.SudokoGenerateDto;
import com.zupple.dto.SudokuSaveDto;
import com.zupple.model.SudokuModel;
import com.zupple.utilities.sudoku.BlockGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sudoku")
public class SudokuController {

    @Autowired
    private SudokuDao dao;

    @GetMapping()
    public List<SudokuModel> getAll() {
        return dao.getAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<SudokuModel> getByUser(@PathVariable int userId) {
        return dao.getByUser(userId);
    }

    @GetMapping("/{id}")
    public SudokuModel getSudoku(@PathVariable int id) {
        return dao.getSudoku(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/generate")
    public SudokuModel generateSudoku(@RequestBody SudokoGenerateDto dto) {
        try {
            SudokuModel sudoku = new SudokuModel();
            int difficulty = dto.getDifficulty();
            sudoku.setDifficulty(difficulty);
            sudoku.setShowTitle(dto.getShowTitle());
            sudoku.setShowDifficulty(dto.getShowDifficulty());
            BlockGenerator blockGenerator = new BlockGenerator();
            sudoku.setGridString(blockGenerator.createBoard(difficulty));
            sudoku.setTitle(dto.getTitle());
            return sudoku;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public SudokuModel createSudoku(@RequestBody SudokuSaveDto dto) {
        try {
            return dao.createSudoku(dto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @PutMapping("")
    public SudokuModel updateSudoku(@RequestBody SudokuSaveDto dto) {
        try {
            return dao.updateSudoku(dto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteSudoku(@PathVariable int id) {
        try {
            dao.deleteSudoku(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }
}