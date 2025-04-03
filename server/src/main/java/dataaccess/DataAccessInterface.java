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
    UserData getUser(String username);
    void createUser(UserData userData);
    void createAuth(AuthData authData);
    AuthData getAuth(String token);
    void deleteAuth(String token);
    Collection<GameList> listGames();
    void createGame(int gameID, String gameName, ChessGame game);
    GameData getGame(int gameID);
    void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);


}
