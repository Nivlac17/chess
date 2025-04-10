package ui.client;

import exception.ResponseException;
import model.AuthData;
import ui.ServerFacade;

import java.util.Arrays;

public class PostLogInClient {

    private ServerFacade server;
    private String serverUrl;

    public  PostLogInClient(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
//        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
//                case "register","r" -> register(params);
//                case "login", "l" -> logIn(params);


                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }





    public String help(){
        return   """
                        Options:
                        List current games:  "l", "list"
                        Create a new game:   "c", "create" ‹GAME NAME>
                        Join a game:         "j", "join" ‹GAME ID> ‹COLOR›
                        Watch a game:        "w", "watch" <GAME ID>
                        Logout:              "logout"
                                
                """;

    }
}
