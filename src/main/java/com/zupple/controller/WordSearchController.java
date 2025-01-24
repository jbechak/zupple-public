package com.zupple.controller;

import com.zupple.dao.WordSearchDao;
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
    private WordSearchDao dao;
    private BuildingTools buildingTools = new BuildingTools();

    @GetMapping()
    public List<WordSearchModel> getAll() {
        return dao.getAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<WordSearchModel> getByUser(@PathVariable int userId) {
        return dao.getByUser(userId);
    }

    @GetMapping("/{id}")
    public WordSearchModel getWordsearch(@PathVariable int id) {
        return dao.getWordSearch(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/generate")
    public WordSearchModel generateWordsearch(@RequestBody WordSearchGenerateDto dto) {
        WordSearchModel wordSearch = new WordSearchModel(dto.getTitle());
        wordSearch.setWidth(dto.getWidth());
        wordSearch.setHeight(dto.getHeight());
        wordSearch.setWordDirections(dto.getWordDirections());
        wordSearch.setWordCollection(dto.getWordCollection());
        try {
            buildingTools.createWordSearch(wordSearch);
            return wordSearch;
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public WordSearchModel createWordsearch(@RequestBody WordSearchSaveDto dto) {
        try {
            return dao.createWordSearch(dto);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @PutMapping("")
    public WordSearchModel updateWordsearch(@RequestBody WordSearchSaveDto dto) {
        try {
            return dao.updateWordSearch(dto);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteWordsearch(@PathVariable int id) {
        try {
            dao.deleteWordSearch(id);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
        }
    }
}
