package service;

import dataaccess.DataAccessException;
import dataaccess.DataAccessMethods;
import model.AuthData;

public class ChessService {
    public static AuthData register(model.UserData registerRequest) throws DataAccessException {
        try {
            if (dataaccess.DataAccessMethods.getUser(registerRequest.username()) == null) {
                DataAccessMethods.createUser(registerRequest);
                DataAccessMethods.createAuth();
                AuthData registerResult = new AuthData("auth111", registerRequest.username());
                System.out.println(DataAccessMethods.registeredUsers);
                return registerResult;
            } else {
                //        if username in db: return Failure response	[403] { "message": "Error: already taken" }
                throw new DataAccessException("Error: unknown register user error");
            }
        } catch (Exception e) {
            System.out.println(e);

        }
        return null;
    }
}



