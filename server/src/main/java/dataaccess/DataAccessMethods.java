package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

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


    public static void createUser(model.UserData userData) {
//    create user object, add to db
        registeredUsers.put(userData.username(), userData);
//        System.out.println(userData);
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

    public static Map<String, GameData> listGames() {
        return createdGames;
    }


    public static void createGame(int gameID, String gameName, ChessGame game) {
        createdGames.put(gameName, new GameData(gameID,null, null, gameName, game));
    }
}