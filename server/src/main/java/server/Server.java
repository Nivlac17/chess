package server;

import spark.*;

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


        



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


// Handlers
    private Object registerUser(Request request, Response response) {
        throw new RuntimeException("Not Implemented");
    }

    private Object clearApplication(Request request, Response response) {
        throw new RuntimeException("Not Implemented");
    }

    private Object logIn(Request request, Response response) {
        throw new RuntimeException("Not Implemented");
    }

    private Object logOut(Request request, Response response) {
        throw new RuntimeException("Not Implemented");
    }

    private Object listGames(Request request, Response response) {
        throw new RuntimeException("Not Implemented");

    }

    private Object joinGame(Request request, Response response) {
        throw new RuntimeException("Not Implemented");

    }




    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
