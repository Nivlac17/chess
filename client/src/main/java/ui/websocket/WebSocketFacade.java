package ui.websocket;


import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;

import exception.ResponseException;
import model.GameData;
import model.GameID;
import ui.DrawBoard;
import ui.client.PostLogInClient;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;



    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson gson = new Gson();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);

                    if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
                        String json = (String) serverMessage.getServerMessage();
                        Notification notification = gson.fromJson(json, Notification.class);
                        notificationHandler.notify(notification);
                    }else if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
                        String json = String.valueOf(serverMessage.getGame());
                        GameData gameData = gson.fromJson(json, GameData.class);

                        DrawBoard.draw(gameData.game().getBoard(), PostLogInClient.color);
                        System.out.println("halla");
                        System.out.println("halla");


                    }else if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)){

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
            this.session.getBasicRemote().sendText(new Gson().toJson(connectCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }



    public int[] parsePosition(String position) {
        String[] cr = position.split("");
        char rowNumber = position.charAt(1);

        int col = 1;//colLetter - 'a';
        int row = 8 - Character.getNumericValue(rowNumber); // 8 - 7 = 1

        return new int[]{row, col};
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
            if(params.length == 3) {
            piece = parsePromotion(params[2]);
            }else if (params.length != 2){
                throw new ResponseException (500, "server error");
            }
            piece = null;

            int[] start = parsePosition(params[0]);
            int[] end = parsePosition(params[1]);
            ChessPosition startPosition = new ChessPosition(start[0],start[1]);
            ChessPosition endPosition = new ChessPosition(end[0], end[1]);
            ChessMove move = new ChessMove(startPosition,endPosition,piece);
            MakeMoveCommand connectCommand = new MakeMoveCommand( authToken, null, move);

            this.session.getBasicRemote().sendText(new Gson().toJson(connectCommand));
        } catch (IOException ex) {
        throw new ResponseException(500, ex.getMessage());
        }
        }


//    update game:
//            var action = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));


}
