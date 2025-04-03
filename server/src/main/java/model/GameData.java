package model;

import chess.ChessGame;
import dataaccess.DataAccessInterface;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public void setWhiteUsername(String username){
        DataAccessInterface.createdGames.put(gameID, new GameData(gameID, username, blackUsername, gameName, game));
    }

    public void setBlackUsername(String username){
        DataAccessInterface.createdGames.put(gameID, new GameData(gameID, whiteUsername, username, gameName, game));
    }
}

