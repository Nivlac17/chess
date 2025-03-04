package chess;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable{
    ChessGame.TeamColor color;
    ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.pieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece piece = (ChessPiece) o;
        return color == piece.color && pieceType == piece.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, pieceType);
    }


    @Override
    public ChessPiece clone() {
        try {
            ChessPiece clone = (ChessPiece) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
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
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculatorInterface moves;
        switch(getPieceType()){
            case BISHOP -> moves = new BishopMovesCalculator();
            case KING -> moves = new KingMovesCalculator();
            case KNIGHT -> moves = new KnightMovesCalculator();
            case ROOK -> moves = new RookMovesCalculator();
            case QUEEN -> moves = new QueenMovesCalculator();
            case PAWN -> moves = new PawnMovesCalculator();

            default -> {return Collections.emptyList();}
        }
        return moves.calculatePieceMoves(board, myPosition);
    }
}
