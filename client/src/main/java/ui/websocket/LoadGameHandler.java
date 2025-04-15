package ui.websocket;

import model.GameData;
import websocket.messages.Notification;

public interface LoadGameHandler {
    void drawBoard(GameData gameData);
}
