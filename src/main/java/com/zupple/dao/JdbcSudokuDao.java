package com.zupple.dao;

import com.zupple.dto.SudokuSaveDto;
import com.zupple.model.SudokuModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcSudokuDao implements SudokuDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcSudokuDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SudokuModel> getAll() {
        List<SudokuModel> sudokuList = new ArrayList<>();

        String sql = "SELECT id, user_id, title, \"showTitle\", difficulty, show_difficulty, instructions, grid_string " +
                "FROM sudoku ORDER BY title;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()) {
            sudokuList.add(mapRowToSudoku(results));
        }
        return sudokuList;
    }

    @Override
    public List<SudokuModel> getByUser(int userId) {
        List<SudokuModel> sudokuList = new ArrayList<>();

        String sql = "SELECT id, user_id, title, show_title, difficulty, show_difficulty, instructions, grid_string " +
                "FROM sudoku WHERE user_id = ? ORDER BY title;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            sudokuList.add(mapRowToSudoku(results));
        }
        return sudokuList;
    }

    @Override
    public SudokuModel getSudoku(int id) {
        String sql = "SELECT id, user_id, title, show_title, difficulty, show_difficulty, instructions, grid_string " +
                "FROM sudoku WHERE id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

        if (results.next()) {
            return mapRowToSudoku(results);
        }
        return null;
    }

    @Override
    public SudokuModel createSudoku(SudokuSaveDto sudoku) {
        String sql = "insert into sudoku (title, user_id, show_title, difficulty, " +
                "show_difficulty, instructions, grid_string)" +
                "values (?, ?, ?, ?, ?, ?, ?) returning id;";

        Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                sudoku.getTitle(),
                sudoku.getUserId(),
                sudoku.getShowTitle(),
                sudoku.getDifficulty(),
                sudoku.getShowDifficulty(),
                sudoku.getInstructions(),
                sudoku.getGridString());

        return getSudoku(id);
    }

    @Override
    public SudokuModel updateSudoku(SudokuSaveDto sudoku) {
        String sql = "update sudoku " +
            "set title = ?, show_title = ?, difficulty = ?, show_difficulty = ?, instructions = ?, grid_string = ? " +
            "where id = ?;";

        jdbcTemplate.update(sql,
            sudoku.getTitle(),
            sudoku.getShowTitle(),
            sudoku.getDifficulty(),
            sudoku.getShowDifficulty(),
            sudoku.getInstructions(),
            sudoku.getGridString(),
            sudoku.getId());

        return getSudoku(sudoku.getId());
    }

    @Override
    public void deleteSudoku(int id) {
        String sql = "DELETE FROM sudoku WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    private SudokuModel mapRowToSudoku(SqlRowSet result) {
        SudokuModel sudoku = new SudokuModel();
        sudoku.setId(result.getInt("id"));
        sudoku.setUserId(result.getInt("user_id"));
        sudoku.setTitle(result.getString("title"));
        sudoku.setShowTitle(result.getBoolean("show_title"));
        sudoku.setDifficulty(result.getInt("difficulty"));
        sudoku.setShowDifficulty(result.getBoolean("show_difficulty"));
        sudoku.setInstructions(result.getString("instructions"));
        sudoku.setGridString(result.getString("grid_string"));
        return sudoku;
    }
}
