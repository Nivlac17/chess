package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static chess.ChessGame.TeamColor.WHITE;

public interface PieceMovesCalculatorInterface {
    Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition);
}

class PieceMovesCalculator{
    int row;
    int col;
}


class BishopMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.row = myPosition.getRow();
        this.col = myPosition.getColumn();
        ChessPiece currentPiece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        int[][] Directions = {{1,1}, {-1,-1},{1,-1},{-1,1}};

        for (int[] Direction : Directions){
            int futureRow = this.row + Direction[0];
            int futureCol = this.col + Direction[1];
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
                        futureRow += Direction[0];
                        futureCol += Direction[1];
                    }
                } else {blocked = true;}

            }

        }
        return moves;
    }
}


class KingMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.row = myPosition.getRow();
        this.col = myPosition.getColumn();
        ChessPiece currentPiece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        int[][] Directions = {{1,1}, {-1,-1},{1,-1},{-1,1},     {0,1}, {1,0}, {0,-1}, {-1,0}};

        for (int[] Direction : Directions){
            int futureRow = this.row + Direction[0];
            int futureCol = this.col + Direction[1];
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
        return moves;
    }
}


class KnightMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.row = myPosition.getRow();
        this.col = myPosition.getColumn();
        ChessPiece currentPiece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        int[][] Directions = {{1,2}, {-1,2},{1,-2},{-1,-2},     {2,1}, {-2,1}, {2,-1}, {-2,-1}};

        for (int[] Direction : Directions){
            int futureRow = this.row + Direction[0];
            int futureCol = this.col + Direction[1];
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
        return moves;
    }
}


class RookMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.row = myPosition.getRow();
        this.col = myPosition.getColumn();
        ChessPiece currentPiece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        int[][] Directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};

        for (int[] Direction : Directions){
            int futureRow = this.row + Direction[0];
            int futureCol = this.col + Direction[1];
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
                        futureRow += Direction[0];
                        futureCol += Direction[1];
                    }
                } else {blocked = true;}

            }

        }
        return moves;
    }
}


class QueenMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.row = myPosition.getRow();
        this.col = myPosition.getColumn();
        ChessPiece currentPiece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        int[][] Directions = {{0,1}, {1,0}, {0,-1}, {-1,0},     {1,1}, {-1,-1},{1,-1},{-1,1}};

        for (int[] Direction : Directions){
            int futureRow = this.row + Direction[0];
            int futureCol = this.col + Direction[1];
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
                        futureRow += Direction[0];
                        futureCol += Direction[1];
                    }
                } else {blocked = true;}

            }

        }
        return moves;
    }
}


class PawnMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.row = myPosition.getRow();
        this.col = myPosition.getColumn();
        ChessPiece currentPiece = board.getPiece(myPosition);
        boolean isWhite = false;
        if(currentPiece.getTeamColor() == WHITE){
            isWhite = true;
        }

        Collection<ChessMove> moves = new ArrayList<>();
        int futureRow;
        if(isWhite) {
            futureRow = this.row + 1;
        } else {
            futureRow = this.row -1;
        }
        int futureCol = this.col;
        if (!(futureRow > 8 || futureCol > 8 || futureRow <=0 || futureCol <=0 )){
            ChessPosition futurePosition = new ChessPosition(futureRow,futureCol);
            ChessPiece pieceInFuturePosition = board.getPiece(futurePosition);
            if (pieceInFuturePosition == null) {
                addPawnMoveOrPromotionToCollection(myPosition,futurePosition,moves);
                if (this.row == 2 && isWhite) {
                    futureRow = 4;
                    futurePosition = new ChessPosition(futureRow, futureCol);
                    pieceInFuturePosition = board.getPiece(futurePosition);
                    if (pieceInFuturePosition == null) {
                        moves.add(new ChessMove(myPosition, futurePosition, null));
                    }
                } else if ((this.row == 7 && !isWhite)) {
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
            futureRow = this.row + 1;
        } else {
            futureRow = this.row -1;
        }
        attackDiagonal(futureRow, this.col + 1, board, moves, myPosition);
        //        Attack Left if White
        if(isWhite) {
            futureRow = this.row + 1;
        } else {
            futureRow = this.row -1;
        }
        attackDiagonal(futureRow, this.col - 1, board, moves, myPosition);




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


