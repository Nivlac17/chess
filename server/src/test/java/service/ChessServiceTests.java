package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessServiceTests {

//    @BeforeEach
//    void setupTests() throws DataAccessException {
//       ChessService chessService = new ChessService();
//       chessService.register(new UserData("t","o","m"));
//    }


    @Test
    void clearTest() throws DataAccessException {
        ChessService chessService = new ChessService();
        chessService.register(new UserData("t","o","m"));
        chessService.clear();
        assertNotNull(chessService.register(new UserData("t","o","m")));
    }



    @Test
    void registerPosotiveTest() throws DataAccessException {
        UserData testUser = new UserData("Calvin", "Password", "email@email.com");
        AuthData testAuthData = ChessService.register(testUser);
        assertNotNull(testAuthData);
        assertEquals("Calvin", testAuthData.username());

    }

    @Test
    void registerNegativeTest() throws DataAccessException {
        ChessService chessService = new ChessService();


        UserData testUser = new UserData("", "Password", "email@email.com");
        Exception exception = assertThrows(DataAccessException.class, () -> ChessService.register(testUser));
        assertEquals("Error: Incomplete Information!!!", exception.getMessage());

        UserData testUser_1 = new UserData("Username", "", "email@email.com");
        Exception exception_1 = assertThrows(DataAccessException.class, () -> ChessService.register(testUser_1));
        assertEquals("Error: Incomplete Information!!!", exception_1.getMessage());

        UserData testUser_2 = new UserData("Username", "howdy", "email@email.com");
        chessService.register(testUser_2);
        Exception exception_2 = assertThrows(DataAccessException.class, () -> ChessService.register(testUser_2));
        assertEquals("Error: Username Username is taken.", exception_2.getMessage());
//

    }

}
