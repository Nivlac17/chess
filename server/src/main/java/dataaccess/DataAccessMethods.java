package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.GameList;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataAccessMethods implements DataAccessInterface{

    public static String clear() throws DataAccessException {
        try {
            registeredUsers.clear();
            createdGames.clear();
            allAuthData.clear();
        } catch (Exception e){
            throw new DataAccessException(e.getMessage(), 500);
        }
        return "";
    }


    public static UserData getUser(String username) {
//        search DB for username
        return registeredUsers.get(username);
    }


    public static void createUser(UserData userData) {
//    create user object, add to db
        registeredUsers.put(userData.username(), userData);
    }


    public static void createAuth(AuthData authData) {
        allAuthData.put(authData.authToken(), authData);
    }

    public static AuthData getAuth(String token) {
        return allAuthData.get(token);
    }


    public static AuthData deleteAuth(String token) {
        allAuthData.remove(token);
        return allAuthData.get(token);
    }

    public static Collection<GameList> listGames() {
        Collection<model.GameList> gameList = new ArrayList<>();
        for (GameData gameData : createdGames.values()){
            gameList.add(new model.GameList(gameData.gameID(),gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName()));
        }
        return gameList;
    }


    public static void createGame(int gameID, String gameName, ChessGame game) {
        createdGames.put(gameID, new GameData(gameID,null, null, gameName, game));
    }

    public static GameData getGame(int gameID) {
        return createdGames.get(gameID);
    }

    public static void updateGame(int gameID,String whiteUsername,String blackUsername, String gameName, ChessGame game) {
        GameData origonalGameData = createdGames.get(gameID);
        GameData newGameData = new GameData(
                gameID,
                (whiteUsername != null) ? whiteUsername : origonalGameData.whiteUsername(),
                (blackUsername != null) ? blackUsername : origonalGameData.blackUsername(),
                (gameName != null) ? gameName : origonalGameData.gameName(),
                (game != null) ? game : origonalGameData.game()
        );
        createdGames.remove(gameID);
        createdGames.put(gameID, newGameData);

    }
}