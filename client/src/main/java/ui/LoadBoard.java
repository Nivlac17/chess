package ui;

import model.GameData;
//import ui.DrawBoard;
//import ui.client.PostLogInClient;
//import websocket.messages.Notification;


public class LoadBoard {
    GameData gameData;
    String color;
    DrawBoard drawBoard = new DrawBoard();


    public void loadBoard(GameData gameData, String color){
        this.gameData = gameData;
        this.color = color;
        drawBoard.draw(gameData.game().getBoard(), color);
    }


}
