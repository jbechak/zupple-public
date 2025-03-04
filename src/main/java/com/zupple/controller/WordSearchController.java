package com.zupple.controller;

import com.zupple.repository.IWordSearchRepository;
import com.zupple.dto.WordSearchGenerateDto;
import com.zupple.dto.WordSearchSaveDto;
import com.zupple.utilities.wordsearch.BuildingTools;
import com.zupple.model.WordSearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/wordsearch")
public class WordSearchController {

    @Autowired
    private IWordSearchRepository repository;
    private BuildingTools buildingTools = new BuildingTools();

    @GetMapping()
    public List<WordSearchModel> getAll() {
        return repository.getAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<WordSearchModel> getByUser(@PathVariable int userId) {
        return repository.getByUser(userId);
    }

    @GetMapping("/{id}")
    public WordSearchModel getWordsearch(@PathVariable int id) {
        return repository.getWordSearch(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/generate")
    public WordSearchModel generateWordsearch(@RequestBody WordSearchGenerateDto dto) {
        var wordSearch = new WordSearchModel(dto.getTitle());
        wordSearch.setWidth(dto.getWidth());
        wordSearch.setHeight(dto.getHeight());
        wordSearch.setWordDirections(dto.getWordDirections());
        wordSearch.setWordCollection(dto.getWordCollection());
        wordSearch.setShowDifficulty(dto.getShowDifficulty());
        try {
            buildingTools.createWordSearch(wordSearch);
            return wordSearch;
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("generating"), e);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public WordSearchModel createWordsearch(@RequestBody WordSearchSaveDto dto) {
        try {
            return repository.createWordSearch(dto);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("creating"), e);
        }
    }

    @PutMapping("")
    public WordSearchModel updateWordsearch(@RequestBody WordSearchSaveDto dto) {
        try {
            return repository.updateWordSearch(dto);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("updating"), e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteWordsearch(@PathVariable int id) {
        try {
            repository.deleteWordSearch(id);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorMessage("deleting"), e);
        }
    }

    private String getErrorMessage(String action) {
        return "An error occurred while " + action + " the wordsearch";
    }
}
