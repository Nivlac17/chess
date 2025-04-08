package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.JoinGame;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessServiceTests {
    @AfterEach
    void stopServer() throws DataAccessException {
        ChessService.clear();
    }


    @Test
    @DisplayName("Clear Data Test")
    void clearTest() throws DataAccessException {
        ChessService.register(new UserData("t","o","m"));
        ChessService.clear();
        assertNotNull(ChessService.register(new UserData("t","o","m")));

    }



    @Test
    @DisplayName("Register User Positive Test")
    void registerPositiveTest() throws DataAccessException {
        UserData testUser = new UserData("Calvin", "Password", "email@email.com");
        AuthData testAuthData = ChessService.register(testUser);
        assertNotNull(testAuthData);
        assertEquals("Calvin", testAuthData.username());
    }

    @Test
    @DisplayName("Register User Negative Test")
    void registerNegativeTest() throws DataAccessException {
        UserData testUser = new UserData("", "Password", "email@email.com");
        Exception exception = assertThrows(DataAccessException.class, () -> ChessService.register(testUser));
        assertEquals("Error: Incomplete Information!!!", exception.getMessage());

        UserData testUser1 = new UserData("Username", "", "email@email.com");
        Exception exception1 = assertThrows(DataAccessException.class, () -> ChessService.register(testUser1));
        assertEquals("Error: Incomplete Information!!!", exception1.getMessage());

        UserData testUser2 = new UserData("Username", "howdy", "email@email.com");
        ChessService.register(testUser2);
        Exception exception2 = assertThrows(DataAccessException.class, () -> ChessService.register(testUser2));
        assertEquals("Error: Username Username is taken.", exception2.getMessage());

    }


    @Test
    @DisplayName("Login Positive Test")
    void logInPositiveTest() throws DataAccessException {
        UserData testUser = new UserData("usernamekey", "password", "email@email.com");
        ChessService.register(testUser);
        UserData testUserLogIn = new UserData("usernamekey", "password",null);
        AuthData authDataActual = ChessService.logIn(testUserLogIn);
        assertEquals( "usernamekey",authDataActual.username());
        assertNotNull(authDataActual.authToken());
    }


    @Test
    @DisplayName("LogIn User Negative Test")
    void logInNegativeTest() throws DataAccessException {
        UserData testUser = new UserData("username1", "Password1", "email@email.com1");
        ChessService.register(testUser);

        UserData testUserFake = new UserData("stan", "Password", "email@email.com");
        Exception exception = assertThrows(DataAccessException.class, () -> ChessService.logIn(testUserFake));
        assertEquals("Error: Invalid User", exception.getMessage());

        UserData testUserFake1 = new UserData("", "Password", "email@email.com");
        Exception exception1 = assertThrows(DataAccessException.class, () -> ChessService.logIn(testUserFake1));
        assertEquals("Error: Incomplete Information!!!", exception1.getMessage());

        UserData testUserFake2 = new UserData("username1", "fake_password", "email@email.com");
        Exception exception2 = assertThrows(DataAccessException.class, () -> ChessService.logIn(testUserFake2));
        assertEquals("Error: invalid credentials", exception2.getMessage());

        ChessService.clear();

    }



    @Test
    @DisplayName("Logout Positive Test")
    void logOutPositiveTest() throws DataAccessException {
        UserData testUser = new UserData("username", "password", "email@email.com");
        AuthData authData = ChessService.register(testUser);
        assertEquals("", ChessService.logOut(authData.authToken()));
        ChessService.clear();
    }


    @Test
    @DisplayName("LogOut User Negative Test")
    void logOutNegativeTest()  {
        Exception exception = assertThrows(DataAccessException.class, () -> ChessService.logOut("d234rft"));
        assertEquals("Error: Bad data internal server error", exception.getMessage());

    }


    @Test
    @DisplayName("List Games Positive Test")
    void listGamesPositiveTest() throws DataAccessException {
        UserData testUser = new UserData("username", "password", "email@email.com");
        AuthData authDataActual = ChessService.register(testUser);
        ChessService.createGame(authDataActual.authToken(),
                new GameData(1, null,null,"Game 1",null));
        ChessService.createGame(authDataActual.authToken(),
                new GameData(2, null,null,"Game 2",null));
        Object listOfGames = ChessService.listGames(authDataActual.authToken());
        assertEquals(
                "[GameList[gameID=1, whiteUsername=null, blackUsername=null, gameName=Game 1], " +
                        "GameList[gameID=2, whiteUsername=null, blackUsername=null, gameName=Game 2]]",
                listOfGames.toString());
        assertNotNull(authDataActual.authToken());
        ChessService.clear();
    }



    @Test
    @DisplayName("List Games Negative Test")
    void listGamesNegativeTest() throws DataAccessException {
        UserData testUser = new UserData("username", "password", "email@email.com");
        AuthData authDataActual = ChessService.register(testUser);
        ChessService.createGame(authDataActual.authToken(),
                new GameData(1, null,null,"Game 1",null));
        ChessService.createGame(authDataActual.authToken(),
                new GameData(2, null,null,"Game 2",null));
        assertThrows(DataAccessException.class, () -> ChessService.listGames("d234rft"));
        ChessService.clear();

    }



    @Test
    @DisplayName("Create Game Positive Test")
    void createGamePositiveTest() throws DataAccessException {
        UserData testUser = new UserData("username", "password", "email@email.com");
        AuthData authDataActual = ChessService.register(testUser);
        ChessService.createGame(authDataActual.authToken(),
                new GameData(1, null,null,"Game 1",null));
        assertEquals("[GameList[gameID=1, whiteUsername=null, blackUsername=null, gameName=Game 1]]",
                ChessService.listGames(authDataActual.authToken()).toString());
        ChessService.clear();
    }


    @Test
    @DisplayName("Create Game Negative Test")
    void createGameNegativeTest() throws DataAccessException {
        UserData testUser = new UserData("username", "password", "email@email.com");
        AuthData authDataActual = ChessService.register(testUser);
        assertThrows(DataAccessException.class, () ->
                ChessService.createGame("as123sdf",
                        new GameData(1, null,null,"Game 1",null)));
        assertThrows(DataAccessException.class, () ->
                ChessService.createGame(authDataActual.authToken(),
                        new GameData(1, null,null,"",null)));
        ChessService.clear();

    }


    @Test
    @DisplayName("Join Game Positive Test")
    void joinGamePositiveTest() throws DataAccessException {
        UserData testUserW = new UserData("w", "password", "email@email.com");
        UserData testUserB = new UserData("b", "password", "email@email.com");

        AuthData authDataActualW = ChessService.register(testUserW);
        AuthData authDataActualB = ChessService.register(testUserB);

        ChessService.createGame(authDataActualW.authToken(),
                new GameData(1, null,null,"Game 1",null));
        ChessService.joinGame(authDataActualW.authToken(), new JoinGame("WHITE", 1));
        ChessService.joinGame(authDataActualB.authToken(), new JoinGame("BLACK", 1));
        assertEquals("[GameList[gameID=1, whiteUsername=w, blackUsername=b, gameName=Game 1]]",
                ChessService.listGames(authDataActualB.authToken()).toString());
        ChessService.clear();
    }



    @Test
    @DisplayName("Join Game Negative Test")
    void joinGameNegativeTest() throws DataAccessException {
        UserData testUserW = new UserData("w", "password", "email@email.com");
        UserData testUserB = new UserData("b", "password", "email@email.com");

        AuthData authDataActualW = ChessService.register(testUserW);
        AuthData authDataActualB = ChessService.register(testUserB);

        ChessService.createGame(authDataActualW.authToken(),
                new GameData(1, null,null,"Game 1",null));
        ChessService.joinGame(authDataActualW.authToken(), new JoinGame("WHITE", 1));
        assertThrows(DataAccessException.class, () ->
                ChessService.joinGame(authDataActualB.authToken(), new JoinGame("WHITE", 1)));
        assertEquals("[GameList[gameID=1, whiteUsername=w, blackUsername=null, gameName=Game 1]]",
                ChessService.listGames(authDataActualB.authToken()).toString());
    }


}
