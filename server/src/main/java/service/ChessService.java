package service;

import dataaccess.DataAccessException;
import dataaccess.DataAccessMethods;
import model.AuthData;

import java.util.UUID;

public class ChessService {
    public static AuthData register(model.UserData registerRequest) throws DataAccessException {
        if (registerRequest.username().equals("") || registerRequest.password().equals("") || registerRequest.email().equals("")){
            throw new DataAccessException("Incomplete Information!!!", 400);
        }
        if (dataaccess.DataAccessMethods.getUser(registerRequest.username()) == null) {
            DataAccessMethods.createUser(registerRequest);
            String token =  generateAuthToken();
            AuthData registerResult = new AuthData(token, registerRequest.username());
            DataAccessMethods.createAuth(registerRequest.username(), registerResult);
            System.out.println(DataAccessMethods.registeredUsers);
            return registerResult;
        } else {
            //        if username in db: return Failure response	[403] { "message": "Error: already taken" }
            throw new DataAccessException("Error: already taken", 403);
        }

    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}



