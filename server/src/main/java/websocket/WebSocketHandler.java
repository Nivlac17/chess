package websocket;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
//    private boolean gameOverLogic = false;
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {

            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            String commandType = json.get("commandType").getAsString();
            System.out.println("WebsocketHandler on message: " + commandType);
//            System.out.println("Line -------------------- working till here");

            switch (commandType) {
                case "CONNECT":
                    UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
                    connect(session, command);
                    break;
                case "MAKE_MOVE":
                    MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(session, moveCommand);
                    break;

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






    private void connect(Session session, UserGameCommand command) throws DataAccessException {
        String username = (ChessService.getAuthData(command.getAuthToken())).username();
        connections.addConnection(username, command.getGameID(), session);
        String view;
        GameData gameData;
        System.out.println("Game ID: " + command.getGameID());
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


    private void delayRace(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }



    private void makeMove(Session session, MakeMoveCommand command) throws DataAccessException {
        String username = (ChessService.getAuthData(command.getAuthToken())).username();
        GameData gameData;
        System.out.println("Game data lines: ");
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


        if (validMoves.contains(chessMove)){
            try {
            GameData existingGame = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));
                ChessGame.TeamColor teamColor =
                        gameData.game().getBoard().getPiece(chessMove.getStartPosition()).getTeamColor();
                if( (teamColor == ChessGame.TeamColor.BLACK &&  !gameData.blackUsername().equals(username)) ||
                        (teamColor == ChessGame.TeamColor.WHITE &&  !gameData.whiteUsername().equals(username))) {
                    connections.sendError(session.getRemote(), "Error: Not your turn!");
                    return;
                }
                    existingGame.setGame(gameData.game());
                gameData.game().makeMove(chessMove);
                ChessService.updateGame(command.getAuthToken(), gameData);
            } catch (InvalidMoveException | DataAccessException e) {
                connections.sendError(session.getRemote(), "Error: Not your turn!");
                return;
            }
            //            Broadcast Loaded game to User
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData, null, null);

            try {
                connections.send(serverMessage, username, command.getGameID());
                delayRace();
            } catch (IOException e) {
                connections.sendError(session.getRemote(), "Error:  could not send Notification");
                return;
            }
//            Broadcast loaded game to everyone else
            var gameUpdate = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData, null, null);
            connections.broadcast(username, gameUpdate, command.getGameID());
            delayRace();
            var message = String.format("%s has moved %s to position %s",
                        username,
                        startColumn + command.getMove().getStartPosition().getRow(),
                        endColumn + command.getMove().getEndPosition().getRow());
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
            connections.broadcast(username, notification, command.getGameID());
        } else{
            connections.sendError(session.getRemote(), "Error: invalid move");
        }
    }
}
