package com.zupple.repository;

import com.zupple.dto.WordSearchSaveDto;
import com.zupple.model.WordSearchModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class WordSearchRepository implements IWordSearchRepository {
    private final JdbcTemplate jdbcTemplate;
    public WordSearchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<WordSearchModel> getAll() {
        List<WordSearchModel> wordSearches = new ArrayList<>();

        String sql = "SELECT id, user_id, title, description, difficulty, show_difficulty, width, height, " +
                "genre, instructions, grid_string " +
                "FROM wordsearch ORDER BY title;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()) {
            wordSearches.add(mapRowToWordSearch(results));
        }
        return wordSearches;
    }

    @Override
    public List<WordSearchModel> getByUser(int userId) {
        List<WordSearchModel> wordSearches = new ArrayList<>();

        String sql = "SELECT id, user_id, title, description, difficulty, show_difficulty, width, height, " +
                "genre, instructions, grid_string " +
                "FROM wordsearch WHERE user_id = ? ORDER BY title;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,userId);

        while (results.next()) {
            wordSearches.add(mapRowToWordSearch(results));
        }
        return wordSearches;
    }

    @Override
    public WordSearchModel getWordSearch(int id) {
        String sql = "SELECT id, user_id, title, description, difficulty, show_difficulty, width, height, " +
                "genre, instructions, grid_string " +
                "FROM wordsearch WHERE id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

        if (results.next()) {
            return mapRowToWordSearch(results);
        }
        return null;
    }

    @Override
    public WordSearchModel createWordSearch(WordSearchSaveDto wordSearch) {
        String sql = "INSERT INTO wordsearch (user_id, title, description, difficulty, " +
            "show_difficulty, width, height, genre, instructions, grid_string)" +
            "VALUES (?, ?, ?, ?,  ?, ?, ?,  ?, ?, ?) RETURNING id;";

        Integer id = jdbcTemplate.queryForObject(sql, Integer.class,
            wordSearch.getUserId(),
            wordSearch.getTitle(),
            wordSearch.getDescription(),
            wordSearch.getDifficulty(),
            wordSearch.getShowDifficulty(),
            wordSearch.getWidth(),
            wordSearch.getHeight(),
            wordSearch.getGenre(),
            wordSearch.getInstructions(),
            wordSearch.getGridString());

        //createWordCollection(id, wordSearch.getWordCollection());
        createWordCollection(id, wordSearch.getUsedWords(), wordSearch.getUnusedWords());

        return getWordSearch(id);
    }

    @Override
    public WordSearchModel updateWordSearch(WordSearchSaveDto wordSearch) {
        String sql = "UPDATE wordsearch " +
                "SET title = ?, description = ?, difficulty = ?, show_difficulty = ?, " +
                "width = ?, height = ?, genre = ?, instructions = ?, grid_string = ? " +
                "WHERE id = ?;";

        jdbcTemplate.update(sql,
                wordSearch.getTitle(),
                wordSearch.getDescription(),
                wordSearch.getDifficulty(),
                wordSearch.getShowDifficulty(),
                wordSearch.getWidth(),
                wordSearch.getHeight(),
                wordSearch.getGenre(),
                wordSearch.getInstructions(),
                wordSearch.getGridString(),
                wordSearch.getId());

        var id = wordSearch.getId();
        //updateWordCollection(id, wordSearch.getWordCollection());
        updateWordCollection(id, wordSearch.getUsedWords(), wordSearch.getUnusedWords());

        return getWordSearch(id);
    }

    public void deleteWordSearch(int id) {
        String sql = "DELETE FROM wordsearch_word WHERE wordsearch_id = ?;";
        jdbcTemplate.update(sql, id);

        sql = "DELETE FROM wordsearch WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    private WordSearchModel mapRowToWordSearch(SqlRowSet results) {
        var wordSearch = new WordSearchModel(results.getString("title"));
        wordSearch.setId(results.getInt("id"));
        wordSearch.setUserId(results.getInt("user_id"));
        wordSearch.setDescription(results.getString("description"));
        wordSearch.setDifficulty(results.getString("difficulty"));
        wordSearch.setShowDifficulty(results.getBoolean("show_difficulty"));
        wordSearch.setWidth(results.getInt("width"));
        wordSearch.setHeight(results.getInt("height"));
        wordSearch.setGenre(results.getString("genre"));
        wordSearch.setInstructions(results.getString("instructions"));
        wordSearch.setGridString(results.getString("grid_string"));
        wordSearch.setUsedWords(getWords(wordSearch.getId(), true));
        wordSearch.setUnusedWords(getWords(wordSearch.getId(), false));

        wordSearch.setWordCollection(Stream
            .concat(wordSearch.getUsedWords().stream(), wordSearch.getUnusedWords().stream())
            .collect(Collectors.toList()));
        return wordSearch;
    }

    private List<String> getWords(int wordSearchId, boolean isActive) {
        List<String> words = new ArrayList<>();
        String sql = "SELECT word FROM wordsearch_word WHERE wordsearch_id = ? AND is_active = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, wordSearchId, isActive);

        while (results.next()) {
            words.add(results.getString("word"));
        }
        return words;
    }

    private void createWordCollection(int wordSearchId, List<String> usedWords, List<String> unusedWords) {
        for (String word : usedWords) {
            String sql = "INSERT INTO wordsearch_word (wordsearch_id, word, is_active) " +
                    "VALUES (?, ?, ?);";
            jdbcTemplate.update(sql, wordSearchId, word, true);
        }
        for (String word : unusedWords) {
            String sql = "INSERT INTO wordsearch_word (wordsearch_id, word, is_active) " +
                    "VALUES (?, ?, ?);";
            jdbcTemplate.update(sql, wordSearchId, word, false);
        }
    }

    private void updateWordCollection(int wordSearchId, List<String> usedWords, List<String> unusedWords) {
        String sql = "DELETE FROM wordsearch_word WHERE wordsearch_id = ?;";
        jdbcTemplate.update(sql, wordSearchId);
        createWordCollection(wordSearchId, usedWords, unusedWords);
    }
}


