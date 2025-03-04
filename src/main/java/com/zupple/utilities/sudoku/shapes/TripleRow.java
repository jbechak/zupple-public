package com.zupple.utilities.sudoku.shapes;

import java.util.ArrayList;
import java.util.List;

public class TripleRow {
    List<Row> tripleRow = new ArrayList<>();
    int topRow;

    public TripleRow(int topRow) {
        this.topRow = topRow;
    }

    @Override
    public String toString() {
        String row = "";
        for (int i = 0; i < 3; i++) {
            row += getRow(i);
            if (i < 2) {
                row += line();
            }
        }
        return row;
    }

    public String line() {
        String line = "";
        for (int i = 0; i < 9; i++) {
            line += "----";
            if (i == 8) {
                line += "-----\n";
            }
        }
        return line;
    }
    public Row getRow(int y) {
        return tripleRow.get(y);
    }

    public void addRow(Row row) {
        tripleRow.add(row);
    }
}