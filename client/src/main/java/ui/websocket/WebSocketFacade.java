package ui.websocket;


import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;

import exception.ResponseException;
import model.GameData;
import model.GameID;
import ui.DrawBoard;
import ui.LoadBoard;
import ui.client.PostLogInClient;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;
    LoadBoard board;
    GameID gameID;




    public WebSocketFacade(String url, NotificationHandler notificationHandler, GameID gameID) throws ResponseException {
        try {
            this.gameID = gameID;
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson gson = new Gson();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    System.out.println("message type: " + serverMessage.getServerMessageType());
                    if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
                        System.out.println("---------7777777");
                        String notification = (String) serverMessage.getServerMessage();
                        notificationHandler.notify(notification);
                    }else if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
                        System.out.println("---------1234567");
                        GameData gameData = serverMessage.getGame();
                        board = new LoadBoard();
                        board.loadBoard(gameData, PostLogInClient.color);
                    }else if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)){
                        System.out.println("----------555555555555");
                        System.out.println(serverMessage.getErrorMessage());
                    }
                }
            });



        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("WebSocket connection opened -- written by calvin.. me.. delete this comment");

    }



    public void joinGame(String authToken, GameID gameID) throws ResponseException {
        try {
            UserGameCommand connectCommand = new UserGameCommand(
                    UserGameCommand.CommandType.CONNECT, authToken, gameID.gameID());
            String json = new Gson().toJson(connectCommand);
            this.session.getBasicRemote().sendText(json);
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }



    public int[] parsePosition(String position) {
        String[] cr = position.split("");
        int rowNumber = Integer.parseInt(cr[1]);
        String column2 = cr[0];
        int column;
        switch (column2) {
            case "a" -> column = 1;
            case "b" -> column = 2;
            case "c" -> column = 3;
            case "d" -> column = 4;
            case "e" -> column = 5;
            case "f" -> column = 6;
            case "g" -> column = 7;
            case "h" -> column = 8;
            default -> column = 0;
        }




        return new int[]{rowNumber, column};
    }

    public ChessPiece.PieceType parsePromotion(String promotion){
        return switch (promotion) {
            case "q", "queen" -> ChessPiece.PieceType.QUEEN;
            case "b", "bishop" ->  ChessPiece.PieceType.BISHOP;
            case "r", "rook" -> ChessPiece.PieceType.ROOK;
            case "k", "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> null;

        };

    }

    public void makeMove(String authToken, String... params) throws ResponseException, IOException {
        try{
            ChessPiece.PieceType piece;
            piece = null;
            if(params.length == 3) {
                piece = parsePromotion(params[2]);
            }else if (params.length != 2){
                throw new ResponseException (500, "server error");
            }

            int[] start = parsePosition(params[0]);
            int[] end = parsePosition(params[1]);
            ChessPosition startPosition = new ChessPosition(start[0],start[1]);
            ChessPosition endPosition = new ChessPosition(end[0], end[1]);
            ChessMove move = new ChessMove(startPosition,endPosition,piece);
            MakeMoveCommand connectCommand = new MakeMoveCommand( authToken, gameID.gameID(), move);

            this.session.getBasicRemote().sendText(new Gson().toJson(connectCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    public void redrawBoard(String authToken){
        board.redrawBoard();
    }

    public void resign(String authToken) throws ResponseException {
        System.out.println("This is a print statement in resign board");
        try {
            UserGameCommand connectCommand = new UserGameCommand(
                    UserGameCommand.CommandType.RESIGN, authToken, gameID.gameID());
            String json = new Gson().toJson(connectCommand);
            this.session.getBasicRemote().sendText(json);
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
