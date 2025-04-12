package ui.client;

import ui.ServerFacade;

import java.util.Arrays;
import java.util.HashMap;

public class GamePlayClient {
    private ServerFacade server;
    private String serverUrl;

    public  GamePlayClient(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input, String authToken) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
//                case "l", "list" -> listGames(authToken);

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
                        Let's play Ball boys!!
                                
                """;

    }
}
