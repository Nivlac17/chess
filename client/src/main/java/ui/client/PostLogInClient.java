package ui.client;

import exception.ResponseException;
import model.GameID;
import model.GameList;
import ui.ServerFacade;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PostLogInClient {

    private ServerFacade server;
    private String serverUrl;

    public  PostLogInClient(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
//        this.notificationHandler = notificationHandler;
    }

    public String eval(String input, String authToken) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "l", "list" -> listGames(authToken);
                case "c", "create" -> createGame(authToken, params);
                case "j", "join" -> joinGame(authToken, params);
//                case "w", "watch" -> watchGame(authToken, params);
                case "logout" -> logOut(authToken);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }



    private String listGames(String authToken) {
        try {

            List<GameList> gameList = server.listGames(authToken);
            if (gameList.isEmpty()) {
                return "No Games Currently Created";
            }else{
                String uiList = "Games: ";
                for(GameList game: gameList){
                    String white = "Empty";
                    String black = "Empty";
                    if(game.whiteUsername() != null){
                        white = game.whiteUsername();
                    }
                    if (game.blackUsername() != null){
                        black = game.blackUsername();
                    }
                    uiList = (uiList +
                            "\n GameID: " + game.gameID() +
                            "\t\t White: " + white +
                            "\tBlack: " + black +
                            "\tGame Name: " +game.gameName());
                }
                return uiList;
            }
        } catch (ResponseException e){
            return e.getMessage();
        }

    }


    private String createGame(String authToken, String... params) {
        try {
            GameID gameID = server.createGame(authToken, params);
            if (gameID.gameID() > 0 ) {
                return "Game Created";
            }

        } catch (ResponseException e){
            return e.getMessage();
        }
        return "Failure to create Game";
    }

    private String joinGame(String authToken, String[] params) {

        try {
            if (Objects.equals(server.joinGame(authToken, params), " Game Joined Successfully ")){
                return " Game Joined Successfully! ";
            }
        } catch (ResponseException e) {
        return "Failure to Join Game " + e.getMessage();
        }
        return "Failure to Join Game ";
    }


    private String logOut(String authToken) {

        try {
            if (Objects.equals(server.logOut(authToken), " Successful Logout ")){
                return " GOODBYE!!! ";
            }
        } catch (ResponseException e) {
            return "Failure to Join Game " + e.getMessage();
        }
        return "Failure to Join Game ";
    }



    public String help(){
        return   """
                        Options:
                        List current games:  "l", "list"
                        Create a new game:   "c", "create" ‹GAME NAME>
                        Join a game:         "j", "join" ‹WHITE/BLACK›‹GAME ID> 
                        Watch a game:        "w", "watch" <GAME ID>
                        Logout:              "logout"
                                
                """;

    }
}
