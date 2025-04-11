package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;
    int middleLine = SQUARE_SIZE_IN_PADDED_CHARS / 2;

    private static final String EMPTY = "   ";


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);
        drawChessBoard(out);
        drawHeaders(out);


    }

    private static void drawHeaders(PrintStream out) {

        setBlack(out);
        out.print("  ");
        String[] headers = {  "A", "B", "C", "D", "E", "F", "G", "H" };
        for (String header : headers) {
            drawHeader(out, header);
        }
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = 1;
//        int suffixLength = 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print("  ");
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out) {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, boardRow);


        }
    }

    private static void drawRowOfSquares(PrintStream out, int row) {
        int middleLine = SQUARE_SIZE_IN_PADDED_CHARS / 2;

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            out.print(SET_TEXT_COLOR_GREEN);
            if (squareRow == middleLine){
                out.printf("%2d ",(8 - row));
            }else {
                out.print("   ");
            }


            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if((row + boardCol) % 2 == 1) {
                    setWhite(out);
                }else{
                    setRed(out);
                }

                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }
                setBlack(out);
            out.print(SET_TEXT_COLOR_GREEN);
            if (squareRow == middleLine){
                out.printf(" %2d",(8 - row));
            }else {
                out.print("  ");
            }
            out.println();
        }

    }



    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }


}
