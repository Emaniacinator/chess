package service;

import chess.ChessGame;
import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import services.Service;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {


    private Service serviceTest;

    @BeforeEach
    public void setUp(){
        serviceTest = new Service();
    }

    @Nested
    @DisplayName("registerNewUser Tests")
    class RegisterUserTests{

        @Test
        @DisplayName("No input username")
        public void registerUserNoUsername(){
            UserData testedUser = new UserData ("", "abcde", "AnEmail");
            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.registerNewUser(testedUser));

            assertEquals(400, expectedException.getErrorCode());
            assertEquals("Error: Can't register without a username", expectedException.getMessage());
        }


        @Test
        @DisplayName("No input passcode")
        public void registerUserNoPasscode(){
            UserData testedUser = new UserData ("NamedHuman", "", "AnEmail");
            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.registerNewUser(testedUser));

            assertEquals(400, expectedException.getErrorCode());
            assertEquals("Error: Can't register without a passcode", expectedException.getMessage());
        }


        @Test
        @DisplayName("No input email")
        public void registerUserNoEmail(){
            UserData testedUser = new UserData ("NamedHuman", "abcde", "");
            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.registerNewUser(testedUser));

            assertEquals(400, expectedException.getErrorCode());
            assertEquals("Error: Can't register without an email", expectedException.getMessage());
        }


        @Test
        @DisplayName("Successfully Registered")
        public void registerUserSuccess(){
            UserData testedUser = new UserData ("NamedHuman", "abcde", "AnEmail");
            AuthData returnedData = assertDoesNotThrow(() -> serviceTest.registerNewUser(testedUser));
            assertEquals("NamedHuman", returnedData.username());
        }


        @Test
        @DisplayName("User Already Exists")
        public void registerUserAlreadyExists(){
            UserData testedUser = new UserData ("NamedHuman", "abcde", "AnEmail");
            UserData secondRegister = new UserData ("NamedHuman", "12345", "ADifferentEmail");

            try{
                serviceTest.registerNewUser(testedUser);
            }
            catch(DataAccessException ignored){

            }

            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.registerNewUser(secondRegister));
            assertEquals(403, expectedException.getErrorCode());
            assertEquals("Error: User is already in database", expectedException.getMessage());
        }
    }


    @Nested
    @DisplayName("loginUser Tests")
    class LoginUserTests{


        @BeforeEach
        public void loginSetup(){
            UserData initialUser = new UserData("user", "1234", "email");

            try{
                serviceTest.registerNewUser(initialUser);
            }
            catch(DataAccessException ignored){

            }
        }


        @Test
        @DisplayName ("No input username")
        public void loginUserNoUsername(){
            UserData testedLoginData = new UserData("", "1234", "");
            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.loginUser(testedLoginData));

            assertEquals(500, expectedException.getErrorCode());
            assertEquals("Error: Did not input either username or passcode", expectedException.getMessage());
        }


        @Test
        @DisplayName ("No input passcode")
        public void loginUserNoPasscode(){
            UserData testedLoginData = new UserData("user", "", "");
            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.loginUser(testedLoginData));

            assertEquals(500, expectedException.getErrorCode());
            assertEquals("Error: Did not input either username or passcode", expectedException.getMessage());
        }


        @Test
        @DisplayName ("User doesn't exist")
        public void loginUserNotExist(){
            UserData testedLoginData = new UserData("guccigang", "1234", "");
            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.loginUser(testedLoginData));

            assertEquals(401, expectedException.getErrorCode());
            assertEquals("Error: User is not in database", expectedException.getMessage());
        }


        @Test
        @DisplayName ("Passcode is incorrect")
        public void loginUserIncorrectPasscode(){
            UserData testedLoginData = new UserData("user", "abcde", "");
            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.loginUser(testedLoginData));

            assertEquals(401, expectedException.getErrorCode());
            assertEquals("Error: Incorrect passcode or username", expectedException.getMessage());
        }


        @Test
        @DisplayName("Successfully logged in")
        public void loginUserSuccess(){
            UserData testedLoginData = new UserData("user", "1234", "");
            AuthData testedAuthData = assertDoesNotThrow(() -> serviceTest.loginUser(testedLoginData));
            assertEquals("user", testedAuthData.username());
        }
    }


    @Nested
    @DisplayName("logoutUser Tests")
    class LogoutUserTests{

        AuthData initialData;

        @BeforeEach
        public void logoutSetup(){
            UserData initialUser = new UserData("user", "1234", "email");

            try{
                serviceTest.clearAllDatabases();
                initialData = serviceTest.registerNewUser(initialUser);
            }
            catch(DataAccessException ignored){

            }
        }


        @Test
        @DisplayName("User isn't in database")
        public void logoutUserNotInDatabase(){
            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.logoutUser("apples"));
            assertEquals(401, expectedException.getErrorCode());
            assertEquals("Error: No authorized user in database", expectedException.getMessage());
        }


        @Test
        @DisplayName("No authToken passed in")
        public void logoutUserNoAuthToken(){
            DataAccessException expectedExceptionEmpty = assertThrows(DataAccessException.class, () -> serviceTest.logoutUser(""));
            DataAccessException expectedExceptionNull = assertThrows(DataAccessException.class, () -> serviceTest.logoutUser(null));

            assertEquals(401, expectedExceptionEmpty.getErrorCode());
            assertEquals("Error: Not logged in", expectedExceptionEmpty.getMessage());
            assertEquals(401, expectedExceptionNull.getErrorCode());
            assertEquals("Error: Not logged in", expectedExceptionNull.getMessage());
        }


        @Test
        @DisplayName("Successfully logged out")
        public void logoutUserSuccess(){
            String userToken = initialData.authToken();
            assertDoesNotThrow(() -> serviceTest.logoutUser(userToken));
            assertThrows(DataAccessException.class, ()-> serviceTest.logoutUser(userToken));
        }
    }


    @Nested
    @DisplayName("createGame Tests")
    public class CreateGameTests {

        AuthData initialData;

        @BeforeEach
        public void createGameSetup() {
            UserData initialUser = new UserData("user", "1234", "email");
            try {
                serviceTest.clearAllDatabases();
                initialData = serviceTest.registerNewUser(initialUser);
            }
            catch (DataAccessException ignored) {

            }
        }


        @Test
        @DisplayName("User isn't in database")
        public void createGameUserNotInDatabase() {
            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.createGame("apples", "name"));
            assertEquals(401, expectedException.getErrorCode());
            assertEquals("Error: No authorized user in database", expectedException.getMessage());
        }


        @Test
        @DisplayName("No authToken passed in")
        public void createGameNoAuthToken() {
            DataAccessException expectedExceptionEmpty = assertThrows(DataAccessException.class, () -> serviceTest.createGame("", "name"));
            DataAccessException expectedExceptionNull = assertThrows(DataAccessException.class, () -> serviceTest.createGame(null, "name"));

            assertEquals(401, expectedExceptionEmpty.getErrorCode());
            assertEquals("Error: Not logged in", expectedExceptionEmpty.getMessage());
            assertEquals(401, expectedExceptionNull.getErrorCode());
            assertEquals("Error: Not logged in", expectedExceptionNull.getMessage());
        }

        @Test
        @DisplayName("No gameName passed in")
        public void createGameNoGameName(){
            DataAccessException expectedException = assertThrows(DataAccessException.class,
                    () -> serviceTest.createGame(initialData.authToken(), ""));
            assertEquals(400, expectedException.getErrorCode());
            assertEquals("Error: No game name specified", expectedException.getMessage());
        }


        @Test
        @DisplayName("Successfully created game")
        public void createGameSuccess(){
            GameData resultingGame = assertDoesNotThrow(() -> serviceTest.createGame(initialData.authToken(), "real name"));

            assertEquals(1, resultingGame.gameID());
            assertNull(resultingGame.whiteUsername());
            assertNull(resultingGame.blackUsername());
            assertEquals("real name", resultingGame.gameName());
            assertSame(ChessGame.class, resultingGame.game().getClass());
        }
    }


    @Nested
    @DisplayName("getAllGameData Tests")
    public class GetAllGameDataTests {

        AuthData initialData;

        @BeforeEach
        public void getAllGameDataSetup() {

            UserData initialUser = new UserData("user", "1234", "email");

            try {
                serviceTest.clearAllDatabases();
                initialData = serviceTest.registerNewUser(initialUser);
                serviceTest.createGame(initialData.authToken(), "GameOne");
                serviceTest.createGame(initialData.authToken(), "GameTwo");
                serviceTest.createGame(initialData.authToken(), "GameThree");
                serviceTest.createGame(initialData.authToken(), "GameFour");
            }
            catch (DataAccessException ignored) {

            }
        }


        @Test
        @DisplayName("User isn't in database")
        public void getAllGameDataUserNotInDatabase() {
            DataAccessException expectedException = assertThrows(DataAccessException.class, () -> serviceTest.getAllGameData("apples"));

            assertEquals(401, expectedException.getErrorCode());
            assertEquals("Error: No authorized user in database", expectedException.getMessage());
        }


        @Test
        @DisplayName("No authToken passed in")
        public void getAllGameDataNoAuthToken() {
            DataAccessException expectedExceptionEmpty = assertThrows(DataAccessException.class, () -> serviceTest.getAllGameData(""));
            DataAccessException expectedExceptionNull = assertThrows(DataAccessException.class, () -> serviceTest.getAllGameData(null));

            assertEquals(401, expectedExceptionEmpty.getErrorCode());
            assertEquals("Error: Not logged in", expectedExceptionEmpty.getMessage());
            assertEquals(401, expectedExceptionNull.getErrorCode());
            assertEquals("Error: Not logged in", expectedExceptionNull.getMessage());
        }


        @Test
        @DisplayName("Successfully got all game data")
        public void getAllGameDataSuccess(){
            GameData[] gameList = assertDoesNotThrow(() -> serviceTest.getAllGameData(initialData.authToken()));

            assertEquals(4, gameList.length);
            assertEquals("GameOne", gameList[0].gameName());
            assertEquals("GameTwo", gameList[1].gameName());
            assertEquals("GameThree", gameList[2].gameName());
            assertEquals("GameFour", gameList[3].gameName());
        }

    }


    @Nested
    @DisplayName("joinGame Tests")
    public class JoinGameTests {

        AuthData initialData;

        @BeforeEach
        public void joinGameSetup() {

            UserData initialUser = new UserData("user", "1234", "email");

            try {
                serviceTest.clearAllDatabases();
                initialData = serviceTest.registerNewUser(initialUser);
                serviceTest.createGame(initialData.authToken(), "game");
            }
            catch (DataAccessException ignored) {

            }
        }


        @Test
        @DisplayName("User isn't in database")
        public void joinGameUserNotInDatabase() {
            DataAccessException expectedException = assertThrows(DataAccessException.class,
                    () -> serviceTest.joinGame("apples", ChessGame.TeamColor.WHITE, 1));
            assertEquals(401, expectedException.getErrorCode());
            assertEquals("Error: No authorized user in database", expectedException.getMessage());
        }


        @Test
        @DisplayName("No authToken passed in")
        public void joinGameNoAuthToken() {
            DataAccessException expectedExceptionEmpty = assertThrows(DataAccessException.class,
                    () -> serviceTest.joinGame("", ChessGame.TeamColor.WHITE, 1));
            DataAccessException expectedExceptionNull = assertThrows(DataAccessException.class,
                    () -> serviceTest.joinGame(null, ChessGame.TeamColor.WHITE, 1));
            assertEquals(401, expectedExceptionEmpty.getErrorCode());
            assertEquals("Error: Not logged in", expectedExceptionEmpty.getMessage());
            assertEquals(401, expectedExceptionNull.getErrorCode());
            assertEquals("Error: Not logged in", expectedExceptionNull.getMessage());
        }


        @Test
        @DisplayName("No valid gameID passed in")
        public void joinGameNoGameID(){
            DataAccessException expectedExceptionNegative = assertThrows(DataAccessException.class,
                    () -> serviceTest.joinGame(initialData.authToken(), ChessGame.TeamColor.WHITE, -1));
            assertEquals(400, expectedExceptionNegative.getErrorCode());
            assertEquals("Error: Input an invalid gameID or playerColor", expectedExceptionNegative.getMessage());
        }


        @Test
        @DisplayName("No game in database")
        public void joinGameNoGameInDatabase(){
            DataAccessException expectedException = assertThrows(DataAccessException.class,
                    () -> serviceTest.joinGame(initialData.authToken(), ChessGame.TeamColor.WHITE, 7));
            assertEquals(400, expectedException.getErrorCode());
            assertEquals("Error: No game with that ID in database", expectedException.getMessage());
        }


        @Test
        @DisplayName("No team color passed in")
        public void joinGameNoTeamColor(){
            DataAccessException expectedException = assertThrows(DataAccessException.class,
                    () -> serviceTest.joinGame(initialData.authToken(), null, 1));
            assertEquals(400, expectedException.getErrorCode());
            assertEquals("Error: Input an invalid gameID or playerColor", expectedException.getMessage());
        }


        @Test
        @DisplayName("Successfully joined game")
        public void joinGameSuccess(){
            assertDoesNotThrow(() -> serviceTest.joinGame(initialData.authToken(), ChessGame.TeamColor.WHITE, 1));
            GameData[] retrievedGame = assertDoesNotThrow(() -> serviceTest.getAllGameData(initialData.authToken()));
            assertEquals(initialData.username(), retrievedGame[0].whiteUsername());
        }


        @Test
        @DisplayName("White team already taken")
        public void joinGameWhiteAlreadyTaken(){
            assertDoesNotThrow(() -> serviceTest.joinGame(initialData.authToken(), ChessGame.TeamColor.WHITE, 1));
            DataAccessException expectedException = assertThrows(DataAccessException.class,
                    () -> serviceTest.joinGame(initialData.authToken(), ChessGame.TeamColor.WHITE, 1));
            assertEquals(403, expectedException.getErrorCode());
            assertEquals("Error: There is already a user in that game for the WHITE team", expectedException.getMessage());
        }


        @Test
        @DisplayName("Black team already taken")
        public void joinGameBlackAlreadyTaken(){
            assertDoesNotThrow(() -> serviceTest.joinGame(initialData.authToken(), ChessGame.TeamColor.BLACK, 1));
            DataAccessException expectedException = assertThrows(DataAccessException.class, () ->
                    serviceTest.joinGame(initialData.authToken(), ChessGame.TeamColor.BLACK, 1));

            assertEquals(403, expectedException.getErrorCode());
            assertEquals("Error: There is already a user in that game for the BLACK team", expectedException.getMessage());
        }
    }


    @Nested
    @DisplayName("clearAllDatabases tests")
    class ClearAllDatabasesTests{

        AuthData initialData;

        @BeforeEach
        public void joinGameSetup() {

            UserData initialUser;

            try {
                serviceTest.clearAllDatabases();
                initialUser = new UserData("test", "12345", "email@email.com");
                initialData = serviceTest.registerNewUser(initialUser);
                serviceTest.createGame(initialData.authToken(), "game");
            }
            catch (DataAccessException ignored) {

            }
        }


        @Test
        @DisplayName("clearAllDatabases success")
        public void clearAllDatabasesSuccess(){

            assertDoesNotThrow(() -> serviceTest.clearAllDatabases());

            try{
                UserData initialUser = new UserData("user", "1234", "email");
                initialData = serviceTest.registerNewUser(initialUser);
            }
            catch (DataAccessException ignored) {

            }

            GameData[] retrievedGame = assertDoesNotThrow(() -> serviceTest.getAllGameData(initialData.authToken()));

            GameData[] emptyList = {};

            assertEquals(emptyList.length, retrievedGame.length);
        }
    }
}
