package com.zupple.businessObjects;

import com.zupple.dto.WordSearchGenerateDto;
import com.zupple.model.WordSearchModel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordSearch implements IWordSearch {

    public record DirectionSpecs(boolean isGoingAcross, boolean isGoingDown, boolean isGoingUp, int currentDirection) {}
    private final String[] instructionArray = {
            "",
            "Look through the word search puzzle and find all of the words from the word list.\n " +
                    "The words can be found going from left to right or from top to bottom.",

            "Look through the word search puzzle and find all of the words from the word list.\n " +
                    "The words can be found going from left to right, top to bottom, or diagonally.",

            "Look through the word search puzzle and find all of the words from the word list.\n " +
                    "The words can be found going from left to right, top to bottom, diagonally, or\n" +
                    "backwards in any of these directions."
    };

    public WordSearchModel generateWordSearch(WordSearchGenerateDto dto) {
        var coordinateLookup = new HashMap<String, Character>();
        var placedWords = new ArrayList<String>();

        //var unplacedWords = dto.getWordCollection();
        var unplacedWords = new ArrayList<>(dto.getWordCollection());
        var width = dto.getWidth();
        var height = dto.getHeight();
        var wordDirections = dto.getWordDirections();
        boolean useDiagonal = wordDirections > 1;

        int charactersAdded = mapWords(unplacedWords, placedWords, width, height, coordinateLookup, useDiagonal);

        var model = new WordSearchModel(
            dto.getTitle(),
            width,
            height,
            wordDirections,
            placedWords,
            unplacedWords,
            getGridString(coordinateLookup, width, height),
            getDifficulty(width, height, charactersAdded, placedWords.size(), wordDirections),
            dto.getShowDifficulty(),
            instructionArray[wordDirections],
            dto.getWordCollection()
        );
        return model;
    }

    private int mapWords(List<String> unplacedWords, List<String> placedWords, int width, int height, HashMap<String, Character> coordinateLookup, boolean useDiagonal) {
        Collections.shuffle(unplacedWords);
        var wordsToRemove = new ArrayList<String>();
        var directionSpecs = new DirectionSpecs(true, false, false, 0);
        int charactersAdded = 0;
        int attemptsToMapStragglers = 0;
        int maxAttemptsForStragglers = useDiagonal ? 4 : 2;

        do {
            boolean hasPlacedWords = false;
            for (var word : unplacedWords) {
                var trimmedWord = word.replaceAll("\\s", "");
                int newCharactersAdded = tryToPlaceWord(trimmedWord, directionSpecs, width, height, coordinateLookup);
                if (newCharactersAdded != -1) {
                    placedWords.add(word);
                    wordsToRemove.add(word);
                    directionSpecs = getUpdatedDirectionSpecs(directionSpecs, useDiagonal);
                    charactersAdded += newCharactersAdded;
                    hasPlacedWords = true;
                }
            }
            for (var word : wordsToRemove) {
                unplacedWords.remove(word);
            }
            wordsToRemove.clear();

            if (unplacedWords.size() > 0) {
                directionSpecs = getUpdatedDirectionSpecs(directionSpecs, useDiagonal);
                if (!hasPlacedWords)
                    attemptsToMapStragglers++;
            }
        } while (unplacedWords.size() > 0 && attemptsToMapStragglers < maxAttemptsForStragglers);
        return charactersAdded;
    }

    private DirectionSpecs getUpdatedDirectionSpecs(DirectionSpecs directionSpecs, boolean useDiagonal) {
        if (useDiagonal) {
            int currentDirection = directionSpecs.currentDirection == 3 ? 0 : directionSpecs.currentDirection + 1;
            switch (currentDirection) {
                case 0:
                    return new DirectionSpecs(true, false, false, currentDirection);
                case 1:
                    return new DirectionSpecs(false, true, false, currentDirection);
                case 2:
                    return new DirectionSpecs(true, true, false, currentDirection);
                case 3:
                    return new DirectionSpecs(true, false, true, currentDirection);
            }
        }
        return new DirectionSpecs(!directionSpecs.isGoingAcross, !directionSpecs.isGoingDown, false, -1);
    }

    private int tryToPlaceWord(String word, DirectionSpecs directionSpecs, int width, int height, HashMap<String, Character> coordinateLookup) {
        var random = new Random();
        var maxX = directionSpecs.isGoingAcross ? width - word.length() : width;
        var maxY = directionSpecs.isGoingDown ? height - word.length() : height;
        var minY = directionSpecs.isGoingUp ? word.length() - 1 : 0;
        var initialX = maxX == 0 ? 0 : random.nextInt(maxX);

        int initialY;
        if (directionSpecs.isGoingUp)
            initialY = random.nextInt(height - word.length()) + word.length();
        else
            initialY = maxY == 0 ? 0 : random.nextInt(maxY);

        //loop through remaining places
        for (int x = initialX; x < maxX; x++) {
            for (int y = initialY; y < maxY; y++) {
                if (wordFits(word, x, y, directionSpecs, coordinateLookup)) {
                    return putWordInMap(word, x, y, directionSpecs, coordinateLookup);
                }
            }
        }
        //loop through first places
        for (int x = 0; x < initialX; x++) {
            for (int y = minY; y < initialY; y++) {
                if (wordFits(word, x, y, directionSpecs, coordinateLookup)) {
                    return putWordInMap(word, x, y, directionSpecs, coordinateLookup);
                }
            }
        }
        return -1;
    }

    private boolean wordFits(String word, int initialX, int initialY, DirectionSpecs directionSpecs, HashMap<String, Character> coordinateLookup) {
        var x = initialX;
        var y = initialY;
        for (int i = 0; i < word.length(); i++) {
            var key = x + "," + y;
            if (coordinateLookup.containsKey(key) && coordinateLookup.get(key) != word.charAt(i)) {
                return false;
            }
            if (directionSpecs.isGoingAcross) x++;
            if (directionSpecs.isGoingDown) y++;
            if (directionSpecs.isGoingUp) y--;
        }
        return true;
    }

    private int putWordInMap(String word, int initialX, int initialY, DirectionSpecs directionSpecs, HashMap<String, Character> coordinateLookup) {
        var x = initialX;
        var y = initialY;
        int charactersAdded = 0;
        for (int i = 0; i < word.length(); i++) {
            var key = x + "," + y;
            if (!coordinateLookup.containsKey(key)) {
                coordinateLookup.put(key, word.charAt(i));
                charactersAdded++;
            }
            if (directionSpecs.isGoingAcross) x++;
            if (directionSpecs.isGoingDown) y++;
            if (directionSpecs.isGoingUp) y--;
        }
        return charactersAdded;
    }

    private String getGridString(HashMap<String, Character> coordinateLookup, int width, int height) {
        var random = new Random();
        String gridString = "";
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                var key = x + "," + y;
                gridString += coordinateLookup.containsKey(key)
                    ? coordinateLookup.get(key)
                    : (char)(random.nextInt(26) + 'A');
            }
        }
        return gridString;
    }

    public String getDifficulty(int width, int height, int charactersAdded, int wordCount, int wordDirections) {
        int difficultyCredits = getDifficultyCredits(width, height, charactersAdded, wordCount, wordDirections);

        if (difficultyCredits >= 8)
            return "Level 5 - Very Difficult";

        if (difficultyCredits >= 6)
            return "Level 4 - Difficult";

        if (difficultyCredits >= 3)
            return "Level 3 - Medium";

        if (difficultyCredits >= 1)
            return "Level 2 - Easy";

        return "Level 1 - Very Easy";
    }

    private int getDifficultyCredits(int width, int height, int charactersAdded, int wordCount, int wordDirections) {
        int credits = 0;
        int remainingSpaces = width * height - charactersAdded;
        credits += remainingSpaces / 50;
        credits += wordDirections * 2 - 2;
        credits += (wordCount - 1) / 10;
        return credits;
    }
}
