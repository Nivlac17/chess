package websocket;

import websocket.messages.Notification;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
        public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Set<String>> gamePlayersYViewers = new ConcurrentHashMap<>();


    public void addConnection(String username, int gameID, Session session) {
            var connection = new Connection(username, (org.eclipse.jetty.websocket.api.Session) session);
            connections.put(username, connection);
            gamePlayersYViewers.computeIfAbsent(gameID, user -> ConcurrentHashMap.newKeySet()).add(username);

    }

        public void remove(String username) {
            connections.remove(username);
        }

        public void broadcast(String excludeUsername, Notification notification) throws IOException {
            var removeList = new ArrayList<Connection>();
            for (var c : connections.values()) {
                if (c.session.isOpen()) {
                    if (!c.username.equals(excludeUsername)) {
                        c.send(notification.toString());
                    }
                } else {
                    removeList.add(c);
                }
            }

            // Clean up any connections that were left open.
            for (var c : removeList) {
                connections.remove(c.username);
            }
        }




}
