package com.zupple.utilities.crossword;

import com.zupple.puzzleParts.Word;
import java.util.Comparator;

public class SortByStartingY implements Comparator<Word> {

    @Override
    public int compare(Word a, Word b)
    {
        return a.getStartingY() - b.getStartingY();
    }
}
