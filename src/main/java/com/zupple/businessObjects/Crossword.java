package com.zupple.businessObjects;

import com.zupple.dto.CrosswordGenerateDto;
import com.zupple.model.CrosswordModel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Crossword implements ICrossword {
    public record RawComponents(ArrayList<PlacedWord> placedWords, HashMap<String, Character> coordinateLookup) {}

    class PlacedWord {
        String word;
        int x;
        int y;
        boolean isGoingDown;
        public PlacedWord(int x, int y, boolean isGoingDown) {
            this.x = x;
            this.y = y;
            this.isGoingDown = isGoingDown;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }

    public CrosswordModel generateCrossword(CrosswordGenerateDto dto) {
        var rawComponents = getBestRawComponents(new ArrayList<>(dto.getWordClues().keySet()));
        return createModel(dto.getTitle(), rawComponents.placedWords, dto.getWordClues(), rawComponents.coordinateLookup);
    }

    public RawComponents getBestRawComponents(ArrayList<String> unplacedWords) {
        RawComponents alternateRawComponents = null;
        int highestWordCount = 0;

        /*
            If the first attempt yields a puzzle that includes all of the words, return it.
            If not, try again until either a puzzle is created that includes all words or the maximum number of attempts is met.
            Max number of attempts = total number of words
        */
        for (int i = 0; i < unplacedWords.size(); i++) {
            var rawComponents = mapWords(new ArrayList<>(unplacedWords));
            if (rawComponents.placedWords.size() == unplacedWords.size())
                return rawComponents;

            if (rawComponents.placedWords.size() > highestWordCount) {
                alternateRawComponents = rawComponents;
                highestWordCount = rawComponents.placedWords.size();
            }
        }

        return alternateRawComponents != null
            ? alternateRawComponents
            : new RawComponents(new ArrayList<>(),  new HashMap<>());
    }

    private RawComponents mapWords(ArrayList<String> unplacedWords) {
        var coordinateLookup = new HashMap<String, Character>();
        var charLookup = new HashMap<Character, List<String>>();
        var placedWords = new ArrayList<PlacedWord>();
        var wordsToRemove = new ArrayList<String>();
        boolean isGoingDown = false;
        int attemptsToMapStragglers = 0;
        int maxAttemptsForStragglers = 2;
        Collections.shuffle(unplacedWords);

        placeFirstWord(unplacedWords, placedWords, charLookup, coordinateLookup);

        do {
            boolean hasPlacedWords = false;
            for (var word : unplacedWords) {
                var trimmedWord = word.replaceAll("\\s", "");

                var placedWord = tryToPlaceWord(trimmedWord, isGoingDown, charLookup, coordinateLookup);
                if (placedWord != null) {
                    placedWord.setWord(trimmedWord);
                    putWordInMaps(placedWord, charLookup, coordinateLookup);
                    placedWords.add(placedWord);
                    wordsToRemove.add(word);
                    isGoingDown = !isGoingDown;
                    hasPlacedWords = true;
                }
            }
            for (var word : wordsToRemove) {
                unplacedWords.remove(word);
            }
            wordsToRemove.clear();

            if (unplacedWords.size() > 0) {
                isGoingDown = !isGoingDown;
                if (!hasPlacedWords)
                    attemptsToMapStragglers++;
            }
        } while (unplacedWords.size() > 0 && attemptsToMapStragglers < maxAttemptsForStragglers);
        return new RawComponents(placedWords, coordinateLookup);
    }

    private void placeFirstWord(
        List<String> unplacedWords,
        List<PlacedWord> placedWords,
        HashMap<Character, List<String>> charLookup,
        HashMap<String, Character> coordinateLookup
    ) {
        var word = unplacedWords.get(0);
        var trimmedWord = word.replaceAll("\\s", "");
        var placedWord = new PlacedWord(0, 0, true);
        placedWord.setWord(trimmedWord);
        putWordInMaps(placedWord, charLookup, coordinateLookup);
        placedWords.add(placedWord);
        unplacedWords.remove(word);
    }

    private void putWordInMaps(PlacedWord placedWord, HashMap<Character, List<String>> charLookup, HashMap<String, Character> coordinateLookup
    ) {
        var x = placedWord.x;
        var y = placedWord.y;
        for (int i = 0; i < placedWord.word.length(); i++) {
            //add to coordinateLookup
            var coordinateString = x + "," + y;
            var character = placedWord.word.charAt(i);
            if (!coordinateLookup.containsKey(coordinateString)) {
                coordinateLookup.put(coordinateString, character);
            }

            //add to char lookup
            if (charLookup.containsKey(character)) {
                charLookup.get(character).add(coordinateString);
            } else {
                charLookup.put(character, new ArrayList<>(List.of(coordinateString)));
            }

            if (placedWord.isGoingDown)
                y++;
            else
                x++;
        }
    }

    private PlacedWord tryToPlaceWord(String word, boolean isGoingDown, HashMap<Character, List<String>> charLookup, HashMap<String, Character> coordinateLookup) {
        var random = new Random();
        var initialI = random.nextInt(word.length());

        var placedWord = iterateOverWordPortion(initialI, word.length(), word, isGoingDown, charLookup, coordinateLookup);
        if (placedWord != null)
            return placedWord;

        return iterateOverWordPortion(0, initialI, word, isGoingDown, charLookup, coordinateLookup);
    }

    private PlacedWord iterateOverWordPortion(int lowIndex, int highIndex, String word, boolean isGoingDown, HashMap<Character, List<String>> charLookup, HashMap<String, Character> coordinateLookup) {
        for (int i = lowIndex; i < highIndex; i++) {
            var placedWord = placeUsingThisChar(word, i, isGoingDown, charLookup, coordinateLookup);
            if (placedWord != null)
                return placedWord;
        }
        return null;
    }

    private PlacedWord placeUsingThisChar(String word, int charIndex, boolean isGoingDown, HashMap<Character, List<String>> charLookup, HashMap<String, Character> coordinateLookup) {
        var random = new Random();
        var character = word.charAt(charIndex);
        if (charLookup.containsKey(character)) {
            //randomly pick a coordinate from the list, and try each from there
            var coordinateList = charLookup.get(character);
            var initialI = random.nextInt(coordinateList.size());

            var placedWord = placeInCoordinateListPortion(initialI, coordinateList.size(), word, charIndex, isGoingDown, coordinateList, coordinateLookup);
            if (placedWord != null)
                return placedWord;

            placedWord = placeInCoordinateListPortion(0, initialI, word, charIndex, isGoingDown, coordinateList, coordinateLookup);
            if (placedWord != null)
                return placedWord;
        }
        return null;
    }

    private PlacedWord placeInCoordinateListPortion(int lowIndex, int highIndex, String word, int charIndex, boolean isGoingDown, List<String> coordinateList, HashMap<String, Character> coordinateLookup) {
        for (int i = lowIndex; i < highIndex; i++) {
            String[] coordinateStringArr = coordinateList.get(i).split(",");
            int x = Integer.parseInt(coordinateStringArr[0]);
            int y = Integer.parseInt(coordinateStringArr[1]);
            var placedWord = wordFits(word, charIndex, x, y, isGoingDown, coordinateLookup);
            if (placedWord != null)
                return placedWord;
        }
        return null;
    }

    private PlacedWord wordFits(String word, int charIndex, int initialX, int initialY, boolean isGoingDown, HashMap<String, Character> coordinateLookup) {
        //check if clashing with another word going the same direction
        if ((isGoingDown && (isPreviousBlockOccupied(initialX, initialY, true, coordinateLookup) || isNextBlockOccupied(initialX, initialY, true, coordinateLookup))
            || (!isGoingDown && (isPreviousBlockOccupied(initialX, initialY, false, coordinateLookup) || isNextBlockOccupied(initialX, initialY, false, coordinateLookup))))
        ) {
            return null;
        }

        var x = isGoingDown ? initialX : initialX + 1;
        var y = isGoingDown ? initialY + 1 : initialY;

        boolean hasChar = true;
        boolean nextBlockHasChar = false;

        if (!wordPortionFits(charIndex + 1, word.length(), x, y, word, true, false, isGoingDown, coordinateLookup))
            return null;

        if (isGoingDown) y = initialY - charIndex;
        else x = initialX - charIndex;
        hasChar = isPreviousBlockOccupied(x, y, isGoingDown, coordinateLookup);
        nextBlockHasChar = coordinateLookup.containsKey(x + "," + y);

        if (!wordPortionFits(0, charIndex, x, y, word, hasChar, nextBlockHasChar, isGoingDown, coordinateLookup))
            return null;

        x = isGoingDown ? initialX : initialX - charIndex;
        y = isGoingDown ? initialY - charIndex : initialY;
        return new PlacedWord(x, y, isGoingDown);
    }

    private boolean wordPortionFits(int lowIndex, int highIndex, int x, int y, String word, boolean hasChar, boolean nextBlockHasChar, boolean isGoingDown, HashMap<String, Character> coordinateLookup) {
        boolean previousBlockHasChar;
        for (int i = lowIndex; i < highIndex; i++) {
            previousBlockHasChar = hasChar;
            hasChar = nextBlockHasChar;
            nextBlockHasChar = isNextBlockOccupied(x, y, isGoingDown, coordinateLookup);

            /* return false if:
                1. contains a letter different from the current
                2. is blank but perpendicular neighbor is not
                3. has 2 consecutive non-blanks
             */
            if ((hasChar && (coordinateLookup.get(x + "," + y) != word.charAt(i) || previousBlockHasChar || nextBlockHasChar))
                    || !hasChar
                    && ((isGoingDown && (coordinateLookup.containsKey((x - 1) + "," + y) || coordinateLookup.containsKey((x + 1) + "," + y)))
                    || (!isGoingDown && (coordinateLookup.containsKey(x + "," + (y - 1)) || coordinateLookup.containsKey(x + "," + (y + 1))))))
            {
                return false;
            }

            if (isGoingDown) y++;
            else x++;
        }
        return true;
    }

    private boolean isNextBlockOccupied(int x, int y, boolean isGoingDown, HashMap<String, Character> coordinateLookup) {
        return isGoingDown
            ? coordinateLookup.containsKey(x + "," + (y + 1))
            : coordinateLookup.containsKey((x + 1) + "," + y);
    }
    private boolean isPreviousBlockOccupied(int x, int y, boolean isGoingDown, HashMap<String, Character> coordinateLookup) {
        return isGoingDown
            ? coordinateLookup.containsKey(x + "," + (y - 1))
            : coordinateLookup.containsKey((x - 1) + "," + y);
    }

    private CrosswordModel createModel(
        String title,
        List<PlacedWord> placedWords,
        Map<String, String> wordClues,
        HashMap<String, Character> coordinateLookup
    ) {
        var unusedWordClues = new HashMap<>(wordClues);
        var usedWordClues = new HashMap<String, String>();
        var downClueList = new ArrayList<String>();
        var acrossClueList = new ArrayList<String>();

        //sort PlacedWords by y, then x
        placedWords.sort(Comparator.comparingInt((PlacedWord pw) -> pw.y).thenComparingInt(pw -> pw.x));
        int minX = 0; int maxX = 0; int maxY = 0; int clueNum = 1;
        int minY = placedWords.get(0).y;
        int previousX = placedWords.get(0).x;
        int previousY = placedWords.get(0).y;

        //calculate min & max values, width, and height, and create clue lists, update wordClue maps
        for (var placedWord : placedWords) {
            if (placedWord.x < minX) minX = placedWord.x;
            if (placedWord.y < minY) minY = placedWord.y;

            if (placedWord.x != previousX || placedWord.y != previousY)
                clueNum++;
            var clue = unusedWordClues.get(placedWord.word);

            if (placedWord.isGoingDown) {
                downClueList.add(clueNum + ". " + clue);
                int wordEndY = placedWord.y + placedWord.word.length() - 1;
                if (wordEndY > maxY) maxY = wordEndY;
            } else {
                acrossClueList.add(clueNum + ". " + clue);
                int wordEndX = placedWord.x + placedWord.word.length() - 1;
                if (wordEndX > maxX) maxX = wordEndX;
            }

            previousX = placedWord.x;
            previousY = placedWord.y;

            usedWordClues.put(placedWord.word, clue);
            unusedWordClues.remove(placedWord.word);
        }
        var width = maxX - minX + 1;
        var height = maxY - minY + 1;

        var gridString = getGridString(coordinateLookup, minX, maxX, minY, maxY);
        return new CrosswordModel(
            title,
            width,
            height,
            wordClues,
            usedWordClues,
            unusedWordClues,
            gridString,
            downClueList,
            acrossClueList
        );
    }

    private String getGridString(HashMap<String, Character> coordinateLookup, int minX, int maxX, int minY, int maxY) {
        String gridString = "";
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                var key = x + "," + y;
                gridString += coordinateLookup.containsKey(key)
                        ? coordinateLookup.get(key)
                        : '.';
            }
        }
        return gridString;
    }
}
