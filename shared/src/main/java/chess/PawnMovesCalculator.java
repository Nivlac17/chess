package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMoveCalculatorInterface {
    public Collection<ChessMove> calculatedPieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        boolean isWhite = currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE;
        int futureRow;
        if (isWhite) {
            futureRow = row + 1;
        } else {
            futureRow = row - 1;
        }
        if (!(futureRow <= 0 || futureRow > 8 || col <= 0 || col > 8)) {
            ChessPosition futurePosition = new ChessPosition(futureRow, col);
            ChessPiece futurePiece = board.getPiece(futurePosition);
            if (futurePiece == null) {
                promotionPieceAdd(moves, myPosition,futurePosition, futureRow);

                if ((isWhite && row == 2) || (!isWhite && row == 7)) {
                    if (isWhite) {
                        futureRow += 1;
                    } else {
                        futureRow -= 1;
                    }
                    if (!(futureRow <= 0 || futureRow > 8)) {
                        futurePosition = new ChessPosition(futureRow, col);
                        futurePiece = board.getPiece(futurePosition);
                        if (futurePiece == null) {
                            promotionPieceAdd(moves, myPosition,futurePosition, futureRow);
                        }
                    }
                }


            }
        }
        return moves;
    }


    
    private void promotionPieceAdd(
            Collection<ChessMove> moves, ChessPosition myPosition, ChessPosition futurePosition, int futureRow){
        if (futureRow == 8 || futureRow == 1){
            moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(myPosition, futurePosition, ChessPiece.PieceType.ROOK));
        }else {
            moves.add(new ChessMove(myPosition, futurePosition, null));
        }
    }

}