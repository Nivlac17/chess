package chess;

import java.util.ArrayList;
import java.util.Collection;

interface PieceMovesCalculatorInterface{
    Collection<ChessMove> PiceMovesCalculator(ChessBoard board , ChessPosition position);

}
public class PieceMovesCalculator {}

class BishopMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface {
    int row;
    int col;



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
//                    System.out.println("-----position: " + this.row + ", " + this.col + ", " + currentPiece.getTeamColor());
//                    System.out.println("-----future position: " + futureRow + ", " + futureCol);
                    if (pieceInFuturePosition != null) {
                        blocked = true;
//                        System.out.println("-----future piece: " + pieceInFuturePosition.getPieceType() + ", " + pieceInFuturePosition.getTeamColor());
                        if (currentPiece.getTeamColor() != pieceInFuturePosition.getTeamColor()) {
                            chessMoves.add(new ChessMove(position, futurePosistion, null));
//                            System.out.println("captured");
                        }
                    } else {
//                        System.out.println("-----future piece: null");
                        chessMoves.add(new ChessMove(position, futurePosistion, null));
                        futureRow = futureRow + direction[0];
                        futureCol = futureCol + direction[1];
                    }
                } else {
                    blocked = true;
                }//blocked = true;
            }
        }
        return chessMoves;

    }
}




class KingMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}

class KnightMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}

class PawnMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}

class QueenMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}

class RookMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}