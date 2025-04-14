package ui.client;

import exception.ResponseException;
import model.GameID;
import ui.ServerFacade;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.HashMap;

public class GamePlayClient {
    private ServerFacade server;
    private String serverUrl;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;






    public  GamePlayClient(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;

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

    public void joinGame(String authToken, GameID gameID) throws ResponseException {
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ws.joinGame(authToken, gameID);
    }





    public String help(){
        return   """
                        Options:
                        Let's play Ball boys!!
                                
                """;

    }
}
