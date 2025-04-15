package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import model.GameID;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
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
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, InvalidMoveException {
        try {

            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            System.out.println(command.toString());
            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = (ChessService.getAuthData(command.getAuthToken())).username();
            connections.addConnection(username, command.getGameID(), session);
            MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, moveCommand);

//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);

            }
        } catch (DataAccessException ex) {
            // Serializes and sends the error message
            connections.sendError(session.getRemote(), new ErrorMessage("Error: unauthorized"));

        }
    }





    private void connect(Session session, String username, UserGameCommand command) throws DataAccessException, IOException {
        connections.addConnection(username, command.getGameID(), session);
        String view;
        GameData gameData = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));

        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData, null, null);
        connections.send(serverMessage, username, command.getGameID());


        if (gameData.whiteUsername().equals(username)){
            view = "WHITE";
        }else if (gameData.blackUsername().equals(username)){
            view = "BLACK";
        }else {
            view = " an Observer.";
        }
        var message = new Notification(Notification.Type.JOIN_GAME, String.format("%s has joined the game as %s", username, view));
        Gson gson = new Gson();

        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, gson.toJson(message), null);
        connections.broadcast(username, notification, command.getGameID());
    }



    private void makeMove(Session session, String username, MakeMoveCommand command) throws DataAccessException, IOException, InvalidMoveException {
        GameData gameData = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));
        Collection<ChessMove> validMoves = gameData.game().validMoves(command.getMove().startPosition);
        ChessMove chessMove = command.getMove();
        int startCol = command.getMove().getStartPosition().getColumn();
        int EndCol = command.getMove().getEndPosition().getColumn();

        String startColumn;
        switch (startCol){
            case 1 -> startColumn = "a";
            case 2 -> startColumn = "b";
            case 3 -> startColumn = "c";
            case 4 -> startColumn = "d";
            case 5 -> startColumn = "e";
            case 6 -> startColumn = "f";
            case 7 -> startColumn = "g";
            case 8 -> startColumn = "h";
            default -> startColumn = "";
        }

        String endColumn;
        switch (EndCol){
            case 1 -> endColumn = "a";
            case 2 -> endColumn = "b";
            case 3 -> endColumn = "c";
            case 4 -> endColumn = "d";
            case 5 -> endColumn = "e";
            case 6 -> endColumn = "f";
            case 7 -> endColumn = "g";
            case 8 -> endColumn = "h";
            default -> endColumn = "";

        }

        if (validMoves.contains(chessMove)){
            gameData.game().makeMove(chessMove);

            GameData existingGame = ChessService.getGame(command.getAuthToken(), new GameID(command.getGameID()));
            existingGame.setGame(gameData.game());
            ChessService.updateGame(command.getAuthToken(), existingGame);
            //            Broadcast Loaded game to User
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData, null, null);

            connections.send(serverMessage, username, command.getGameID());
//            Broadcast loaded game to everyone else
            var gameUpdate = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData, null, null);
            connections.broadcast(username, gameUpdate, command.getGameID());

//            Broadcast notified game to everyone else that black won
            if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                var message = new Notification(Notification.Type.MOVE_MADE,
                        String.format("%s has won the game as BLACK!!",
                        gameData.blackUsername())
                );
                ServerMessage victoryMessage =
                        new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                null, message, null);
                connections.send(victoryMessage, username, command.getGameID());

                connections.broadcast(username, victoryMessage, command.getGameID());

            }else if(gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                var message = new Notification(Notification.Type.MOVE_MADE,
                        String.format("%s has won the game as WHITE!!",
                                gameData.whiteUsername())
                );
                ServerMessage victoryMessage =
                        new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                null, message, null);
                connections.send(victoryMessage, username, command.getGameID());

                connections.broadcast(username, victoryMessage, command.getGameID());

            }else {
                var message = new Notification(Notification.Type.MOVE_MADE, String.format("%s has moved %s to position %s",
                        username,
                        startColumn + command.getMove().getStartPosition().getRow(),
                        endColumn + +command.getMove().getEndPosition().getRow())
                );
                Gson gson = new Gson();
                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, gson.toJson(message), null);
                connections.broadcast(username, notification, command.getGameID());
            }
        }
    }
}