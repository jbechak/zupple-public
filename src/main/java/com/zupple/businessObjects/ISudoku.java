package com.zupple.businessObjects;

import com.zupple.dto.SudokoGenerateDto;
import com.zupple.model.SudokuModel;

public interface ISudoku {
    SudokuModel generateSudoku(SudokoGenerateDto dto);
}
