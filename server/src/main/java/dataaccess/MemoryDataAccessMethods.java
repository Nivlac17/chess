package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.GameList;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccessMethods implements DataAccessInterface{
    private static Map<String, UserData> REGISTERED_USERS = new HashMap<>();
    private static Map<Integer, GameData> CREATED_GAMES = new HashMap<>();
    private static Map<String, AuthData> AUTH_DATA = new HashMap<>();

    public String clear() throws DataAccessException {
        try {
            REGISTERED_USERS.clear();
            CREATED_GAMES.clear();
            AUTH_DATA.clear();
        } catch (Exception e){
            throw new DataAccessException(e.getMessage(), 500);
        }
        return "";
    }


    public UserData getUser(String username) {
//        search DB for username
        return REGISTERED_USERS.get(username);
    }
    String hashPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    public void createUser(UserData userData) {
//    create user object, add to db
        REGISTERED_USERS.put(userData.username(), new UserData(userData.username(),hashPassword(userData.password()), userData.email()));
    }


    public void createAuth(AuthData authData) {
        AUTH_DATA.put(authData.authToken(), authData);
    }

    public AuthData getAuth(String token) {
        return AUTH_DATA.get(token);
    }


    public void deleteAuth(String token) {
        AUTH_DATA.remove(token);
    }

    public Collection<GameList> listGames() {
        Collection<model.GameList> gameList = new ArrayList<>();
        for (GameData gameData : CREATED_GAMES.values()){
            gameList.add(new model.GameList(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName()));
        }
        return gameList;
    }


    public void createGame(int gameID, String gameName, ChessGame game) {
        CREATED_GAMES.put(gameID, new GameData(gameID,null, null, gameName, game));
    }

    public GameData getGame(int gameID) {
        return CREATED_GAMES.get(gameID);
    }

    public void updateGame(int gameID, String whiteUsername, String blackUsername,
                                  String gameName, ChessGame game) {
        GameData origonalGameData = CREATED_GAMES.get(gameID);
        if(whiteUsername != null){
            origonalGameData = origonalGameData.setWhiteUsername(whiteUsername);
        }
        if(blackUsername != null){
            origonalGameData = origonalGameData.setBlackUsername(blackUsername);
        }
        if(gameName != null){
            origonalGameData = origonalGameData.setGameName(gameName);
        }
        if(game != null){
            origonalGameData = origonalGameData.setGame(game);
        }

        CREATED_GAMES.put(gameID,origonalGameData);

    }
}