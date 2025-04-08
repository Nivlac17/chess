package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import service.ChessService;

import static org.junit.jupiter.api.Assertions.*;


public class MySQLDataAccessMethodsTest {
    private static MySQLDataAccessMethods dataAccessMethods;

    @BeforeAll
    static void setup() {
         dataAccessMethods = new MySQLDataAccessMethods();
    }


    @AfterEach
    void stopServer() throws DataAccessException {
        dataAccessMethods.clear();
    }

    @Test
    @DisplayName("Clear Data Test")
    @Order(1)
    void clearTest() throws DataAccessException {
        dataAccessMethods.createUser(new UserData("username", "password", "email@email.com"));
        dataAccessMethods.createGame(1, "Catch-Phrase!!!", new ChessGame());
        dataAccessMethods.createAuth(new AuthData("1234","Calvin"));
        dataAccessMethods.clear();
        assertNull(dataAccessMethods.getUser("username"));
        assertNull(dataAccessMethods.getGame(1));
        assertThrows(DataAccessException.class, () -> dataAccessMethods.getAuth("1234"));
    }


    @Test
    @DisplayName("Get User Positive Test")
    @Order(2)
    void getUserPositiveTest () throws DataAccessException {
        UserData username = new UserData("username1", "password", "email@email.com");
        UserData b = new UserData("b", "c", "d");

        dataAccessMethods.createUser(username);
        dataAccessMethods.createUser(b);
        UserData result = dataAccessMethods.getUser("username1");


        assertTrue(BCrypt.checkpw("password",result.password()));
        assertEquals(result.username(), username.username());
        assertEquals(dataAccessMethods.getUser("b").email(), b.email());
    }

    @Test
    @DisplayName("Get User Negative Test")
    @Order(3)
    void getUserNegativeTest () throws DataAccessException {
        UserData a = new UserData("a", "password", "email@email.com");
        UserData b = new UserData("b", "c", "d");

        dataAccessMethods.createUser(a);
        dataAccessMethods.createUser(b);
        assertNull(dataAccessMethods.getUser("asdf"));
        assertNull(dataAccessMethods.getUser(""));
    }

    @Test
    @DisplayName("Create User Positive Test")
    @Order(4)
    void createUserPositiveTest () throws DataAccessException {
        UserData a = new UserData("a", "password", "email@email.com");
        UserData b = new UserData("b", "c", "d");

        dataAccessMethods.createUser(a);
        dataAccessMethods.createUser(b);

        UserData result = dataAccessMethods.getUser("b");


        assertTrue(BCrypt.checkpw("c",result.password()));
        assertEquals(result.username(), b.username());
        assertEquals(dataAccessMethods.getUser("b").email(), b.email());
    }


    @Test
    @DisplayName("Create User Negative Test")
    @Order(5)
    void createUserNegativeTest () throws DataAccessException {
        UserData a = new UserData("a", null, "email@email.com");
        UserData b = new UserData("b", "c", "d");

        assertThrows(DataAccessException.class, () -> dataAccessMethods.createUser(a));
        dataAccessMethods.createUser(b);

    }


    @Test
    @DisplayName("Create AuthData Positive Test")
    @Order(6)
    void createAuthPositiveTest () throws DataAccessException {
        AuthData a = new AuthData("abcd1234", "cal");
        UserData b = new UserData("cal", "c", "d");

        dataAccessMethods.createUser(b);
        dataAccessMethods.createAuth(a);


        AuthData result = dataAccessMethods.getAuth("abcd1234");

        assertEquals(result.username(), a.username());
        assertNotNull(dataAccessMethods.getAuth("abcd1234"));
    }


    @Test
    @DisplayName("Create Auth Negative Test")
    @Order(7)
    void createAuthNegativeTest () throws DataAccessException {
        AuthData a = new AuthData(null, "cal");
        assertThrows(DataAccessException.class, () -> dataAccessMethods.createAuth(a));
    }


    @Test
    @DisplayName("Get AuthData Positive Test")
    @Order(8)
    void getAuthPositiveTest () throws DataAccessException {
        AuthData a = new AuthData("abcd1234", "cal");
        AuthData c = new AuthData("cal", "c");

        dataAccessMethods.createAuth(c);
        dataAccessMethods.createAuth(a);

        AuthData result = dataAccessMethods.getAuth("abcd1234");
        assertEquals(result.username(), a.username());

        AuthData result2 = dataAccessMethods.getAuth("cal");
        assertEquals(result2.username(), c.username());

    }


    @Test
    @DisplayName("Get AuthData Negative Test")
    @Order(9)
    void getAuthNegativeTest () throws DataAccessException {
        AuthData a = new AuthData("cal1", "cal");
        dataAccessMethods.createAuth(a);
        assertThrows(DataAccessException.class, () -> dataAccessMethods.getAuth("vgh"));
    }



    @Test
    @DisplayName("Delete AuthData Positive Test")
    @Order(10)
    void deleteAuthPositiveTest () throws DataAccessException {
        AuthData a = new AuthData("abcd1234", "cal");
        AuthData c = new AuthData("cal", "c");

        dataAccessMethods.createAuth(c);
        dataAccessMethods.createAuth(a);
        assertNotNull(dataAccessMethods.getAuth(c.authToken()));
        dataAccessMethods.deleteAuth(c.authToken());
        dataAccessMethods.deleteAuth(a.authToken());
        assertThrows(DataAccessException.class, () -> dataAccessMethods.getAuth(c.username()));
        assertThrows(DataAccessException.class, () -> dataAccessMethods.getAuth(a.username()));

    }


    @Test
    @DisplayName("Delete AuthData Positive Test")
    @Order(11)
    void deleteAuthNegativeTest () throws DataAccessException {
        AuthData a = new AuthData("abcd1234", "cal");

        dataAccessMethods.createAuth(a);
        dataAccessMethods.deleteAuth(a.authToken());
        assertThrows(DataAccessException.class, () -> dataAccessMethods.deleteAuth(""));

    }


    @Test
    @DisplayName("List Games Positive Test")
    @Order(12)
    void listGamesPositiveTest () throws DataAccessException {
        GameData game1 = new GameData(1,"Joe", "Fred", "game1", new ChessGame());
        GameData uprising = new GameData(2,"Frodo", "Sam", "1 game to rule them all", new ChessGame());


        dataAccessMethods.createGame(game1.gameID(), game1.gameName(), game1.game());
        dataAccessMethods.createGame(uprising.gameID(), uprising.gameName(), uprising.game());
        assertEquals("[GameList[gameID=1, whiteUsername=null, blackUsername=null, gameName=game1], " +
                        "GameList[gameID=2, whiteUsername=null, blackUsername=null, gameName=1 game to rule them all]]",
                dataAccessMethods.listGames().toString());

    }




    @Test
    @DisplayName("List Games Negative Test")
    @Order(13)
    void listGamesNegativeTest () throws DataAccessException {
        assertEquals("[]", dataAccessMethods.listGames().toString());
    }

    @Test
    @DisplayName("Create Game Positive Test")
    @Order(14)
    void createGameNegativeTest () throws DataAccessException {
        ChessGame game = new ChessGame();
        dataAccessMethods.createGame(1,"name", game);
        GameData chessGameNamedName = dataAccessMethods.getGame(1);
        GameData expected = new GameData(1,"null", "null", "name", game);
        assertEquals(expected.gameID(),chessGameNamedName.gameID());
        assertNull(chessGameNamedName.whiteUsername());
        assertNull(chessGameNamedName.blackUsername());
        assertEquals(expected.gameName(),chessGameNamedName.gameName());
        assertEquals(game.getBoard().toString(), chessGameNamedName.game().getBoard().toString());
        assertEquals(game.getTeamTurn(), chessGameNamedName.game().getTeamTurn());
        System.out.println(chessGameNamedName.game().getBoard().toString());
    }


}
