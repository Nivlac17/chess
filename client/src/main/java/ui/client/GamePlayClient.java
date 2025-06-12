package ui.client;

import exception.ResponseException;
import model.GameID;
import ui.ServerFacade;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.io.IOException;
import java.util.Arrays;

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
                case "m", "move", "make" -> makeMove(authToken, params);
                case "r", "redraw" -> redrawBoard(authToken);
                case "resign" -> resign(authToken);
                case "leave" -> leave(authToken);

                case "quit", "q" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public void joinGame(String authToken, GameID gameID) throws ResponseException {
        ws = new WebSocketFacade(serverUrl, notificationHandler, gameID);
        ws.joinGame(authToken, gameID);
    }

    public String makeMove(String authToken, String... params) throws ResponseException, IOException {
        ws.makeMove(authToken, params);
        return "";
    }

    public String redrawBoard(String authToken){
        ws.redrawBoard(authToken);
        return "";
    }

    public String resign(String authToken) throws ResponseException {
        System.out.println("Are you sure you want to resign? Y/N: ");
        String input = new java.util.Scanner(System.in).nextLine();
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        if (cmd.equals("y")||cmd.equals("yes")) {
            ws.resign(authToken);
        }
        return "";
    }

    public String leave(String authToken) throws ResponseException {
        ws.leave(authToken);
        return "quit";
    }




    public String help(){
        return   """
                Options:
                        Highlight legal moves: "hl", "highlight"  ‹position> (e.g. f5)
                        Make a move: "m", "move", "make"    ‹source> ‹destination› ‹optional promotion› (e.g. f5 e4 q)
                        Redraw Chess Board: "r", "redraw"
                        Resign from game: "resign"
                        Leave game: "leave"
                """;

    }
}
