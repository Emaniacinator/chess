package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mindrot.jbcrypt.BCrypt;
import server.Server;
import services.Service;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlDataAccessTests {


    private DataAccessFramework getDataAccessType(Class<? extends DataAccessFramework> specificDatabase){

        if (specificDatabase.equals(GeneralDataAccess.class)){

            GeneralDataAccess newDatabase = new GeneralDataAccess();
            newDatabase.deleteAllData();
            return newDatabase;

        }

        else if (specificDatabase.equals(MySqlDataAccess.class)){

            try{

                MySqlDataAccess newDatabase = new MySqlDataAccess();
                newDatabase.deleteAllData();
                return newDatabase;

            }

            catch(DataAccessException exception){

                System.out.println("How'd you even get this exception?");
                System.out.println(exception.toString());
                return null;

            }

        }

        System.out.println("How'd you even get here?");
        return null;

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void addAuthDataNoUsername(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        DataAccessException exception = assertThrows(DataAccessException.class, () -> dataAccess.addAuthData(""));

        assertEquals(500, exception.getErrorCode());
        assertEquals("No valid username to make AuthData for", exception.getMessage());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void addAuthDataSuccess(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        String testedUsername = "username";
        AuthData testedAuthorization = assertDoesNotThrow(() -> dataAccess.addAuthData(testedUsername));

        assertEquals(testedUsername, testedAuthorization.username());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void addUserDataTestEmptyPassword(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        UserData testedUser = new UserData ("username", "", "AnEmail");
        DataAccessException expectedException = assertThrows(DataAccessException.class, () -> dataAccess.addUserData(testedUser));

        assertEquals(400, expectedException.getErrorCode());
        assertEquals("Error: Can't register without a passcode", expectedException.getMessage());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void addUserDataTestSuccess(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        UserData testedUser = new UserData ("username", "abcde", "AnEmail");
        AuthData testedAuthorization = assertDoesNotThrow(() -> dataAccess.addUserData(testedUser));

        assertEquals(testedAuthorization.username(), testedUser.username());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void getAuthDataNotInDatabase(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        String badAuthToken = "sasquatch";

        DataAccessException exception = assertThrows(DataAccessException.class, () -> dataAccess.getAuthData(badAuthToken));

        assertEquals(401, exception.getErrorCode());
        assertEquals("Error: No authorized user in database", exception.getMessage());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void getAuthDataNotLoggedIn(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        DataAccessException exception = assertThrows(DataAccessException.class, () -> dataAccess.getAuthData(""));

        assertEquals(401, exception.getErrorCode());
        assertEquals("Error: Not logged in", exception.getMessage());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void getAuthDataSuccess(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        UserData testUser = new UserData("username", "password", "email");
        AuthData initialData = assertDoesNotThrow(() -> dataAccess.addUserData(testUser));
        AuthData retrievedData = assertDoesNotThrow(() -> dataAccess.getAuthData(initialData.authToken()));

        assertEquals(initialData.authToken(), retrievedData.authToken());
        assertEquals(initialData.username(), retrievedData.username());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void getUserDataNotInDatabase(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        UserData testedUser = new UserData ("username", "password", "email");
        assertDoesNotThrow(() -> dataAccess.addUserData(testedUser));

        DataAccessException expectedException = assertThrows(DataAccessException.class, () -> dataAccess.getUserData("Jonathan"));

        assertEquals(401, expectedException.getErrorCode());
        assertEquals("Error: User is not in database", expectedException.getMessage());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void getUserDataSuccess(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        UserData testedUser = new UserData ("username", "password", "email");
        assertDoesNotThrow(() -> dataAccess.addUserData(testedUser));
        UserData resultingData = assertDoesNotThrow(() -> dataAccess.getUserData("username"));

        assertEquals(testedUser.username(), resultingData.username());
        assertTrue(BCrypt.checkpw(testedUser.password(), resultingData.password()));
        assertEquals(testedUser.email(), resultingData.email());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void deleteAuthDataNoDataPassedIn(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        DataAccessException expectedException = assertThrows(DataAccessException.class, () -> dataAccess.deleteAuthData(null));

        assertEquals(401, expectedException.getErrorCode());
        assertEquals("Error: No AuthData to delete", expectedException.getMessage());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void deleteAuthDataSuccess(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        UserData testedUser = new UserData ("username", "password", "email");
        AuthData testData = assertDoesNotThrow(() -> dataAccess.addUserData(testedUser));
        assertDoesNotThrow(() -> dataAccess.deleteAuthData(testData));

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void addGameDataNoName(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        DataAccessException expectedException = assertThrows(DataAccessException.class, () -> dataAccess.addGameData(null));

        assertEquals(400, expectedException.getErrorCode());
        assertEquals("Error: No received game name", expectedException.getMessage());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void addGameDataSuccess(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        GameData testData = assertDoesNotThrow(() -> dataAccess.addGameData("namedGame"));

        assertEquals(1, testData.gameID());
        assertEquals(null, testData.whiteUsername());
        assertEquals(null, testData.blackUsername());
        assertEquals("namedGame", testData.gameName());
        assertEquals(new ChessGame().getBoard(), testData.game().getBoard());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void getGameDataNotInDatabase(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        DataAccessException expectedException = assertThrows(DataAccessException.class, () -> dataAccess.getGameData(-3));

        assertEquals(400, expectedException.getErrorCode());
        assertEquals("Error: No game with that ID in database", expectedException.getMessage());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void getGameDataSuccess(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        GameData testInput = assertDoesNotThrow(() -> dataAccess.addGameData("newGame"));
        GameData testOutput = assertDoesNotThrow(() -> dataAccess.getGameData(testInput.gameID()));

        assertEquals(testInput.gameID(), testOutput.gameID());
        assertEquals(testInput.whiteUsername(), testOutput.whiteUsername());
        assertEquals(testInput.blackUsername(), testOutput.blackUsername());
        assertEquals(testInput.gameName(), testOutput.gameName());
        assertEquals(testInput.game().getBoard(), testOutput.game().getBoard());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void updateGameDataNotInDatabase(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        GameData testGame = new GameData(-3, null, null, "aName", new ChessGame());

        DataAccessException expectedException = assertThrows(DataAccessException.class, () -> dataAccess.updateGameData(-3, testGame));

        assertEquals(400, expectedException.getErrorCode());
        assertEquals("Error: No game data to update", expectedException.getMessage());

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void updateGameDataPlayersSuccess(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException {

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        GameData originalGame = assertDoesNotThrow(() -> dataAccess.addGameData("aGame"));
        GameData updatedData = new GameData (originalGame.gameID(), "John", "Lucy", "aGame", originalGame.game());

        assertDoesNotThrow(() -> dataAccess.updateGameData(originalGame.gameID(), updatedData));

        GameData retrievalTest = assertDoesNotThrow(() -> dataAccess.getGameData(originalGame.gameID()));

        assertEquals(updatedData.gameID(), retrievalTest.gameID());
        assertEquals(updatedData.whiteUsername(), retrievalTest.whiteUsername());
        assertEquals(updatedData.blackUsername(), retrievalTest.blackUsername());
        assertEquals(updatedData.gameName(), retrievalTest.gameName());
        assertEquals(updatedData.game().getBoard(), retrievalTest.game().getBoard());

    }


    // I'm not sure how to get this one to fail, so I'll write a couple success cases instead
    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void getAllGameDataSuccessManyGames(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException{

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        assertDoesNotThrow(() -> dataAccess.addGameData("I"));
        assertDoesNotThrow(() -> dataAccess.addGameData("Want"));
        assertDoesNotThrow(() -> dataAccess.addGameData("To"));
        assertDoesNotThrow(() -> dataAccess.addGameData("Be"));
        assertDoesNotThrow(() -> dataAccess.addGameData("A"));
        assertDoesNotThrow(() -> dataAccess.addGameData("Hippie"));
        assertDoesNotThrow(() -> dataAccess.addGameData("And"));

        GameData[] retrievedList = assertDoesNotThrow(() -> dataAccess.getAllGameData());

        assertEquals(7, retrievedList.length);

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void getAllGameDataSuccessNoGames(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException{

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        GameData[] retrievedList = assertDoesNotThrow(() -> dataAccess.getAllGameData());

        assertEquals(0, retrievedList.length);

    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, GeneralDataAccess.class})
    void deleteAllDataSuccess(Class<? extends DataAccessFramework> specificDatabase) throws DataAccessException{

        DataAccessFramework dataAccess = getDataAccessType(specificDatabase);

        assertDoesNotThrow(() -> dataAccess.deleteAllData());

        GameData[] gameList = assertDoesNotThrow(() -> dataAccess.getAllGameData());

        assertEquals(0, gameList.length);

    }

}
