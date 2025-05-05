package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMoveCalculatorInterface {
    public Collection<ChessMove> calculatedPieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}, {1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        for (int[] direction : directions) {
            int futureRow = row + direction[0];
            int futureCol = col + direction[1];
            ChessPiece currentPiece = board.getPiece(myPosition);
            if (!(futureRow <= 0 || futureRow > 8 || futureCol <= 0 || futureCol > 8)) {
                ChessPosition futurePosition = new ChessPosition(futureRow, futureCol);
                ChessPiece futurePiece = board.getPiece(futurePosition);
                if (futurePiece != null) {
                    if (futurePiece.pieceColor != currentPiece.pieceColor) {
                        moves.add(new ChessMove(myPosition, futurePosition, null));
                    }
                } else {
                    moves.add(new ChessMove(myPosition, futurePosition, null));
                }
            }
        }
        return moves;
    }
}