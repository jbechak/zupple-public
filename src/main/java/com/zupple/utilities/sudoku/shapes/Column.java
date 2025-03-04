package com.zupple.utilities.sudoku.shapes;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private List<Block> blockColumn = new ArrayList<>();
    private int columnIndex;

    public Column(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public void addBlock(Block block) {
        blockColumn.add(block);
    }

    public int getBlockValue(int index) {
        return blockColumn.get(index).getValue();
    }

    public void setBlockValue(int index, int value) {
        blockColumn.get(index).setValue(value);
    }
}