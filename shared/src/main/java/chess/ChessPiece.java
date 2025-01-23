package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final PieceType pieceType;
    private final ChessGame.TeamColor pieceColor;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        ChessPiece that = (ChessPiece) o;
//        return pieceType == that.pieceType && pieceColor == that.pieceColor;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(pieceType, pieceColor);
//    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculatorInterface pieceCalculator;
        switch (getPieceType()) {
            case BISHOP:
                pieceCalculator = new BishopMovesCalculater();
                break;
            case KING:
                pieceCalculator = new KingMovesCalculater();
                break;
            case KNIGHT:
                pieceCalculator = new KnightMovesCalculater();
                break;
            case PAWN:
                pieceCalculator = new PawnMovesCalculater();
                break;
            case QUEEN:
                pieceCalculator = new QueenMovesCalculater();
                break;
            case ROOK:
                pieceCalculator = new RookMovesCalculater();
                break;

            default:
                return Collections.emptyList();
        }
        //        call to PieceMovesCalculator to return legal pieceMoves

        return pieceCalculator.PiceMovesCalculator(board, myPosition);

    }
}
