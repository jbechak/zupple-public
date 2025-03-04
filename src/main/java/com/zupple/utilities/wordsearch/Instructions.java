package com.zupple.utilities.wordsearch;

public class Instructions {
    private final String[] standard = {
        "",
        "Look through the word search puzzle and find all of the words from the word list.\n " +
        "The words can be found going from left to right or from top to bottom.",

        "Look through the word search puzzle and find all of the words from the word list.\n " +
        "The words can be found going from left to right, top to bottom, or diagonally.",

        "Look through the word search puzzle and find all of the words from the word list.\n " +
        "The words can be found going from left to right, top to bottom, diagonally, or\n" +
        "backwards in any of these directions."
    };
    public String getStandard(int index) {
        return this.standard[index];
    }
}
