package client;

import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import model.GameID;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


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
    public void registerPositiveTest() throws ResponseException {
        Random rand = new Random();
        int randomInt = rand.nextInt(1000000);
        AuthData authData = sf.register("milly" + randomInt, "password", "email");
        assertNotNull(authData.authToken());
        assertEquals("milly" + randomInt, authData.username());
    }


    @Test
    public void registerNegativeTest() throws ResponseException {
        sf.register("Jdk", "password", "email");
        assertThrows( ResponseException.class, () -> sf.register("Jdk", "password", "email"));
    }

    @Test
    public void logInPositiveTest() throws ResponseException {
        AuthData authData = sf.logIn("Jdk", "password", "email");
        assertNotNull(authData.authToken());
        assertEquals("Jdk", authData.username());
    }

    @Test
    public void logInNegativeTest() throws ResponseException {
        assertThrows( ResponseException.class, () -> sf.logIn("Jdk", "PASSWORD"));
    }

    @Test
    public void listGamesPositiveTest() throws ResponseException {
        AuthData authData = sf.logIn("Jdk", "password");
        assertNotNull(sf.listGames(authData.authToken()));
    }

    @Test
    public void listGamesNegativeTest() throws ResponseException {
        sf.logIn("Jdk", "password");
        assertThrows(NullPointerException.class, () -> sf.listGames("123456yui"));
    }

    @Test
    public void createGamePositiveTest() throws ResponseException {
        AuthData authData = sf.logIn("Jdk", "password");
        GameID gameID = sf.createGame(authData.authToken(), "coolvids");
        assertNotNull(gameID.gameID());
    }


    @Test
    public void createGameNegativeTest() throws ResponseException {
        AuthData authData = sf.logIn("Jdk", "password");
        assertThrows(ResponseException.class, () -> sf.createGame("123456yui", "nameristhiser"));
        assertThrows(ResponseException.class, () -> sf.createGame(authData.authToken(), ""));

    }

    @Test
    public void joinGamePositiveTest() throws ResponseException {
        AuthData authData = sf.logIn("Jdk", "password");
        GameID gameID = sf.createGame(authData.authToken(), "coolvids");
        String gameForTesting = sf.joinGame(authData.authToken(), "white", String.valueOf(gameID.gameID()));
        assertEquals(" Game Joined Successfully ", gameForTesting);
    }


    @Test
    public void joinGameNegativeTest() throws ResponseException {
        AuthData authData = sf.logIn("Jdk", "password");
        GameID gameID = sf.createGame(authData.authToken(), "coolvids");
        String gameForTesting = sf.joinGame(authData.authToken(), "white", String.valueOf(gameID.gameID()));
        assertEquals(" Game Joined Successfully ", gameForTesting);
        assertThrows(ResponseException.class, () -> sf.joinGame(authData.authToken(), "white", String.valueOf(gameID.gameID())));

    }

    @Test
    public void logOutPositiveTest() throws ResponseException {
        AuthData authData = sf.logIn("Jdk", "password");
        assertEquals( sf.logOut(authData.authToken()), " Successful Logout ");
    }

    @Test
    public void logOutNegativeTest() throws ResponseException {
        AuthData authData = sf.logIn("Jdk", "password");
        assertThrows( NullPointerException.class, () -> sf.logOut("asdlfkj;"));
    }


}





//
//        Assertions.assertTrue(true);
//        String authToken = sf.logIn("c", "c").authToken();
//        GameID newGameID = sf.createGame(authToken,"calvin12");
//        String gameList = sf.listGames(authToken).toString();
////        String gameName = sf.getGame(authToken,"1");
////        System.out.println(gameName);
//    }
//
//
//
//
//
//
//
//
//
//
//            logOut
//    getGame
//            updateGame
//
//}
