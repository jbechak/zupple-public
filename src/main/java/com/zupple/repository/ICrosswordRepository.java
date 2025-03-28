package com.zupple.repository;

import com.zupple.dto.CrosswordSaveDto;
import com.zupple.model.CrosswordModel;
import java.util.List;

public interface ICrosswordRepository {
    List<CrosswordModel> getAll();
    List<CrosswordModel> getByUser(int userId);
    CrosswordModel getCrossword(int id);
    CrosswordModel createCrossword(CrosswordSaveDto crossword);
    CrosswordModel updateCrossword(CrosswordSaveDto crossword);
    void deleteCrossword(int id);
}
