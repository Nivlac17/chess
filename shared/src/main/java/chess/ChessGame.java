package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard thisBoard = new ChessBoard();
    int turnTracker = 0;


    public ChessGame() {
        thisBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if((this.turnTracker % 2) == 0){
            return TeamColor.WHITE;
        } else {
            return TeamColor.BLACK;
        }
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if (team == TeamColor.WHITE){
            turnTracker = 0;
        }else{
            turnTracker = 1;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currentPiece = thisBoard.getPiece(startPosition);
        if ( currentPiece != null ) {
//            for move in possibleMoves, if move not placesKingInCheck add to returned collection
            Collection<ChessMove> tempMovesHolder = currentPiece.pieceMoves(thisBoard,startPosition);
            Collection<ChessMove> movesHolder = new ArrayList<>();
            for (ChessMove singleMove : tempMovesHolder){
                if (!placesKingInCheck(singleMove)){
                    movesHolder.add(singleMove);
                }
            }
            return movesHolder;
        }else{return new ArrayList<>();}
    }


    @Override
    public ChessGame clone(){
        try{
            ChessGame clone = (ChessGame) super.clone();
            clone.thisBoard = thisBoard.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean placesKingInCheck(ChessMove move) {

//        thoughts: clone chess game w/ chess board makeMove (Deep Copy!!!!!!!. return boolean is king in check
        ChessGame tempGame = this.clone();
        ChessBoard tempBoard = this.thisBoard.clone();
        ChessPiece movePiece = tempBoard.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() == null) {
            tempGame.thisBoard.addPiece(move.getEndPosition(), movePiece);
        } else {
            tempGame.thisBoard.addPiece(move.getEndPosition(), new ChessPiece(movePiece.getTeamColor(),move.getPromotionPiece()));
        }
        tempGame.thisBoard.addPiece(move.getStartPosition(), null);
        return tempGame.isInCheck(movePiece.getTeamColor());
    }





    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }
}
