package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMoveCalculatorInterface{
    public Collection<ChessMove> calculatedPieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1,1},{-1,1},{1,-1},{-1,-1}};
        for (int[] direction: directions){
            int futureRow = row + direction[0];
            int futureCol = col + direction[1];
            ChessPiece currentPiece = board.getPiece(myPosition);
            boolean blocked = false;
            while(!blocked){
                if(!(futureRow <= 0 || futureRow > 8 || futureCol <= 0 || futureCol > 8)) {
                    ChessPosition futurePosition = new ChessPosition(futureRow, futureCol);
                    ChessPiece futurePiece = board.getPiece(futurePosition);
                    if (futurePiece != null) {
                        if(futurePiece.pieceColor != currentPiece.pieceColor){
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
        return moves;
    }
}
