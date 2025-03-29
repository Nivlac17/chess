package dataaccess;

import model.AuthData;

public class DataAccessMethods implements DataAccessInterface{

    public static String getUser(String username) throws DataAccessException {
//        search DB for username
        if(registeredUsers.get(username) == null ){
            return null;

        } else {
//            throw new DataAccessException("Error: Username " + username + " is taken.");
            return username;
        }
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