package com.zupple.dao;

import com.zupple.dto.WordSearchSaveDto;
import com.zupple.model.WordSearchModel;

import java.util.List;

public interface WordSearchDao {
    List<WordSearchModel> getAll();
    List<WordSearchModel> getByUser(int userId);
    WordSearchModel getWordSearch(int id);
    WordSearchModel createWordSearch(WordSearchSaveDto wordSearch);
    WordSearchModel updateWordSearch(WordSearchSaveDto wordSearch);
    void deleteWordSearch(int id);
}
