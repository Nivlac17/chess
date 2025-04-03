package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public interface DataAccessInterface {
    Map<String, UserData> RegisteredUsers = new HashMap<>();
    Map<Integer, GameData> CreatedGames = new HashMap<>();
    Map<String, AuthData> AllAuthData = new HashMap<>();
}
