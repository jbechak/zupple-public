package com.zupple.controller;

import com.zupple.repository.ISudokuRepository;
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
    private ISudokuRepository repository;

    @GetMapping()
    public List<SudokuModel> getAll() {
        return repository.getAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<SudokuModel> getByUser(@PathVariable int userId) {
        return repository.getByUser(userId);
    }

    @GetMapping("/{id}")
    public SudokuModel getSudoku(@PathVariable int id) {
        return repository.getSudoku(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/generate")
    public SudokuModel generateSudoku(@RequestBody SudokoGenerateDto dto) {
        try {
            var sudoku = new SudokuModel();
            int difficulty = dto.getDifficulty();
            sudoku.setDifficulty(difficulty);
            sudoku.setShowTitle(dto.getShowTitle());
            sudoku.setShowDifficulty(dto.getShowDifficulty());
            var blockGenerator = new BlockGenerator();
            sudoku.setGridString(blockGenerator.createBoard(difficulty));
            sudoku.setTitle(dto.getTitle());
            return sudoku;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("generating"), e);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public SudokuModel createSudoku(@RequestBody SudokuSaveDto dto) {
        try {
            return repository.createSudoku(dto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("creating"), e);
        }
    }

    @PutMapping("")
    public SudokuModel updateSudoku(@RequestBody SudokuSaveDto dto) {
        try {
            return repository.updateSudoku(dto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("updating"), e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteSudoku(@PathVariable int id) {
        try {
            repository.deleteSudoku(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("deleting"), e);
        }
    }

    private String getErrorMessage(String action) {
        return "An error occurred while " + action + " the sudoku";
    }
}