package ui;


import java.io.*;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;


import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import exception.ResponseException;
import model.*;

public class ServerFacade {
    private final String serverUrl;


    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(String... params) throws ResponseException  {
        String username = params[0];
        String password = params[1];
        String email    = params[2];
        UserData userData = new UserData(username,password,email);
        var path = "/user";
        try {
            return this.makeRequest("POST", path, null, userData, AuthData.class);
        }catch (ResponseException | NullPointerException e){
            throw new ResponseException(404, "User Already Exists, Please Use a Different Username");
        }
    }

    public AuthData logIn(String... params) throws ResponseException {
        String username = params[0];
        String password = params[1];
        UserData userData = new UserData(username,password,null);
        var path = "/session";
        try{
        return this.makeRequest("POST", path, null, userData, AuthData.class);
        }catch (ResponseException | NullPointerException e){
            throw new ResponseException(404, "Invalid Credential, Please try again.");
        }
    }


    public List<GameList> listGames(String authToken) throws ResponseException {
        var path = "/game";
        JsonObject response = this.makeRequest("GET", path, authToken, null,  JsonObject.class);
        JsonArray gamesArray = response.getAsJsonArray("games");
        List<GameList> games = new Gson().fromJson(gamesArray, new TypeToken<List<GameList>>(){}.getType());

        return games;
    }

    public GameID createGame(String authToken, String... params) throws ResponseException {
        var path = "/game";
        var gameName = new GameCreationRequest(params[0]);

        return this.makeRequest("POST", path, authToken, gameName, GameID.class);
    }


    public String joinGame(String authToken, String[] params) throws ResponseException {
        var path = "/game";
        String color = null;
        if(params[0].equals("white")){
            color = "WHITE";
        }
        if(params[0].equals("black")){
            color = "BLACK";
        }
        var joinRequest = new JoinGame(color ,Integer.parseInt(params[1]));
        this.makeRequest("PUT", path, authToken, joinRequest, Void.class);

        return " Game Joined Successfully ";
    }

    public String logOut(String authToken) throws ResponseException {
        var path = "/session";
                this.makeRequest("DELETE", path, authToken, null, Void.class);
        return " Successful Logout ";
    }










    private <T> T makeRequest(String method, String path, String authToken, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws RuntimeException, IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws ResponseException, IOException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new RuntimeException("not implemented but yes, it is an error in server facade");
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}