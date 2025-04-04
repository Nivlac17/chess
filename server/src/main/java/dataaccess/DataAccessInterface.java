package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.GameList;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface DataAccessInterface {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData userData) throws DataAccessException;
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String token) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
    Collection<GameList> listGames();
    void createGame(int gameID, String gameName, ChessGame game);
    GameData getGame(int gameID);
    void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);


    String clear() throws DataAccessException;
}
