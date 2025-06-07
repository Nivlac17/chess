package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import model.GameID;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.ChessService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;


@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private boolean gameOverLogic = false;
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {

        try {

            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            System.out.println(command.toString());
            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = (ChessService.getAuthData(command.getAuthToken())).username();
            connections.addConnection(username, command.getGameID(), session);
            MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
            System.out.println(command.getGameID());
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, moveCommand);

//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);

            }
        } catch (DataAccessException ex) {
            // Serializes and sends the error message

            connections.sendError(session.getRemote(), "Error: unauthorized");


        }
    }


    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket Error for session " + session + ": " + throwable.getMessage());
        throwable.printStackTrace();
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("WebSocket Closed. Code: " + statusCode + ", Reason: " + reason);
    }





    private void connect(Session session, String username, UserGameCommand command) {
        connections.addConnection(username, command.getGameID(), session);
        String view;
        GameData gameData;
        try {
            gameData = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));
        } catch (DataAccessException e) {
            connections.sendError(session.getRemote(), "Error: GameData is unusable 1");
            return;
        }

        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData, null, null);
        try {
            connections.send(serverMessage, username, command.getGameID());
        } catch (IOException e) {
            connections.sendError(session.getRemote(), "Error: GameData is unusable 2");
            return;
        }


        if (gameData.whiteUsername().equals(username)){
            view = "WHITE";
        }else if (gameData.blackUsername().equals(username)){
            view = "BLACK";
        }else {
            view = " an Observer.";
        }
        var message = String.format("%s has joined the game as %s", username, view);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        connections.broadcast(username, notification, command.getGameID());
    }


    private String getColumnLetter(int col) {
        return switch (col) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> "?";
        };
    }




    private void makeMove(Session session, String username, MakeMoveCommand command) {
        if (gameOverLogic){
            connections.sendError(session.getRemote(), "Error: Gave Over");
            return;
        }
        GameData gameData;
        try {
            gameData = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));
        } catch (DataAccessException e) {
            connections.sendError(session.getRemote(), "Error: GameData is unusable");
            return;
        }

        Collection<ChessMove> validMoves = gameData.game().validMoves(command.getMove().getStartPosition());
        ChessMove chessMove = command.getMove();
        int startCol = command.getMove().getStartPosition().getColumn();
        int endCol = command.getMove().getEndPosition().getColumn();

        String startColumn = getColumnLetter( startCol);


        String endColumn = getColumnLetter( endCol);


        ServerMessage gameOver = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, " Game Over ", null);
        if (validMoves.contains(chessMove)){
            try {
//            GameData existingGame = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));
//            existingGame.setGame(gameData.game());
                gameData.game().makeMove(chessMove);
                ChessService.updateGame(command.getAuthToken(), gameData);
            } catch (InvalidMoveException | DataAccessException e) {
                connections.sendError(session.getRemote(), "Error: could not update game or invalid move");
                return;
            }
            //            Broadcast Loaded game to User
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData, null, null);

            try {
                connections.send(serverMessage, username, command.getGameID());
            } catch (IOException e) {
                connections.sendError(session.getRemote(), "Error: could not send Notification");
                return;
            }
            System.out.println("+++++++++++++++++++" );

//            Broadcast loaded game to everyone else
            var gameUpdate = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData, null, null);
            connections.broadcast(username, gameUpdate, command.getGameID());

//            Broadcast notified game to everyone else that black won
            if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                var message = String.format("%s has won the game as BLACK!!",
                        gameData.blackUsername());
                ServerMessage victoryMessage =
                        new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                null, message, null);
                try {
                    connections.send(victoryMessage, username, command.getGameID());
                    connections.send(gameOver, username, command.getGameID());
                } catch (IOException e) {
                    connections.sendError(session.getRemote(), "Error: could not send Notification");
                    return;
                }

                connections.broadcast(username, victoryMessage, command.getGameID());


            }else if(gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                var message =
                        String.format("%s has won the game as WHITE!!", gameData.whiteUsername());
                ServerMessage victoryMessage =
                        new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                null, message, null);
                try {
                    connections.send(victoryMessage, username, command.getGameID());
                    gameOverLogic = true;
                } catch (IOException e) {
                    connections.sendError(session.getRemote(), "Error: could not send Notification");
                    return;
                }

                connections.broadcast(username, victoryMessage, command.getGameID());
                gameOverLogic = true;


            }else {
                var message = String.format("%s has moved %s to position %s",
                        username,
                        startColumn + command.getMove().getStartPosition().getRow(),
                        endColumn + command.getMove().getEndPosition().getRow());
                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                connections.broadcast(username, notification, command.getGameID());
                if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)){
                    var checkMessage = "BLACK is in check";
                    var checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, checkMessage, null);
                    connections.broadcast(username, checkNotification, command.getGameID());
                } else if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)){
                    var checkMessage = "WHITE is in check";
                    var checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, checkMessage, null);
                    connections.broadcast(username, checkNotification, command.getGameID());
                }
            }
        } else{
            connections.sendError(session.getRemote(), "Error: invalid move");

        }
    }
}
