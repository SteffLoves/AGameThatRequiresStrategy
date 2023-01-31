/* Stephanie Morales
   Dr. Steinberg
   COP3503 Fall 2022
   Programming Assignment 1
*/


import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;


public class Game {

    private int[][] board;
    //2D integer array that symbolizes the board.
    private char[] computerMoves;
    //1D char array that will store computerize moves.


    public Game(int size, String filename) {
        //Constructor with two parameters

        this.board = new int[size][size];

        //read moves
        try {
            this.readMoves(filename);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }


    //Scan the files of moves and store them
    public void readMoves(String filename) {
        File file = new File(filename);
        if (file.exists()) {

            //Get the size of the array and hold moves
            int size = 0;
            Path path = Path.of(filename);

            try (Stream<String> stream = Files.lines(path, Charset.defaultCharset())) {
                size = (int) stream.count();
                this.computerMoves = new char[size];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //Read all lines to the array.
            try {
                int i = 0;
                for (String str : Files.readAllLines(path)) {
                    this.computerMoves[i++] = str.trim().charAt(0);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                throw new FileNotFoundException(filename.concat(" does not exist"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //Play a round of the game
    public int play(int player) {

        //Check if the moves are read from text file.
        if (this.computerMoves == null) {
            System.err.println("Unable to read moves");
            System.exit(0);
        }
        //Keep count
        int i = 0,
                j = 0,
                k = 0,
                gridSize = 8;
        boolean isComputerMove = false;

        //Make the moves:
        while (true) {
            //Mark the location of the knight
            this.board[i][j] = i + j;
            //Computer move.
            char ch = this.computerMoves[k];
            //Player moves
            if (player == 2 && (i == 0 && j == 0)) {
                if (ch == 'd') {
                    j += 1;
                    i += 1;
                } else if (ch == 'r') {
                    j += 1;
                } else if (ch == 'b') {
                    i += 1;
                }
                k += 1;
            }else{
                //Check if P1 is making the next move, P2 is the computer
                if (!isComputerMove) {
                    //If P1 needs to win, make it have the last move
                    if (player==1) {
                        //Don't let P2 make the last move
                        if (((i + 1) == gridSize - 1 && (j + 1) == gridSize - 1)) {
                            i += 1;
                        }
                    }
                    else{
                        //If P2 needs to win
                        //Don't let P1 make the last move, change their movement to right or diagonal
                        if ((i + 1) == 6 && (j + 1) == 7) {
                            i += 1;
                        }else {
                            //Normal diagonal moves for P1
                            if ((j + 1) < gridSize) {
                                j += 1;
                            }
                            if ((i + 1) < gridSize) {
                                i += 1;
                            }
                        }
                    }
                    //P1 is done moving, time for P2's move
                    isComputerMove = true;
                }else {
                    //P2 normal moves
                    if (ch == 'd') {
                        if ((j + 1) < gridSize) {
                            j += 1;
                        }
                        if ((i + 1) < gridSize) {
                            i += 1;
                        }
                    } else if (ch == 'r') {
                        if ((j + 1) < gridSize) {
                            j += 1;
                        }
                    } else if (ch == 'b') {
                        if ((i + 1) < gridSize) {
                            i += 1;
                        }
                    }
                    //Move is done by P2 and time for P1 to move
                    isComputerMove = false;
                    k += 1;
                }
            }
            //End game once the knight is on the last piece
            if (i == gridSize - 1 && j == gridSize - 1) {
                break;
            }
        }

        //If the last move was done by computer:
        if (!isComputerMove) {
            return 1;
        }
        else
            return 2;
    }
}
