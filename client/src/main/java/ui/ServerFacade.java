package ui;


import java.io.*;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameList;
import model.ListOfGameList;
import model.UserData;

public class ServerFacade {
    private final String serverUrl;


    public ServerFacade(String url) {
        serverUrl = url;
        System.out.println(url);

    }

    public AuthData register(String... params) throws ResponseException {
        String username = params[0];
        String password = params[1];
        String email    = params[2];
        UserData userData = new UserData(username,password,email);
        var path = "/user";
        return this.makeRequest("POST", path, null, userData, AuthData.class);
    }

    public AuthData logIn(String... params) throws ResponseException {
        String username = params[0];
        String password = params[1];
        UserData userData = new UserData(username,password,null);
        var path = "/session";
        return this.makeRequest("POST", path, null, userData, AuthData.class);
    }


    public GameList listGames(String authToken) throws ResponseException {
        var path = "/game";
        System.out.println(authToken);
        return  this.makeRequest("GET", path, authToken, null, GameList.class);
    }

    public int createGame(String authToken, String... params) throws ResponseException {
        String gameName = params[0]; // authToken and game name request
        var path = "/game";
        return this.makeRequest("POST", path, authToken, gameName, Integer.class);
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
//        }
        }
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}