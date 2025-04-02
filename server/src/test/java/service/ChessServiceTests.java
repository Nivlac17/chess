package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessServiceTests {

//    @BeforeEach
//    void setupTests() throws DataAccessException {
//       ChessService chessService = new ChessService();
////       chessService.register(new UserData("t","o","m"));
//    }

//    @AfterAll
//    static void stopServer() throws DataAccessException {
//        ChessService.clear();
//    }


    @Test
    @DisplayName("Clear Data Test")
    void clearTest() throws DataAccessException {
        ChessService.register(new UserData("t","o","m"));
        ChessService.clear();
        assertNotNull(ChessService.register(new UserData("t","o","m")));
        ChessService.clear();

    }



    @Test
    @DisplayName("Register User Positive Test")
    void registerPositiveTest() throws DataAccessException {
        UserData testUser = new UserData("Calvin", "Password", "email@email.com");
        AuthData testAuthData = ChessService.register(testUser);
        assertNotNull(testAuthData);
        assertEquals("Calvin", testAuthData.username());
        ChessService.clear();
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

        ChessService.clear();
    }


    @Test
    @DisplayName("Login Positive Test")
    void logInPositiveTest() throws DataAccessException {
        UserData testUser = new UserData("username", "password", "email@email.com");
        ChessService.register(testUser);
        UserData testUserLogIn = new UserData("username", "password",null);
        AuthData authDataActual = ChessService.logIn(testUserLogIn);
        assertEquals( "username",authDataActual.username());
        assertNotNull(authDataActual.authToken());
        ChessService.clear();
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
        ChessService chessService = new ChessService();
        UserData testUser = new UserData("username", "password", "email@email.com");
        AuthData authData = chessService.register(testUser);
        assertEquals("", chessService.logOut(authData.authToken()));
        ChessService.clear();
    }


    @Test
    @DisplayName("LogOut User Negative Test")
    void logOutNegativeTest() throws DataAccessException {
        Exception exception = assertThrows(DataAccessException.class, () -> ChessService.logOut("d234rft"));
        assertEquals("Error: unauthorized", exception.getMessage());
        ChessService.clear();

    }


}
