package com.zupple.dao;

import com.zupple.dto.CrosswordSaveDto;
import com.zupple.model.CrosswordModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JdbcCrosswordDao implements CrosswordDao {

    private final JdbcTemplate jdbcTemplate;
    private final String DOWN = "Down";
    private final String ACROSS = "Across";

    public JdbcCrosswordDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CrosswordModel> getAll() {
        List<CrosswordModel> crosswords = new ArrayList<>();

        String sql = "SELECT id, user_id, title, description, difficulty, width, height, " +
                "genre, instructions, grid_string " +
                "FROM crossword ORDER BY title;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while(results.next()) {
            CrosswordModel crossword = mapRowToCrossword(results);
            crossword = getWordClues(crossword);
            crosswords.add(crossword);
        }
        return crosswords;
    }

    @Override
    public List<CrosswordModel> getByUser(int userId) {
        List<CrosswordModel> crosswords = new ArrayList<>();

        String sql = "SELECT id, user_id, title, description, difficulty, width, height, " +
                "genre, instructions, grid_string " +
                "FROM crossword WHERE user_id = ? ORDER BY title;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while(results.next()) {
            CrosswordModel crossword = mapRowToCrossword(results);
            crossword = getWordClues(crossword);
            crosswords.add(crossword);
        }
        return crosswords;
    }

    @Override
    public CrosswordModel getCrossword(int id) {

        String sql = "SELECT id, user_id, title, description, difficulty, width, height, " +
                "genre, instructions, grid_string " +
                "FROM crossword WHERE id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

        if (results.next()) {
            CrosswordModel crossword = mapRowToCrossword(results);
            crossword = getWordClues(crossword);
            return crossword;
        }
        return null;
    }

    @Override
    public CrosswordModel createCrossword(CrosswordSaveDto crossword) {
        String sql = "INSERT INTO crossword (title, user_id, description, difficulty, " +
                "width, height, genre, instructions, grid_string)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

        Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
                crossword.getTitle(),
                crossword.getUserId(),
                crossword.getDescription(),
                crossword.getDifficulty(),
                crossword.getWidth(),
                crossword.getHeight(),
                crossword.getGenre(),
                crossword.getInstructions(),
                crossword.getGridString());

        createCrosswordWordClue(crossword.getWordClues(), crossword.getDownClueList(), DOWN, id);
        createCrosswordWordClue(crossword.getWordClues(), crossword.getAcrossClueList(), ACROSS, id);
        return getCrossword(id);
    }

    @Override
    public CrosswordModel updateCrossword(CrosswordSaveDto crossword) {
        String sql = "UPDATE crossword " +
            "SET title = ?, description = ?, difficulty = ?, " +
            "width = ?, height = ?, genre = ?, instructions = ?, grid_string = ? " +
            "WHERE id = ?;";

        jdbcTemplate.update(sql,
            crossword.getTitle(),
            crossword.getDescription(),
            crossword.getDifficulty(),
            crossword.getWidth(),
            crossword.getHeight(),
            crossword.getGenre(),
            crossword.getInstructions(),
            crossword.getGridString(),
            crossword.getId());

        var id = crossword.getId();
        updateCrosswordWordClue(crossword.getWordClues(), crossword.getDownClueList(), DOWN, id);
        updateCrosswordWordClue(crossword.getWordClues(), crossword.getAcrossClueList(), ACROSS, id);

        return getCrossword(id);
    }

    @Override
    public void deleteCrossword(int id) {
        String sql = "DELETE FROM crossword_wordclue WHERE crossword_id = ?;";
        jdbcTemplate.update(sql, id);

        sql = "DELETE FROM crossword WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    private CrosswordModel mapRowToCrossword(SqlRowSet results) {
        CrosswordModel crossword = new CrosswordModel(results.getString("title"));
        crossword.setId(results.getInt("id"));
        crossword.setUserId(results.getInt("user_id"));
        crossword.setDescription(results.getString("description"));
        crossword.setDifficulty(results.getString("difficulty"));
        crossword.setWidth(results.getInt("width"));
        crossword.setHeight(results.getInt("height"));
        crossword.setGenre(results.getString("genre"));
        crossword.setInstructions(results.getString("instructions"));
        crossword.setGridString(results.getString("grid_string"));

        return crossword;
    }

    private CrosswordModel getWordClues(CrosswordModel crossword) {
        List<String> downClueList = new ArrayList<>();
        List<String> acrossClueList = new ArrayList<>();
        Map<String, String> wordClues = new HashMap<>();

        int id = crossword.getId();

        String sql = "select clue_direction, clue_number, word, clue " +
                "from crossword_wordclue " +
                "where crossword_id = ? " +
                "order by clue_number;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

        while (results.next()) {
            String clue = results.getString("clue");
            if (results.getString("clue_direction").equals(DOWN)) {
                downClueList.add(results.getString("clue_number") + ". " + clue);
            }
            if (results.getString("clue_direction").equals(ACROSS)) {
                acrossClueList.add(results.getString("clue_number") + ". " + clue);
            }
            wordClues.put(results.getString("word"), clue);
        }

        crossword.setWordClues(wordClues);
        crossword.setDownClueList(downClueList);
        crossword.setAcrossClueList(acrossClueList);

        return crossword;
    }

    private void createCrosswordWordClue(Map<String, String> wordClues, List<String> clueList,
                                          String direction, int crosswordId) {
        for (String clue : clueList) {
            String[] clueArray = clue.split("\\.");
            int clueNumber = 0;
            try {
                clueNumber = Integer.parseInt(clueArray[0]);
            } catch (NumberFormatException e) {
                System.out.println("Number Format Exception");
            }
            String thisClue = clueArray[1].trim();
            for (Map.Entry<String, String> wordClue : wordClues.entrySet()) {
                if (wordClue.getValue().equals(thisClue)) {
                    String sql = "insert into crossword_wordclue (crossword_id, clue_direction, " +
                            "clue_number, word, clue) " +
                            "values (?, ?, ?, ?, ?);";
                    jdbcTemplate.update(sql, crosswordId, direction, clueNumber,
                            wordClue.getKey(), wordClue.getValue());
                    break;
                }
            }
        }
    }

    private void updateCrosswordWordClue(Map<String, String> wordClues, List<String> clueList,
                                         String direction, int crosswordId) {
        String sql = "delete from crossword_wordclue where crossword_id = ? AND clue_direction = ?;";
        jdbcTemplate.update(sql, crosswordId, direction);
        createCrosswordWordClue(wordClues, clueList, direction, crosswordId);
    }
}
