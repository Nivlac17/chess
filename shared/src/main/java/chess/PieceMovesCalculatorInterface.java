package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;

public interface PieceMovesCalculatorInterface {
    Collection<ChessMove> PieceMovesCalculaterInterface(ChessBoard board, ChessPosition myPosition);
}

class PieceMovesCalculator{
    int row;
    int col;
}


class BishopMovesCalculator extends PieceMovesCalculator implements PieceMovesCalculatorInterface{
    public Collection<ChessMove> PieceMovesCalculaterInterface(ChessBoard board, ChessPosition myPosition) {
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
    public Collection<ChessMove> PieceMovesCalculaterInterface(ChessBoard board, ChessPosition myPosition) {
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
    public Collection<ChessMove> PieceMovesCalculaterInterface(ChessBoard board, ChessPosition myPosition) {
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
    public Collection<ChessMove> PieceMovesCalculaterInterface(ChessBoard board, ChessPosition myPosition) {
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
    public Collection<ChessMove> PieceMovesCalculaterInterface(ChessBoard board, ChessPosition myPosition) {
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
    public Collection<ChessMove> PieceMovesCalculaterInterface(ChessBoard board, ChessPosition myPosition) {
        this.row = myPosition.getRow();
        this.col = myPosition.getColumn();
        ChessPiece currentPiece = board.getPiece(myPosition);
        boolean IsWhite = false;
        if(currentPiece.getTeamColor() == WHITE){
            IsWhite = true;
        }

        Collection<ChessMove> moves = new ArrayList<>();
        int futureRow;
        if(IsWhite) {
            futureRow = this.row + 1;
        } else {
            futureRow = this.row -1;
        }
        int futureCol = this.col;
        if (!(futureRow > 8 || futureCol > 8 || futureRow <=0 || futureCol <=0 )){
            ChessPosition futurePosition = new ChessPosition(futureRow,futureCol);
            ChessPiece pieceInFuturePosition = board.getPiece(futurePosition);
            if (pieceInFuturePosition == null) {
                if(futureRow == 8 || futureRow == 1){
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.BISHOP));
                }else {
                    moves.add(new ChessMove(myPosition, futurePosition, null));
                }
                if (this.row == 2 && IsWhite) {
                    futureRow = 4;
                    futurePosition = new ChessPosition(futureRow, futureCol);
                    pieceInFuturePosition = board.getPiece(futurePosition);
                    if (pieceInFuturePosition == null) {
                        moves.add(new ChessMove(myPosition, futurePosition, null));
                    }
                } else if ((this.row == 7 && !IsWhite)) {
                    futureRow = 5;
                    futurePosition = new ChessPosition(futureRow, futureCol);
                    pieceInFuturePosition = board.getPiece(futurePosition);
                    if (pieceInFuturePosition == null) {
                        moves.add(new ChessMove(myPosition, futurePosition, null));
                    }
                }
            }


        }
//        Attack Right
        if(IsWhite) {
            futureRow = this.row + 1;
        } else {
            futureRow = this.row -1;
        }

        futureCol = this.col + 1;
        if (!(futureRow > 8 || futureCol > 8 || futureRow <=0 || futureCol <=0 )){
            ChessPosition futurePosition = new ChessPosition(futureRow,futureCol);
            ChessPiece pieceInFuturePosition = board.getPiece(futurePosition);
            if (pieceInFuturePosition != null && pieceInFuturePosition.getTeamColor() != currentPiece.getTeamColor()) {
                if(futureRow == 8 || futureRow == 1){
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.BISHOP));
                }else {
                    moves.add(new ChessMove(myPosition, futurePosition, null));
                }            }
        }


        //        Attack Left
        if(IsWhite) {
            futureRow = this.row + 1;
        } else {
            futureRow = this.row -1;
        }

        futureCol = this.col - 1;
        if (!(futureRow > 8 || futureCol > 8 || futureRow <=0 || futureCol <=0 )){
            ChessPosition futurePosition = new ChessPosition(futureRow,futureCol);
            ChessPiece pieceInFuturePosition = board.getPiece(futurePosition);
            if (pieceInFuturePosition != null && pieceInFuturePosition.getTeamColor() != currentPiece.getTeamColor()) {
                if(futureRow == 8 || futureRow == 1){
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.BISHOP));
                }else {
                    moves.add(new ChessMove(myPosition, futurePosition, null));
                }            }
        }




        return moves;
    }
}