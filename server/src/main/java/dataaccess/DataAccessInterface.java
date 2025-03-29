package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public interface DataAccessInterface {
    Map<String, UserData> registeredUsers = new HashMap<>();
    Map<String, GameData> createdGames = new HashMap<>();
    Map<String, AuthData> allAuthData = new HashMap<>();
}
