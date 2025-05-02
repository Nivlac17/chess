package chess;

import java.util.Collection;

public interface PieceMoveCalculatorInterface {
    Collection<ChessMove> calculatedPieceMoves(ChessBoard board, ChessPosition myPosition);
}
