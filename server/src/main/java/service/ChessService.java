package service;

import dataaccess.DataAccessException;
import dataaccess.DataAccessMethods;
import model.AuthData;

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
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null){
            throw new DataAccessException("Error: Incomplete Information!!!", 400);
        }
        try {
            if (dataaccess.DataAccessMethods.getUser(registerRequest.username()) == null) {
                DataAccessMethods.createUser(registerRequest);
                String token = generateAuthToken();
                AuthData registerResult = new AuthData(token, registerRequest.username());
                DataAccessMethods.createAuth(registerRequest.username(), registerResult);
                System.out.println(DataAccessMethods.registeredUsers);
                return registerResult;
            }
        }catch(DataAccessException e) {
            throw new DataAccessException(e.getMessage(), e.getStatus());
        }
        return null;
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}



