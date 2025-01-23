package chess;

import java.util.ArrayList;
import java.util.Collection;

interface PieceMovesCalculatorInterface{
    Collection<ChessMove> PiceMovesCalculator(ChessBoard board , ChessPosition position);

}
public class PieceMovesCalculator {
    int row;
    int col;
}

class BishopMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface {

    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.row = position.getRow();
        this.col = position.getColumn();
        int[][] possibleMoves = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}}; // these are directions, add loop? to make direction
        ChessPiece currentPiece = board.getPiece(position);
        Collection<ChessMove> chessMoves = new ArrayList<>();
        for (int[] direction : possibleMoves) {
            int futureRow = this.row + direction[0];
            int futureCol = this.col + direction[1];
            boolean blocked = false;
            while (!blocked) {
                if ( !(futureRow > 8 || futureRow <= 0 || futureCol > 8 || futureCol <= 0) ) {
                    ChessPosition futurePosistion = new ChessPosition(futureRow, futureCol);
                    ChessPiece pieceInFuturePosition = board.getPiece(futurePosistion);
                    if (pieceInFuturePosition != null) {
                        blocked = true;
                        if (currentPiece.getTeamColor() != pieceInFuturePosition.getTeamColor()) {
                            chessMoves.add(new ChessMove(position, futurePosistion, null));
                        }
                    } else {
                        chessMoves.add(new ChessMove(position, futurePosistion, null));
                        futureRow = futureRow + direction[0];
                        futureCol = futureCol + direction[1];
                    }
                } else {
                    blocked = true;
                }
            }
        }
        return chessMoves;

    }
}




class KingMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface {

    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.row = position.getRow();
        this.col = position.getColumn();
        int[][] possibleMoves = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}, {0,1}, {1,0}, {0,-1}, {-1,0}}; // these are directions, add loop? to make direction
        ChessPiece currentPiece = board.getPiece(position);
        Collection<ChessMove> chessMoves = new ArrayList<>();
        for (int[] direction : possibleMoves) {
            int futureRow = this.row + direction[0];
            int futureCol = this.col + direction[1];
            if (!(futureRow > 8 || futureRow <= 0 || futureCol > 8 || futureCol <= 0)) {
                ChessPosition futurePosistion = new ChessPosition(futureRow, futureCol);
                ChessPiece pieceInFuturePosition = board.getPiece(futurePosistion);
                if (pieceInFuturePosition != null) {
                    if (currentPiece.getTeamColor() != pieceInFuturePosition.getTeamColor()) {
                        chessMoves.add(new ChessMove(position, futurePosistion, null));
                    }
                } else {
                    chessMoves.add(new ChessMove(position, futurePosistion, null));
                }
            }
        }
        return chessMoves;
    }
}



class KnightMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.row = position.getRow();
        this.col = position.getColumn();
        int[][] possibleMoves = {{-1, 2}, {1, 2}, {-1, -2}, {1, -2}, {2,1}, {2,-1}, {-2,1}, {-2,-1}}; // these are directions, add loop? to make direction
        ChessPiece currentPiece = board.getPiece(position);
        Collection<ChessMove> chessMoves = new ArrayList<>();
        for (int[] direction : possibleMoves) {
            int futureRow = this.row + direction[0];
            int futureCol = this.col + direction[1];
            if (!(futureRow > 8 || futureRow <= 0 || futureCol > 8 || futureCol <= 0)) {
                ChessPosition futurePosistion = new ChessPosition(futureRow, futureCol);
                ChessPiece pieceInFuturePosition = board.getPiece(futurePosistion);
                if (pieceInFuturePosition != null) {
                    if (currentPiece.getTeamColor() != pieceInFuturePosition.getTeamColor()) {
                        chessMoves.add(new ChessMove(position, futurePosistion, null));
                    }
                } else {
                    chessMoves.add(new ChessMove(position, futurePosistion, null));
                }
            }
        }
        return chessMoves;
    }
}

class PawnMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface {

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.row = position.getRow();
        this.col = position.getColumn();
        ChessPiece currentPiece = board.getPiece(position);
        Collection<ChessMove> chessMoves = new ArrayList<>();
        boolean IsWhite = false;
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            IsWhite = true;
        }
int futureRow;
//            Normal Forward
        if (IsWhite) {
            futureRow = this.row + 1;
        } else {
            futureRow = this.row - 1;
        }
        int futureCol = this.col + 0;
        if (!(futureRow > 8 || futureRow <= 0 || futureCol > 8 || futureCol <= 0)) {
            ChessPosition futurePosistion = new ChessPosition(futureRow, futureCol);
            ChessPiece pieceInFuturePosition = board.getPiece(futurePosistion);
            if (pieceInFuturePosition == null) {
                if (IsWhite && futureRow == 8){
                    chessMoves.add(new ChessMove(position, futurePosistion, null));
                }
                else if (!IsWhite && futureRow == 1) {
                    chessMoves.add(new ChessMove(position, futurePosistion, null));
                }
                else{
                    chessMoves.add(new ChessMove(position, futurePosistion, null));
                }

//            Two Forward
                if ((IsWhite && this.row == 2)) {
                    futureRow = this.row + 2;
                    futureCol = this.col + 0;
                    if (!(futureRow > 8 || futureRow <= 0 || futureCol > 8 || futureCol <= 0)) {
                        futurePosistion = new ChessPosition(futureRow, futureCol);
                        pieceInFuturePosition = board.getPiece(futurePosistion);
                        if (pieceInFuturePosition == null) {
                            chessMoves.add(new ChessMove(position, futurePosistion, null));
                        }
                    }
                } else if ( !IsWhite && this.row == 7) {
                    futureRow = this.row - 2;
                    futureCol = this.col + 0;
                    if (!(futureRow > 8 || futureRow <= 0 || futureCol > 8 || futureCol <= 0)) {
                        futurePosistion = new ChessPosition(futureRow, futureCol);
                        pieceInFuturePosition = board.getPiece(futurePosistion);
                        if (pieceInFuturePosition == null) {
                            chessMoves.add(new ChessMove(position, futurePosistion, null));
                        }
                    }
                }
            }
        }



//            Attack Right
        if (IsWhite) {
            futureRow = this.row + 1;
        } else {
            futureRow = this.row - 1;
        }            futureCol = this.col + 1;
            if (!(futureRow > 8 || futureRow <= 0 || futureCol > 8 || futureCol <= 0)) {
                ChessPosition futurePosistion = new ChessPosition(futureRow, futureCol);
                ChessPiece pieceInFuturePosition = board.getPiece(futurePosistion);
                if (pieceInFuturePosition != null && currentPiece.getTeamColor() != pieceInFuturePosition.getTeamColor()) {
                    chessMoves.add(new ChessMove(position, futurePosistion, null));
                }
            }

//            Attack left
        if (IsWhite) {
            futureRow = this.row + 1;
        } else {
            futureRow = this.row - 1;
        }            futureCol = this.col + -1;
            if (!(futureRow > 8 || futureRow <= 0 || futureCol > 8 || futureCol <= 0)) {
                ChessPosition futurePosistion = new ChessPosition(futureRow, futureCol);
                ChessPiece pieceInFuturePosition = board.getPiece(futurePosistion);
                if (pieceInFuturePosition != null && currentPiece.getTeamColor() != pieceInFuturePosition.getTeamColor()) {
                    chessMoves.add(new ChessMove(position, futurePosistion, null));
                }
            }


            return chessMoves;
        }
    }





class QueenMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.row = position.getRow();
        this.col = position.getColumn();
        int[][] possibleMoves = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}}; // these are directions, add loop? to make direction
        ChessPiece currentPiece = board.getPiece(position);
        Collection<ChessMove> chessMoves = new ArrayList<>();
        for (int[] direction : possibleMoves) {
            int futureRow = this.row + direction[0];
            int futureCol = this.col + direction[1];
            boolean blocked = false;
            while (!blocked) {
                if ( !(futureRow > 8 || futureRow <= 0 || futureCol > 8 || futureCol <= 0) ) {
                    ChessPosition futurePosistion = new ChessPosition(futureRow, futureCol);
                    ChessPiece pieceInFuturePosition = board.getPiece(futurePosistion);
                    if (pieceInFuturePosition != null) {
                        blocked = true;
                        if (currentPiece.getTeamColor() != pieceInFuturePosition.getTeamColor()) {
                            chessMoves.add(new ChessMove(position, futurePosistion, null));
                        }
                    } else {
                        chessMoves.add(new ChessMove(position, futurePosistion, null));
                        futureRow = futureRow + direction[0];
                        futureCol = futureCol + direction[1];
                    }
                } else {
                    blocked = true;
                }
            }
        }
        return chessMoves;

    }
}


class RookMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.row = position.getRow();
        this.col = position.getColumn();
        int[][] possibleMoves = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}}; // these are directions, add loop? to make direction
        ChessPiece currentPiece = board.getPiece(position);
        Collection<ChessMove> chessMoves = new ArrayList<>();
        for (int[] direction : possibleMoves) {
            int futureRow = this.row + direction[0];
            int futureCol = this.col + direction[1];
            boolean blocked = false;
            while (!blocked) {
                if ( !(futureRow > 8 || futureRow <= 0 || futureCol > 8 || futureCol <= 0) ) {
                    ChessPosition futurePosistion = new ChessPosition(futureRow, futureCol);
                    ChessPiece pieceInFuturePosition = board.getPiece(futurePosistion);
                    if (pieceInFuturePosition != null) {
                        blocked = true;
                        if (currentPiece.getTeamColor() != pieceInFuturePosition.getTeamColor()) {
                            chessMoves.add(new ChessMove(position, futurePosistion, null));
                        }
                    } else {
                        chessMoves.add(new ChessMove(position, futurePosistion, null));
                        futureRow = futureRow + direction[0];
                        futureCol = futureCol + direction[1];
                    }
                } else {
                    blocked = true;
                }
            }
        }
        return chessMoves;

    }
}