package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public interface DataAccessInterface {
    Map<String, UserData> Registered_Users = new HashMap<>();
    Map<Integer, GameData> Created_Games = new HashMap<>();
    Map<String, AuthData> All_Auth_Data = new HashMap<>();
}
