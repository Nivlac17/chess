package dataaccess;

public class DataAccessMethods {

    public static String getUser(String username) {
//        search DB for username
//        if username in db: return Failure response	[403] { "message": "Error: already taken" }
//        else
        return null;
    }


    public static void createUser(model.UserData userData) {
//    create user object, add to db
        System.out.println(userData);
    }


    public static void createAuth() {
    }



}