package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import ui.ChessClient;

import static chess.ChessGame.TeamColor.*;
import static org.junit.jupiter.api.Assertions.*;
import static ui.EscapeSequences.*;


public class ServerFacadeTests {

    private static Server server;

    private static ServerFacade testFacade;


    @BeforeAll
    public static void init() {

        server = new Server();

        var port = 8080;

        server.run(port);

        System.out.println("Started test HTTP server on " + port);

        testFacade = new ServerFacade("http://localhost:8080");

        try {

            server.helperClearDatabases();

        }

        catch(Exception impossible){

            System.out.println("To be honest, I'm not sure how you got here,");

        }

    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testUserRegisterSuccess(){

        String[] userDataInputs = {"user", "user", "user"};

        assertDoesNotThrow(() -> testFacade.registerUser(userDataInputs));

    }


    @Test
    public void testUserRegisterAlreadyExists(){

        String[] userDataInputs = {"duplicate", "user", "user"};

        assertDoesNotThrow(() -> testFacade.registerUser(userDataInputs));

        String[] duplicateUserDataInputs = {"duplicate", "again", "oops"};

        Exception receivedException = assertThrows(Exception.class, () -> testFacade.registerUser(duplicateUserDataInputs));

        assertEquals("Error: User is already in database", receivedException.getMessage());

    }


    @Test
    public void testUserLoginSuccess(){

        String[] makeUser = {"Jiminy", "Cricket", "Email"};

        assertDoesNotThrow(() -> testFacade.registerUser(makeUser));

        String[] loginHelp = {"Jiminy", "Cricket"};

        assertDoesNotThrow(() -> testFacade.loginUser(loginHelp));

    }


    @Test
    public void testUserLoginNoExistingUser(){

        String[] inputStringList = {"sasquatch", "apple"};

        Exception receivedException = assertThrows(Exception.class, () -> testFacade.loginUser(inputStringList));

        assertEquals("Error: User is not in database", receivedException.getMessage());

    }


    @Test
    public void logoutUserSuccess(){

        String[] userDataInputs = {"test", "user", "four"};

        AuthData registeredAuthData = assertDoesNotThrow(() -> testFacade.registerUser(userDataInputs));

        assertDoesNotThrow(() -> testFacade.logoutUser(registeredAuthData));

    }


    @Test
    public void logoutUserNotLoggedIn(){

        AuthData invalidAuthData = new AuthData("fake", "user");

        Exception receivedException = assertThrows(Exception.class, () -> testFacade.logoutUser(invalidAuthData));

        assertEquals("Error: No authorized user in database", receivedException.getMessage());

    }


    @Test
    public void createGameSuccess(){

        String[] userDataInputs = {"another", "test", "user"};

        AuthData registeredAuthData = assertDoesNotThrow(() -> testFacade.registerUser(userDataInputs));

        String[] newGameInput = {"aBoringName"};

        assertDoesNotThrow(() -> testFacade.createGame(newGameInput, registeredAuthData));

    }


    @Test
    public void createGameNoAuthData(){

        String[] newGameInput = {"aName"};

        Exception receivedException = assertThrows(Exception.class, () -> testFacade.createGame(newGameInput, null));

        assertEquals("Error: Not logged in", receivedException.getMessage());

    }


}
