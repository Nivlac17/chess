package ui;

import exception.ResponseException;
import model.AuthData;

import java.util.Arrays;

public class PreLogInClient {

    private String authToken = null;

    private ServerFacade server;
    private String serverUrl;

    public  PreLogInClient(String serverUrl){
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
                case "register","r" -> register(params);
                case "login", "l" -> logIn(params);


                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params){
        try {
            AuthData authData = server.register(params);
            authToken = authData.authToken();
            return ("Successful registration for: " + authData.username());

        } catch (ResponseException e){
            return e.getMessage();
        }

    }

    private String logIn(String... params) {
        try {
            AuthData authData = server.logIn(params);
            authToken = authData.authToken();
            return ("Successful LogIn for: " + authData.username());

        } catch (ResponseException e){
            return e.getMessage();
        }
    }



    public String help(){
     return   """
                        Available commands:
                         Log In as Existing User - "login" , "l" <Username> <Password>
                         Register as a New User  - "register", "r" <Username> <Password> <Email>
                         Print Help Message      - "help", "h"
                         Exit Program            - "quit", "q"
                                
                """;

    }
}
