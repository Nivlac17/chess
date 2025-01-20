package chess;

import java.util.ArrayList;
import java.util.Collection;

interface PieceMovesCalculatorInterface{
    Collection<ChessMove> PiceMovesCalculator(ChessBoard board , ChessPosition position);

}
public class PieceMovesCalculator {
}

class BishopMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}

class KingMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}

class KnightMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}

class PawnMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}

class QueenMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}

class RookMovesCalculater extends PieceMovesCalculator implements PieceMovesCalculatorInterface{

    @Override
    public Collection<ChessMove> PiceMovesCalculator(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
}