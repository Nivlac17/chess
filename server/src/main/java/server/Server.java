package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import model.GameData;
import model.JoinGame;
import service.ChessService;
import spark.*;

import java.util.Collection;
import java.util.Map;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::registerUser);
        Spark.delete("/db", this::clearApplication);
        Spark.post("/session", this::logIn);
        Spark.delete("/session", this::logOut);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.post("/game", this::createGame);
//        Spark.exception(ResponseException.class, this::exceptionHandler);



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }



    // Handlers
    private Object clearApplication(Request request, Response response) throws DataAccessException {
        try {
            return ChessService.clear();
        } catch (DataAccessException e ){
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }



    private Object registerUser(Request request, Response response) {
        Gson serializer = new Gson();
        var registerRequest = serializer.fromJson(request.body(), model.UserData.class);
        try {
            var registerResult = ChessService.register(registerRequest);
            return serializer.toJson(registerResult);
        } catch (DataAccessException e ) {
            response.status(e.getStatus());
            return new Gson().toJson(Map.of("message" , e.getMessage()));
        }
    }



    private Object logIn(Request request, Response response) {
        Gson serializer = new Gson();
        var logInRequest = serializer.fromJson(request.body(), model.UserData.class);
        try {
            var logInResult = ChessService.logIn(logInRequest);
            return serializer.toJson(logInResult);
        } catch (DataAccessException e ) {
            response.status(e.getStatus());
            return new Gson().toJson(Map.of("message" , e.getMessage()));
        }
    }



    private Object logOut(Request request, Response response) {
        String token = request.headers("Authorization");
        try {
            ChessService.logOut(token);
            return "";
        } catch (DataAccessException e ) {
            response.status(e.getStatus());
            return new Gson().toJson(Map.of("message" , e.getMessage()));
        }
    }



    private Object listGames(Request request, Response response) {
        Gson serializer = new Gson();
        JsonObject jsonSerializer = new JsonObject();
        String token = request.headers("Authorization");
        try {
            var listGamesResult = ChessService.listGames(token);
            JsonArray arrayOfGames = serializer.toJsonTree(listGamesResult).getAsJsonArray();
            jsonSerializer.add("games", arrayOfGames);
            return serializer.toJson(jsonSerializer);


        } catch (DataAccessException e ) {
            response.status(e.getStatus());
            return new Gson().toJson(Map.of("message" , e.getMessage()));
        }
    }

    private Object joinGame(Request request, Response response) {
        Gson serializer = new Gson();
        String token = request.headers("Authorization");
        JoinGame joinGameRequest = serializer.fromJson(request.body(), model.JoinGame.class);

        try {
            ChessService.joinGame(token, joinGameRequest);
            return "";
        } catch (DataAccessException e ) {
            response.status(e.getStatus());
            return new Gson().toJson(Map.of("message" , e.getMessage()));
        }
    }


    private Object createGame(Request request, Response response) {
        Gson serializer = new Gson();
        String token = request.headers("Authorization");
        var createGameRequest = serializer.fromJson(request.body(), model.GameData.class);
        try {
            int createGameResult = ChessService.createGame(token, createGameRequest);
            return serializer.toJson(Map.of("gameID", createGameResult));
        } catch (DataAccessException e ) {
            response.status(e.getStatus());
            return new Gson().toJson(Map.of("message" , e.getMessage()));
        }
    }






    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
