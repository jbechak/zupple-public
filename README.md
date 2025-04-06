# Zupple
## Deployed application link
https://zupple-app.netlify.app/

## Background
As a father, and as somebody who has previously spent a lot of time teaching children, I thought that an application that can quickly generate a wordsearch puzzle from a list of words would be a helpful learning tool. So I set out to develop the wordsearch generator, and as soon as I got it working in its most basic form, I was printing off puzzles for my daughter to use to reinforce her new vocabulary. Then, having just developed an algorithm that relies on matching letters in a matrix, I decided to try to expand it to create crossword puzzles, and after that, Sudoku puzzles. These three puzzle generators, which now make up Zupple, have all served as wonderful learning tools around the house, and I believe they would be helpful for teachers as well.

I designed the backend for this application when I was first learning object oriented programming. It was a great opportunity to try to formulate my own algorithms to create different types of puzzles. Most recently, I've been working on a front end, so users can have access to all of the features. I hope you enjoy!

## User Instructions
Zupple has 3 modes: Wordsearch, Crossword, and Sudoku. In each mode, users can generate puzzles and view them. Logged in users can save and print puzzles as well. To get started in any of the 3 modes, optionally log in, and then click the mode from the menu bar in the top left area of the browser window. All 3 modes have 3 tabs: Puzzle List, Edit, and View/Save.

**Puzzle List tab** - From the Puzzle List tab, click the 'Create New Puzzle' button to create a new puzzle. If you have any saved puzzles, click on the edit, view, or delete icons for that puzzle to take any of these options.

**Edit tab** - Fill out all of the required fields. Clicking the 'Generate Puzzle' button will generate the puzzle and open the 'View/Save' tab.

**View/Save tab** - Here you can view, save, or print your puzzle. 

For Wordsearch puzzles, if certain words didn't make it into the generated puzzle, it will be indicated in the View/Save tab. When this happens, go to the Edit tab. Here are 3 things you can do to help ensure that all of your words fit into the puzzle:
1. Increase the width and/or height
2. Remove words from the word list
3. Increase the possible word directions

For Crossword puzzles, if certain words didn't make it into the generated puzzle, it will be indicated in the View/Save tab. This will happen if there is no possible way to include all of the words together in the puzzle due to a lack of common letters. For example, if there are 2 words in the word/clue list: "PIANO" and "BLUE", there is no way to create a crossword puzzle with only these 2 words, since they share no common letters. When this happens, go to the Edit tab. Here are 3 things you can do in this situation to help ensure that all of your words fit into the puzzle:
1. Add a connecting word. "PIANO" and "BLUE" have no common letters. However, the word "GREEN" shares an "N" with "PIANO" and an "E" with "BLUE". In this situation, if you add the word "GREEN", the puzzle will generate with all 3 of these words, rather than 1 of the original 2 words.
2. Modify the words, "PIANO" and "BLUE" have no common letters, but "PIANOS" and "BLUES" do!
3. Remove one of the words.
