package dataaccess;

import model.AuthData;
import model.UserData;

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


    public static void createAuth(String username, AuthData authData) {
        allAuthData.put(username, authData);
    }



}