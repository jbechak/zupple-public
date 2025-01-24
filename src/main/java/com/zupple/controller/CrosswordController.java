package com.zupple.controller;

import com.zupple.utilities.crossword.CrosswordBuildingTools;
import com.zupple.utilities.crossword.CrosswordPuzzle;
import com.zupple.dao.CrosswordDao;
import com.zupple.dto.CrosswordGenerateDto;
import com.zupple.dto.CrosswordSaveDto;
import com.zupple.model.CrosswordModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/crossword")
public class CrosswordController {

    @Autowired
    private CrosswordDao dao;

    private CrosswordBuildingTools buildingTools = new CrosswordBuildingTools();

    @GetMapping()
    public List<CrosswordModel> getAll() {
        return dao.getAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<CrosswordModel> getByUser(@PathVariable int userId) {
        return dao.getByUser(userId);
    }

    @GetMapping("/{id}")
    public CrosswordModel getCrossword(@PathVariable int id) {
        return dao.getCrossword(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/generate")
    public CrosswordModel generateCrossword(@RequestBody CrosswordGenerateDto dto) {
        CrosswordPuzzle puzzle = new CrosswordPuzzle(dto.getTitle());
        puzzle.setWordClues(dto.getWordClues());
        puzzle.populateWordList();
        try {
            return buildingTools.createGrid(puzzle);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public CrosswordModel createCrossword(@RequestBody CrosswordSaveDto dto) {
        try {
            return dao.createCrossword(dto);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @PutMapping("")
    public CrosswordModel updateCrossword(@RequestBody CrosswordSaveDto dto) {
        try {
            return dao.updateCrossword(dto);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCrossword(@PathVariable int id) {
        try {
            dao.deleteCrossword(id);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }
}