package dataaccess;

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
        assertEquals(testedUser.password(), resultingData.password());
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

}
