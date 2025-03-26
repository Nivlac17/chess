package service;

import dataaccess.DataAccessException;
import dataaccess.DataAccessMethods;
import model.AuthData;

public class ChessService {
    public static AuthData register(model.UserData registerRequest){
        if (dataaccess.DataAccessMethods.getUser(registerRequest.username()) == null){
            DataAccessMethods.createUser(registerRequest);
            DataAccessMethods.createAuth();
        }
        AuthData registerResult = null;
        return registerResult;
    }

}
