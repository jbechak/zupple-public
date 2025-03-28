package com.zupple.businessObjects;

import com.zupple.dto.CrosswordGenerateDto;
import com.zupple.model.CrosswordModel;

public interface ICrossword {
    CrosswordModel generateCrossword(CrosswordGenerateDto dto);
}
