package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMoveCalculatorInterface{
    public Collection<ChessMove> calculatedPieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1,0},{-1,0},{0,-1},{0,1}};
        loopMoves(moves, directions, row, col, myPosition, board);
        return moves;
    }
}
