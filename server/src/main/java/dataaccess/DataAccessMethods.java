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
            RegisteredUsers.clear();
            CreatedGames.clear();
            AllAuthData.clear();
        } catch (Exception e){
            throw new DataAccessException(e.getMessage(), 500);
        }
        return "";
    }


    public static UserData getUser(String username) {
//        search DB for username
        return RegisteredUsers.get(username);
    }


    public static void createUser(UserData userData) {
//    create user object, add to db
        RegisteredUsers.put(userData.username(), userData);
    }


    public static void createAuth(AuthData authData) {
        AllAuthData.put(authData.authToken(), authData);
    }

    public static AuthData getAuth(String token) {
        return AllAuthData.get(token);
    }


    public static void deleteAuth(String token) {
        AllAuthData.remove(token);
    }

    public static Collection<GameList> listGames() {
        Collection<model.GameList> gameList = new ArrayList<>();
        for (GameData gameData : CreatedGames.values()){
            gameList.add(new model.GameList(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName()));
        }
        return gameList;
    }


    public static void createGame(int gameID, String gameName, ChessGame game) {
        CreatedGames.put(gameID, new GameData(gameID,null, null, gameName, game));
    }

    public static GameData getGame(int gameID) {
        return CreatedGames.get(gameID);
    }

    public static void updateGame(int gameID, String whiteUsername, String blackUsername,
                                  String gameName, ChessGame game) {
        GameData origonalGameData = CreatedGames.get(gameID);
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