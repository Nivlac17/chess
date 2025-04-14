import chess.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import spark.Spark;
import websocket.WebSocketHandler;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);


        int port = 8080;
        server.Server server8080 = new server.Server();
        server8080.run(port);
        System.out.println("♕ Chess Server Running on port: " + port);


    }
}