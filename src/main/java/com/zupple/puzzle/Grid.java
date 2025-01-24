package com.zupple.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
    private List<List> grid = new ArrayList<>();
    private int height;
    private int width;
    private int usedSpaces = 0;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;

        for (int i = 0; i < height; i++) {
            List<String> line = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                line.add(".");
            }
            grid.add(line);
        }
    }

    public void updateUsedSpaces(int newSpaces) {
        usedSpaces += newSpaces;
    }
    public int totalSpaces() {
        return height * width;
    }
    public int remainingSpaces() {
        return totalSpaces() - usedSpaces;
    }

    @Override
    public String toString() {
        String gridString = "";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gridString += (getBlock(j, i));
            }
        }
        return gridString;
    }

    public void setBlock(int x, int y, String letter) {
        List<String> line = getRow(y);
        line.set(x, letter);
    }

    public String getBlock(int x, int y) {
        List<String> line = getRow(y);
        return line.get(x);
    }

    public List<String> getRow(int y) {
        return grid.get(y);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void fillWithRandomLetters() {
        Random generate = new Random();
        for (int i = 0; i < width; i ++) {
            for (int j = 0; j < height; j++) {
                if (getBlock(i, j).equals(".")) {
                    int num = generate.nextInt(26);
                    String letter = String.valueOf((char)(num + 65));
                    setBlock(i, j, letter);
                }
            }
        }
    }
}