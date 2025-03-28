package com.zupple.controller;

import com.zupple.businessObjects.ICrossword;
import com.zupple.repository.ICrosswordRepository;
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
    private ICrosswordRepository repository;

    @Autowired
    private ICrossword crosswordBO;

    @GetMapping()
    public List<CrosswordModel> getAll() {
        return repository.getAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<CrosswordModel> getByUser(@PathVariable int userId) {
        return repository.getByUser(userId);
    }

    @GetMapping("/{id}")
    public CrosswordModel getCrossword(@PathVariable int id) {
        return repository.getCrossword(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/generate")
    public CrosswordModel generateCrossword(@RequestBody CrosswordGenerateDto dto) {
        try {
            var model = crosswordBO.generateCrossword(dto);
            return model;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("generating"), e);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public CrosswordModel createCrossword(@RequestBody CrosswordSaveDto dto) {
        try {
            return repository.createCrossword(dto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("creating"), e);
        }
    }

    @PutMapping("")
    public CrosswordModel updateCrossword(@RequestBody CrosswordSaveDto dto) {
        try {
            return repository.updateCrossword(dto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("updating"), e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCrossword(@PathVariable int id) {
        try {
            repository.deleteCrossword(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("deleting"), e);
        }
    }

    private String getErrorMessage(String action) {
        return "An error occurred while " + action + " the crossword";
    }
}