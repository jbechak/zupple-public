package com.zupple.businessObjects;

import com.zupple.dto.WordSearchGenerateDto;
import com.zupple.model.WordSearchModel;

public interface IWordSearch {
    WordSearchModel generateWordSearch(WordSearchGenerateDto dto);
}
