package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMoveCalculatorInterface{
    public Collection<ChessMove> calculatedPieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1,1},{-1,1},{1,-1},{-1,-1}};
        loopMoves(moves, directions, row, col, myPosition, board);
        return moves;
    }
}
