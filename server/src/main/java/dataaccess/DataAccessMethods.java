package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.GameList;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class DataAccessMethods implements DataAccessInterface{

    public static String clear() throws DataAccessException {
        try {
            Registered_Users.clear();
            Created_Games.clear();
            All_Auth_Data.clear();
        } catch (Exception e){
            throw new DataAccessException(e.getMessage(), 500);
        }
        return "";
    }


    public static UserData getUser(String username) {
//        search DB for username
        return Registered_Users.get(username);
    }


    public static void createUser(UserData userData) {
//    create user object, add to db
        Registered_Users.put(userData.username(), userData);
    }


    public static void createAuth(AuthData authData) {
        All_Auth_Data.put(authData.authToken(), authData);
    }

    public static AuthData getAuth(String token) {
        return All_Auth_Data.get(token);
    }


    public static void deleteAuth(String token) {
        All_Auth_Data.remove(token);
    }

    public static Collection<GameList> listGames() {
        Collection<model.GameList> gameList = new ArrayList<>();
        for (GameData gameData : Created_Games.values()){
            gameList.add(new model.GameList(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName()));
        }
        return gameList;
    }


    public static void createGame(int gameID, String gameName, ChessGame game) {
        Created_Games.put(gameID, new GameData(gameID,null, null, gameName, game));
    }

    public static GameData getGame(int gameID) {
        return Created_Games.get(gameID);
    }

    public static void updateGame(int gameID, String whiteUsername, String blackUsername,
                                  String gameName, ChessGame game) {
        GameData origonalGameData = Created_Games.get(gameID);
        if(whiteUsername != null){
            origonalGameData.setWhiteUsername(whiteUsername);
        }
        if(blackUsername != null){
            origonalGameData.setBlackUsername(blackUsername);
        }
        if(gameName != null){
            origonalGameData.setGameName(gameName);
        }
        if(game != null){
            origonalGameData.setGame(game);
        }


    }
}