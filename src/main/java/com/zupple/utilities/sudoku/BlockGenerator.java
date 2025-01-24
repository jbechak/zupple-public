package com.zupple.utilities.sudoku;

import com.zupple.utilities.sudoku.shapes.Row;
import com.zupple.utilities.sudoku.shapes.TripleRow;

public class BlockGenerator {
    Board board = new Board();
    int blanks;

    public String createBoard(int difficulty) {
        blanks = difficulty * 6;

        initialBoardSetup();
        populateRows();
        board.putAllBlocksInList();
        String gridString = board.toString();

        board.setLevel(difficulty);
        board.createBlanks(blanks);

        gridString = board.toString() + "\n" + gridString;
        return gridString;
    }

    public void createBoard() {
        initialBoardSetup();
        populateRows();
        board.putAllBlocksInList();
    }

    public void initialBoardSetup() {
        //Row.populateListOf9();
        putRowsInBoard();
        putTripleRowsInBoard();

        board.createAllColumns();
        board.createAllSquares();
        board.updateColumns(0);
        board.updateSquares(0);
    }

    public void putRowsInBoard() {
        for (int i = 0; i < 9; i++) {
            Row row = new Row(i);
            board.addRow(row);
        }
    }

    public void putTripleRowsInBoard() {
        //is this needed?
        for (int i = 0, j = 0 ; i < 7; i += 3, j++) {
            TripleRow tripleRow = new TripleRow(i);
            tripleRow.addRow(board.getRow(i));
            tripleRow.addRow(board.getRow(i + 1));
            tripleRow.addRow(board.getRow(i + 2));
            board.addTripleRow(tripleRow);
        }
    }

    public void populateRows() {
        boolean success = false;

        do {
            do {
//                success = create0to2();
                success = createRows(1, 2);
            }
            while (!success);

            do {
//                success = create3to5();
                success = createRows(3, 5);
            } while (!success);

            for (int i = 0; i < 100; i++) {
//                success = create6to7();
                success = createRows(6, 7);
                if (success) {
                    break;
                }
            }
        } while (!success);
        board.getRow(8).alternateRow9Builder(board);
    }

    public boolean createRows( int startingRow, int EndingRow) {
        for (int i = startingRow; i <= EndingRow; i++) {
            if (!board.getRow(i).shuffleRowUntilGood(board)) {
                return false;
            }
            board.updateColumns(i);
            board.updateSquares(i);
        }
        return true;
    }

//    public boolean create0to2() {
//        for (int i = 1; i <= 2; i++) {
//            if (!board.getRow(i).shuffleRowUntilGood(board)) {
//                return false;
//            }
//            board.updateColumns(i);
//            board.updateSquares(i);
//        }
//        return true;
//    }
//
//    public boolean create3to5() {
//        for (int i = 3; i <= 5; i++) {
//            if (!board.getRow(i).shuffleRowUntilGood(board)) {
//                return false;
//            }
//            board.updateColumns(i);
//            board.updateSquares(i);
//        }
//        return true;
//    }
//
//    public boolean create6to7() {
//        for (int i = 6; i <= 7; i++) {
//            if (!board.getRow(i).shuffleRowUntilGood(board)) {
//                return false;
//            }
//            board.updateColumns(i);
//            board.updateSquares(i);
//        }
//        return true;
//    }
}
