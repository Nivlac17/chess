package ui.client;

import exception.ResponseException;
import model.GameData;
import model.GameID;
import model.GameList;
import ui.DrawBoard;
import ui.ServerFacade;

import java.util.*;

public class PostLogInClient {
    public static Map<Integer, Integer> listNumberInterpreter;
    private ServerFacade server;
    private String serverUrl;

    public  PostLogInClient(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.listNumberInterpreter = new HashMap<>();
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
                case "w", "watch" -> watchGame(authToken, params);
                case "logout" -> logOut(authToken);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private void setListNumberInterpreter (String authToken) throws ResponseException {
        List<GameList> gameList = server.listGames(authToken);
            int i = 0;
            for (GameList game : gameList) {
                i++;
                listNumberInterpreter.put(i, game.gameID());
            }
    }

    private String watchGame(String authToken, String... params) {
        if (params.length != 1){
            System.out.println("Invalid Game Input, Please Try Again");
            return help();
        }

        try {
            if(listNumberInterpreter == null){
                setListNumberInterpreter(authToken);
            }
        } catch (Exception e){}
            params[0] = String.valueOf(listNumberInterpreter.get(Integer.parseInt(params[0])));


        try {
            GameData result = server.getGame(authToken, "1");
            DrawBoard.main(result.game().getBoard(), "white");
            return " Game Joined Successfully! ";
        } catch (ResponseException e) {
            return "Failure to Join Game " ;
        }
    }


    private String listGames(String authToken) {
        try {

            List<GameList> gameList = server.listGames(authToken);
            if (gameList.isEmpty()) {
                return "No Games Currently Created";
            }else{
                String uiList = "Games: ";
                int i = 0;
                for(GameList game: gameList){
                    i++;
                    listNumberInterpreter.put(i, game.gameID());
                    String white = "Empty";
                    String black = "Empty";
                    if(game.whiteUsername() != null){
                        white = game.whiteUsername();
                    }
                    if (game.blackUsername() != null){
                        black = game.blackUsername();
                    }
                    uiList = (uiList +
                            "\n" + i + ". " +
                            "\tGame Name: " + game.gameName()) +
                            "\t\t White: " + white +
                            "\tBlack: " + black ;
                }
                return uiList;
            }
        } catch (ResponseException e){
            return e.getMessage();
        }

    }


    private String createGame(String authToken, String... params) {
        if (params.length != 1){
            System.out.println("Invalid Game Name, Please Try Again");
            return help();
        }
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

    private String joinGame(String authToken, String... params) {
        if (params.length != 2){
            System.out.println("Invalid Game Input, Please Try Again");
            return help();
        }

        try {
            setListNumberInterpreter(authToken);
            params[1] = String.valueOf(listNumberInterpreter.get(Integer.parseInt(params[1])));
        } catch (Exception e){}



        try {
            String result = server.joinGame(authToken, params);

            GameData gameInfo = server.getGame(authToken, params[1]);
            DrawBoard.main(gameInfo.game().getBoard(), params[0]);
            if (Objects.equals(result, " Game Joined Successfully ")){
                return " Game Joined Successfully! ";
            } else if(Objects.equals(result, "Invalid Color Given, please try again")){
                return "Invalid Color Given, please try again";
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
