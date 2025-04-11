package client;

import exception.ResponseException;
import model.GameID;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade sf;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + port;
        sf = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() throws ResponseException {
        Assertions.assertTrue(true);
        String authToken = sf.logIn("c", "c").authToken();
        GameID newGameID = sf.createGame(authToken,"calvin12");
        String gameList = sf.listGames(authToken).toString();
        String gameName = sf.getGame(authToken,"1");
        System.out.println(gameName);
    }

}
