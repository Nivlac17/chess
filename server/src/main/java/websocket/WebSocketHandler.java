package websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import model.GameID;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ChessService;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = (ChessService.getAuthData(command.getAuthToken())).username();

            connections.addConnection(username, command.getGameID(), session);
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
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
}