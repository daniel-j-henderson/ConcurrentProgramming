# Parallel Sudoku Solver

##Daniel Henderson and Austin Alberts

####Concurrent Programming Final Project


The 'Test' class can be run thusly "java Test <puzzle>" where <puzzle> is the name of a file in the style of PuzzleA and PuzzleB in the Puzzles directory. It will solve the puzzle on 1, 2, 4, and 10 threads. To turn off/on the humanistic algorithms, edit the "hum" boolean variable in the main function of Test.java and recompile. 

The 'TestSerial' class can be run in the same way, and will solve the puzzle serially for a comparison object. Edit the "USE_HUMANISTIC" boolean variable in the main function to turn off/on the humaniztic algorithms and recompile. 

Re-compile using 'javac *.java'
