package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMoveCalculatorInterface {
    public Collection<ChessMove> calculatedPieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}, {1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        moveOneSquare(directions, row, col, board, myPosition, moves);
        return moves;
    }
}