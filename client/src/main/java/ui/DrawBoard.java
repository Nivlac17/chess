package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;
    int middleLine = SQUARE_SIZE_IN_PADDED_CHARS / 2;

    public static ChessBoard board;


    public static void main(ChessBoard board) {
        DrawBoard.board = board;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
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

        out.print("  ");
        printHeaderText(out, headerText);

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

    private static String setPieceColor(int row, int col, ChessBoard board){
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return(SET_TEXT_COLOR_BLUE);
        } else {
            return(SET_TEXT_COLOR_BLACK);
        }
    }


    private static String setPieceType(int row, int col, ChessBoard board){
        ChessPiece piece = null;
        try {
            piece = board.getPiece(new ChessPosition(row, col));
        }catch(Exception e){}

        if (piece == null){
            return null;
        }
        String picture = null;
        switch (piece.getPieceType()) {
            case BISHOP -> picture ="B";
            case KNIGHT -> picture ="N";
            case PAWN -> picture ="p";
            case KING -> picture ="K";
            case QUEEN -> picture ="Q";
            case ROOK -> picture ="R";
            default -> picture = null;
        }
        return picture;
    }

    private static void drawRowOfSquares(PrintStream out, int row) {
            out.print(SET_TEXT_COLOR_GREEN);
                out.printf("%2d ",(8 - row));
                String piece;
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if((row + boardCol) % 2 == 1) {
                    setWhite(out);
                }else{
                    setRed(out);
                }
                piece = setPieceType(row + 1, boardCol + 1, board);
                if (piece == null){
                    out.print(SET_TEXT_COLOR_GREEN);
                    out.print(("   "));
                }else{
                    out.print(setPieceColor(row + 1, boardCol + 1, board));
                    out.print((" " +  piece + " "));
                }
//                out.print(setPieceColor(row + 1, boardCol + 1, board));
//                    out.print((" " +  piece + " "));
                }
                setBlack(out);
            out.print(SET_TEXT_COLOR_GREEN);
                out.printf(" %2d",(8 - row));

            out.println();
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
