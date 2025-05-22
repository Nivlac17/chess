package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMoveCalculatorInterface {
    public Collection<ChessMove> calculatedPieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1, 2}, {-1, 2}, {1, -2}, {-1, -2},   {2, 1}, {-2, 1}, {2, -1}, {-2, -1}};
        moveOneSquare(directions, row, col, board, myPosition, moves);
        return moves;
    }
}
