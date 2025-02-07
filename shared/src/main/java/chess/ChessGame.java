package chess;

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
        if (currentPiece != null) {
            return currentPiece.pieceMoves(thisBoard,startPosition);
        }else{return null;}
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        turnTracker += 1;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = new ChessPosition(1,1);
        boolean checkBool = false;
        boolean foundKingBool = false;
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPiece king = thisBoard.getPiece(new ChessPosition(i, j));
                if (king != null){
                    if (king.pieceType == ChessPiece.PieceType.KING && king.getTeamColor() == teamColor){
                        kingPosition = new ChessPosition(i,j);
                        foundKingBool = true;
                        break;
                    }
                }
            }
            if (foundKingBool) {break;}
        }
        int kingRow = kingPosition.getRow();
        int kingCol = kingPosition.getColumn();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++) {
                ChessPiece threat = thisBoard.getPiece(new ChessPosition(i, j));
                if (threat != null && threat.getTeamColor() != teamColor) {
                    ChessPosition threatPosition = new ChessPosition(i, j);
                    Collection<ChessMove> threatPossibleMoves = validMoves(threatPosition);
                    for (ChessMove possibleMove : threatPossibleMoves){
                        if (kingRow == possibleMove.endPosition.getRow() && kingCol == possibleMove.endPosition.getColumn()) {
                            checkBool = true;
                            break;
                        }
                    }
                }
            }
            if(checkBool){break;}
        }
        return checkBool;
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
        thisBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return thisBoard;
    }
}