package chess;

import java.util.Collection;

public interface PieceMoveCalculatorInterface {
    Collection<ChessMove> calculatedPieceMoves(ChessBoard board, ChessPosition myPosition);

    default void loopMoves(Collection<ChessMove> moves, int[][] directions, int row, int col,
                                                               ChessPosition myPosition, ChessBoard board){
        for (int[] direction : directions) {
            int futureRow = row + direction[0];
            int futureCol = col + direction[1];
            ChessPiece currentPiece = board.getPiece(myPosition);
            boolean blocked = false;
            while (!blocked) {
                if (futureRow > 0 && futureRow <= 8 && futureCol > 0 && futureCol <= 8) {
                    ChessPosition futurePosition = new ChessPosition(futureRow, futureCol);
                    ChessPiece futurePiece = board.getPiece(futurePosition);
                    if (futurePiece != null) {
                        if (futurePiece.pieceColor != currentPiece.pieceColor) {
                            moves.add(new ChessMove(myPosition, futurePosition, null));
                        }
                        blocked = true;
                    } else {
                        moves.add(new ChessMove(myPosition, futurePosition, null));
                        futureRow += direction[0];
                        futureCol += direction[1];
                    }
                } else {
                    blocked = true;
                }
            }
        }
    }
}
