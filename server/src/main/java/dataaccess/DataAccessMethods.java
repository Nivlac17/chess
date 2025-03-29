package dataaccess;

public class DataAccessMethods implements DataAccessInterface{

    public static String getUser(String username) throws DataAccessException {
//        search DB for username
        if(registeredUsers.get(username) == null ){
            return null;

        } else {
            throw new DataAccessException("Error: Username " + username + " is taken.");
        }
    }


    public static void createUser(model.UserData userData) {
//    create user object, add to db
        registeredUsers.put(userData.username(), userData);

//        System.out.println(userData);
    }


    public static void createAuth() {
    }



}