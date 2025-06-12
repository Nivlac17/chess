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
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;



@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final Object resignLock = new Object();
    private final Object moveLock = new Object();
    private final Object connectLock = new Object();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {

            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            String commandType = json.get("commandType").getAsString();
            System.out.println("WebsocketHandler on message: " + commandType);

            switch (commandType) {
                case "CONNECT":
                    UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
                    connect(session, command);
                    break;
                case "MAKE_MOVE":
                    MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(session, moveCommand);
                    break;
                case "LEAVE":
                    UserGameCommand leaveCommand = new Gson().fromJson(message, UserGameCommand.class);
                    leaveGame(leaveCommand);
                    break;
                case "RESIGN":
                    UserGameCommand resignCommand = new Gson().fromJson(message, UserGameCommand.class);
                    resign(session, resignCommand);
                    break;
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
        synchronized (connectLock) {
            connections.resigned.put(command.getGameID(), false);
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
            if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)) {
                view = "BLACK";
            } else if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
                view = "WHITE";
            } else {
                view = " an Observer.";
            }
            var message = String.format("%s has joined the game as %s", username, view);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
            connections.broadcast(username, notification, command.getGameID());
        }
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
        synchronized (moveLock) {

            System.out.println("Making Move");
            synchronized (connections) {
                String username = (ChessService.getAuthData(command.getAuthToken())).username();
                GameData gameData;
                try {
                    gameData = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));
                } catch (DataAccessException e) {
                    connections.sendError(session.getRemote(), "Error: GameData is unusable");
                    return;
                }
                Collection<ChessMove> validMoves = gameData.game().validMoves(command.getMove().getStartPosition());
                Boolean resigned = connections.resigned.get(command.getGameID());
                if (resigned) {
                    validMoves = new ArrayList<>();
                }
                ChessMove chessMove = command.getMove();
                int startCol = command.getMove().getStartPosition().getColumn();
                int endCol = command.getMove().getEndPosition().getColumn();
                String[] columns = {getColumnLetter(startCol), getColumnLetter(endCol)};

                if (validMoves.contains(chessMove)) {
                    updateGameWMove(username, command, chessMove, session, gameData, columns);
                } else {
                    connections.sendError(session.getRemote(), "Error: invalid move");
                }
            }
        }
    }

    private void updateGameWMove(String username, MakeMoveCommand command, ChessMove chessMove,
                                 Session session, GameData gameData, String[] columns){
        try {
            GameData existingGame = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));
            ChessGame.TeamColor teamColor =
                    gameData.game().getBoard().getPiece(chessMove.getStartPosition()).getTeamColor();
            if ((teamColor == ChessGame.TeamColor.WHITE && !gameData.whiteUsername().equals(username)) ||
                    (teamColor == ChessGame.TeamColor.BLACK && !gameData.blackUsername().equals(username))) {
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
                columns[0] + command.getMove().getStartPosition().getRow(),
                columns[1] + command.getMove().getEndPosition().getRow());
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        connections.broadcast(username, notification, command.getGameID());

        if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
            var checkmateMessage = "!!Checkmate!! \n BLACK Wins!!!";
            notifyEveryone(username, session, command, checkmateMessage);
        } else if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            var checkmateMessage = "!!Checkmate!! \n WHITE Wins!!!";
            notifyEveryone(username, session, command, checkmateMessage);
        } else if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)) {
            var checkMessage = "WHITE is in Check.";
            notifyEveryone(username, session, command, checkMessage);
        } else if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
            var checkMessage = "BLACK is in Check.";
            notifyEveryone(username, session, command, checkMessage);
        }
    }

    private void notifyEveryone(String username, Session session, UserGameCommand command, String message){
        var checkmateNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        try {
            connections.send(checkmateNotification, username, command.getGameID());
            connections.broadcast(username, checkmateNotification, command.getGameID());
        } catch (IOException e) {
            connections.sendError(session.getRemote(), "Error:  could not send Notification 2257");
        }
    }

    private void resign(Session session, UserGameCommand command) throws DataAccessException {
        synchronized (resignLock) {

            if (!connections.resigned.get(command.getGameID())) {
                System.out.println("resign log -- resign was called");
                String username = (ChessService.getAuthData(command.getAuthToken())).username();
                String victor;
                GameData gameData = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));
                if (gameData.whiteUsername().equals(username)) {
                    System.out.println("resign log -- white resign");
                    connections.resigned.put(command.getGameID(), true);
                    victor = "BLACK";
                    var resignMessage = String.format("%s has resigned%n %s Wins!",
                            username, victor);
                    notifyEveryone(username, session, command, resignMessage);
                } else if (gameData.blackUsername().equals(username)) {
                    System.out.println("resign log -- black resign");
                    connections.resigned.put(command.getGameID(), true);
                    victor = "WHITE";
                    var resignMessage = String.format("%s has resigned%n %s Wins!",
                            username, victor);
                    notifyEveryone(username, session, command, resignMessage);
                } else {
                    connections.sendError(session.getRemote(), "Error:  You Are an Observer, NOT a Playa!");
                }
            } else {
                connections.sendError(session.getRemote(), "Error:  User Already Resigned");
            }
        }
    }

    private void leaveGame(UserGameCommand command) throws DataAccessException {
        synchronized (connections) {
            String username = (ChessService.getAuthData(command.getAuthToken())).username();
        GameData existingGame = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));
        var message = username + " left the game" ;
            if (username.equals(existingGame.whiteUsername())) {
                System.out.println("white username leaving");
                GameData gameData = new GameData(existingGame.gameID(), "game", null, existingGame.gameName(), existingGame.game());
                ChessService.updateGameWhiteUsername(command.getAuthToken(), username, gameData.gameID());
                var checkmateNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                connections.broadcast(username, checkmateNotification, command.getGameID());
                connections.removePlayer(command.getGameID(), username);

            } else if (username.equals(existingGame.blackUsername())) {
                System.out.println("black username leaving");

                GameData gameData = new GameData(existingGame.gameID(), null, "game", existingGame.gameName(), existingGame.game());
                ChessService.updateGameBlackUsername(command.getAuthToken(), username, gameData.gameID());
                var checkmateNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                connections.broadcast(username, checkmateNotification, command.getGameID());
                connections.removePlayer(command.getGameID(), username);

            } else {
                System.out.println("observer username leaving");
                var checkmateNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
                connections.broadcast(username, checkmateNotification, command.getGameID());
                connections.removePlayer(command.getGameID(), username);

            }
        }
    }

}
