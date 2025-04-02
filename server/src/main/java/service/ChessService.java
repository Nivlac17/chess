package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DataAccessMethods;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Map;
import java.util.UUID;

public class ChessService {

    public static String clear() throws DataAccessException {
        try {
            return DataAccessMethods.clear();
        } catch (DataAccessException e ){
            throw new DataAccessException(e.getMessage(), e.getStatus());
        }
    }

    public static AuthData register(model.UserData registerRequest) throws DataAccessException {
        if (registerRequest.password() == null || registerRequest.username() == null) {
            throw new DataAccessException("Error: Invalid User", 400);
        } else if ( registerRequest.username().isEmpty()  || registerRequest.password().isEmpty()  || registerRequest.email().isEmpty()){
            throw new DataAccessException("Error: Incomplete Information!!!", 400);
        }
            if (dataaccess.DataAccessMethods.getUser(registerRequest.username()) == null) {
                DataAccessMethods.createUser(registerRequest);
                AuthData registerResult = new AuthData(generateAuthToken(), registerRequest.username());
                DataAccessMethods.createAuth(registerResult);
                System.out.println(DataAccessMethods.registeredUsers);
                return registerResult;
            } else {
                throw new DataAccessException("Error: Username " + registerRequest.username() + " is taken.", 403);
            }
    }


    private static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }


    public static AuthData logIn(model.UserData logInRequest) throws DataAccessException {
        if ( logInRequest.username().isEmpty()  || logInRequest.password().isEmpty()){
            throw new DataAccessException("Error: Incomplete Information!!!", 400);
        }

        UserData userData = dataaccess.DataAccessMethods.getUser(logInRequest.username());

        if (userData == null) {
            throw new DataAccessException("Error: Invalid User", 401);
        }else if (!(userData.password().equals(logInRequest.password())) || userData.username().isEmpty() ){
            throw new DataAccessException("Error: invalid credentials", 401);
        }else {
            AuthData logInResult = new AuthData(generateAuthToken(), logInRequest.username());
            DataAccessMethods.createAuth(logInResult);
            return logInResult;
        }
    }

    public static Object logOut(String token) throws DataAccessException {
        AuthData authData = DataAccessMethods.getAuth(token);
        System.out.println(authData);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        DataAccessMethods.deleteAuth(token);
        return "";
    }


    public static Object listGames(String token) throws DataAccessException {
        AuthData authData = DataAccessMethods.getAuth(token);
        System.out.println(authData);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        return DataAccessMethods.listGames();
    }

    public static int createGame(String token, GameData createGameRequest) throws DataAccessException{
        AuthData authData = DataAccessMethods.getAuth(token);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        String gameName = createGameRequest.gameName();
        ChessGame game = new ChessGame();
        int gameID = generateGameID();
        DataAccessMethods.createGame(gameID, gameName, game);

        return gameID;
    }

    private static int generateGameID() {
        Map<String, GameData> gameDataList = DataAccessMethods.listGames();
        int idnum = 1;
        for ( GameData game:  gameDataList.values()){
            idnum++;
        }
        return idnum + 1;
    }
}



