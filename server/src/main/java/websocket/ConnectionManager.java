package websocket;

import com.google.gson.Gson;
import model.GameData;
import websocket.messages.Notification;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection> > connections = new ConcurrentHashMap<>();


    public void addConnection(String username, int gameID, Session session) {
            var connection = new Connection(username, session);
            connections.computeIfAbsent(gameID, id -> new ConcurrentHashMap<>()).put(username, connection);

    }

        public void remove(int gameID) {
            connections.remove(gameID);
        }

        public void broadcast(String excludeUsername, ServerMessage notification, int gameID){
            var removeList = new ArrayList<String>();
            var connectionList = connections.get(gameID);
            if(connectionList == null){
                System.out.println("I'm dying!!!!!");
                return;
            }
            for (var entry : connectionList.entrySet()) {
                String username = entry.getKey();
                System.out.println(username);
                Connection connection = entry.getValue();

                if (!username.equals(excludeUsername)) {
                    try {
                        Gson gson = new Gson();
                        connection.send(gson.toJson(notification));
                    } catch (IOException e) {
                        removeList.add(username);
                    }
                }

            }
            for (String userToRemove : removeList) {
                connectionList.remove(userToRemove);
            }
            if (connectionList.isEmpty()) {
                remove(gameID);
            }
        }


    public void send(ServerMessage serverMessage, GameData gameData, String username, int gameID) throws IOException {
        Gson gson = new Gson();
        System.out.println(serverMessage);
        String game = gson.toJson(serverMessage);

        var connectionList = connections.get(gameID);
        var connection = connectionList.get(username);



        connection.send(game);
    }
}
