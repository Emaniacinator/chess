package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.model.AuthData;
import chess.model.GameData;
import org.junit.jupiter.api.*;
import server.GameID;
import server.GameList;
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

        var port = 0;

        server.run(port);

        System.out.println("Started test HTTP server on " + port);

        testFacade = new ServerFacade("http://localhost:8080");

    }


    @BeforeEach
    public void furtherPreparation(){

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


    @Test
    public void joinGameSuccess(){

        String[] userDataInput = {"open", "a", "user"};

        String[] gameInput = {"joinedGame"};

        AuthData loggedInUser = assertDoesNotThrow(() -> testFacade.registerUser(userDataInput));

        GameID returnedID = assertDoesNotThrow(() -> testFacade.createGame(gameInput, loggedInUser));

        String[] joinGameData = {Integer.toString(returnedID.gameID()), "WHITE"};

        GameData joinedGame = assertDoesNotThrow(() -> testFacade.joinGame(joinGameData, loggedInUser));

        assertEquals("open", joinedGame.whiteUsername());

    }


    @Test
    public void joinGameBadColor(){

        String[] inputUserData = {"newUser", "newPasscode", "newEmail"};

        AuthData loggedIn = assertDoesNotThrow(() -> testFacade.registerUser(inputUserData));

        String[] gameString = {"badColor"};

        assertDoesNotThrow(() -> testFacade.createGame(gameString, loggedIn));

        String[] joinGame = {"1", "blue"};

        Exception receivedException = assertThrows(Exception.class, () -> testFacade.joinGame(joinGame, loggedIn));

        assertEquals("Error: blue is not a valid team color. Please try again.", receivedException.getMessage());

    }


    @Test
    public void observeGameSuccess(){

        String[] newUserLogin = {"anotherDude", "anotherPasscode", "gross"};

        AuthData login = assertDoesNotThrow(() -> testFacade.registerUser(newUserLogin));

        String[] newGameData = {"successfulObservation"};

        GameID returnedID = assertDoesNotThrow(() -> testFacade.createGame(newGameData, login));

        String[] observeGame = {Integer.toString(returnedID.gameID())};

        GameData returnedGame = assertDoesNotThrow(() -> testFacade.observeGame(observeGame, login));

        assertSame(GameData.class, returnedGame.getClass());

    }


    @Test
    public void observeGameNoGameExists(){

        String[] anotherNewUser = {"yetAnother", "passcodeAgain", "annoying"};

        AuthData login = assertDoesNotThrow(() -> testFacade.registerUser(anotherNewUser));

        String[] observeGameString = {"1"};

        Exception receivedException = assertThrows(Exception.class, () -> testFacade.observeGame(observeGameString, login));

        assertEquals("Error: The gameID you requested doesn't exist in the database. Please try again.", receivedException.getMessage());

    }


    @Test
    public void listGamesSuccess(){

        String[] fifteen = {"fifteen", "fifteen", "fifteen"};

        AuthData login = assertDoesNotThrow(() -> testFacade.registerUser(fifteen));

        String[] gameOne = {"gameOne"};

        String[] gameTwo = {"gameTwo"};

        String[] gameThree = {"gameThree"};

        assertDoesNotThrow(() -> testFacade.createGame(gameOne, login));

        assertDoesNotThrow(() -> testFacade.createGame(gameTwo, login));

        assertDoesNotThrow(() -> testFacade.createGame(gameThree, login));

        GameList returnedGames = assertDoesNotThrow(() -> testFacade.listGames(login));

        assertEquals(3, returnedGames.games().length);

    }


    @Test
    public void listGamesNoAuthData(){

        String[] finalUser = {"lastUser", "finally", "done!"};

        AuthData login = assertDoesNotThrow(() -> testFacade.registerUser(finalUser));

        String[] firstGame = {"firstGame"};

        String[] secondGame = {"secondGame"};

        String[] thirdGame = {"thirdGame"};

        assertDoesNotThrow(() -> testFacade.createGame(firstGame, login));

        assertDoesNotThrow(() -> testFacade.createGame(secondGame, login));

        assertDoesNotThrow(() -> testFacade.createGame(thirdGame, login));

        Exception receivedException = assertThrows(Exception.class, () -> testFacade.listGames(null));

        assertEquals("Error: Not logged in", receivedException.getMessage());

    }


}
