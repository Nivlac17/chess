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


    public static void createAuth(AuthData authData) {
        allAuthData.put(authData.authToken(), authData);
    }

    public static AuthData getAuth(String token) {
        return allAuthData.get(token);
    }


    public static void deleteAuth(String token) {
        allAuthData.remove(token);
        System.out.println(allAuthData);
    }
}