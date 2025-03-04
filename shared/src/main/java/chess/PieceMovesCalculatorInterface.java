package chess;

import java.util.ArrayList;
import java.util.Collection;
import static chess.ChessGame.TeamColor.WHITE;

public interface PieceMovesCalculatorInterface {
    Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition);
}

class PieceMovesCalculator{
    void lineMoves(int[][] directions, int row, int col, ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition){
        for (int[] direction : directions){
            int futureRow = row + direction[0];
            int futureCol = col + direction[1];
            isMovingLoop(futureRow, futureCol, board, direction,myPosition,moves);
        }
    }
void isMovingLoop(int futureRow, int futureCol, ChessBoard board, int[] direction, ChessPosition myPosition,Collection<ChessMove> moves){
    ChessPiece currentPiece = board.getPiece(myPosition);
    boolean blocked = false;
    while(!blocked){
        if (!(futureRow > 8 || futureCol > 8 || futureRow <=0 || futureCol <=0 )){
            ChessPosition futurePosition = new ChessPosition(futureRow,futureCol);
            ChessPiece pieceInFuturePosition = board.getPiece(futurePosition);

            if (pieceInFuturePosition != null) {
                if (pieceInFuturePosition.getTeamColor() != currentPiece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, futurePosition, null));
                }
                blocked = true;
            }else{
                moves.add(new ChessMove(myPosition, futurePosition, null));
                futureRow += direction[0];
                futureCol += direction[1];
            }
        } else {blocked = true;
        }
    }
}
    void oneMove(int[][] directions, int row, int col, ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition){
        ChessPiece currentPiece = board.getPiece(myPosition);
        for (int[] direction : directions){
            int futureRow = row + direction[0];
            int futureCol = col + direction[1];
            if (!(futureRow > 8 || futureCol > 8 || futureRow <=0 || futureCol <=0 )){
                ChessPosition futurePosition = new ChessPosition(futureRow,futureCol);
                ChessPiece pieceInFuturePosition = board.getPiece(futurePosition);

                if (pieceInFuturePosition != null) {
                    if (pieceInFuturePosition.getTeamColor() != currentPiece.getTeamColor()){
                        moves.add(new ChessMove(myPosition, futurePosition, null));
                    }
                }else{
                    moves.add(new ChessMove(myPosition, futurePosition, null));

                }


            }

        }
    }
}


class BishopMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1,1}, {-1,-1},{1,-1},{-1,1}};
        lineMoves(directions,row,col,board, moves, myPosition);
        return moves;
    }
}


class KingMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1,1}, {-1,-1},{1,-1},{-1,1},     {0,1}, {1,0}, {0,-1}, {-1,0}};
        oneMove(directions,row,col,board, moves, myPosition);
        return moves;
    }
}


class KnightMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1,2}, {-1,2},{1,-2},{-1,-2},     {2,1}, {-2,1}, {2,-1}, {-2,-1}};
        oneMove(directions,row,col,board, moves, myPosition);
        return moves;
    }
}


class RookMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        lineMoves(directions,row,col,board, moves, myPosition);
        return moves;
    }
}


class QueenMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0},     {1,1}, {-1,-1},{1,-1},{-1,1}};
        lineMoves(directions,row,col,board, moves, myPosition);
        return moves;
    }
}


class PawnMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece currentPiece = board.getPiece(myPosition);
        boolean isWhite = false;
        if(currentPiece.getTeamColor() == WHITE){
            isWhite = true;
        }

        Collection<ChessMove> moves = new ArrayList<>();
        int futureRow;
        if(isWhite) {
            futureRow = row + 1;
        } else {
            futureRow = row -1;
        }
        int futureCol = col;
        if (!(futureRow > 8 || futureCol > 8 || futureRow <=0 || futureCol <=0 )){
            ChessPosition futurePosition = new ChessPosition(futureRow,futureCol);
            ChessPiece pieceInFuturePosition = board.getPiece(futurePosition);
            if (pieceInFuturePosition == null) {
                addPawnMoveOrPromotionToCollection(myPosition,futurePosition,moves);
                if (row == 2 && isWhite) {
                    futureRow = 4;
                    futurePosition = new ChessPosition(futureRow, futureCol);
                    pieceInFuturePosition = board.getPiece(futurePosition);
                    if (pieceInFuturePosition == null) {
                        moves.add(new ChessMove(myPosition, futurePosition, null));
                    }
                } else if ((row == 7 && !isWhite)) {
                    futureRow = 5;
                    futurePosition = new ChessPosition(futureRow, futureCol);
                    pieceInFuturePosition = board.getPiece(futurePosition);
                    if (pieceInFuturePosition == null) {
                        moves.add(new ChessMove(myPosition, futurePosition, null));
                    }
                }
            }
        }
        //        Attack Right if White
        if(isWhite) {
            futureRow = row + 1;
        } else {
            futureRow = row -1;
        }
        attackDiagonal(futureRow, col + 1, board, moves, myPosition);
        //        Attack Left if White
        if(isWhite) {
            futureRow = row + 1;
        } else {
            futureRow = row -1;
        }
        attackDiagonal(futureRow, col - 1, board, moves, myPosition);




        return moves;
    }
    void attackDiagonal(int futureRow, int futureCol, ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition){
        if (!(futureRow > 8 || futureCol > 8 || futureRow <=0 || futureCol <=0 )){
            ChessPosition futurePosition = new ChessPosition(futureRow,futureCol);
            ChessPiece pieceInFuturePosition = board.getPiece(futurePosition);
            if (pieceInFuturePosition != null && pieceInFuturePosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                addPawnMoveOrPromotionToCollection(myPosition,futurePosition,moves);
            }
        }
    }
    void addPawnMoveOrPromotionToCollection(ChessPosition myPosition, ChessPosition futurePosition, Collection<ChessMove> moves){
        if(futurePosition.getRow() == 8 || futurePosition.getRow() == 1){
            moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.BISHOP));
        }else {
            moves.add(new ChessMove(myPosition, futurePosition, null));
        }
    }
}


