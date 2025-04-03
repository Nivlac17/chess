package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public interface DataAccessInterface {
    Map<String, UserData> REGISTERED_USERS = new HashMap<>();
    Map<Integer, GameData> CREATED_GAMES = new HashMap<>();
    Map<String, AuthData> AUTH_DATA = new HashMap<>();
}
