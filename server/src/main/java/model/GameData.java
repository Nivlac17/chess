package model;

import chess.ChessGame;
import dataaccess.DataAccessInterface;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public void setWhiteUsername(String username){
        DataAccessInterface.CREATED_GAMES.put(gameID, new GameData(gameID, username, blackUsername, gameName, game));
    }

    public void setBlackUsername(String username){
        DataAccessInterface.CREATED_GAMES.put(gameID, new GameData(gameID, whiteUsername, username, gameName, game));
    }

    public void setGameName(String gameName) {
        DataAccessInterface.CREATED_GAMES.put(gameID, new GameData(gameID, whiteUsername, blackUsername, gameName, game));

    }

    public void setGame(ChessGame game) {
        DataAccessInterface.CREATED_GAMES.put(gameID, new GameData(gameID, whiteUsername, blackUsername, gameName, game));
    }
}

