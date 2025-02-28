package service;

import chess.model.AuthData;
import chess.model.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import services.Service;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    /* Here is a list of all the services and needed tests:
     *
     * registerNewUser
     *  - Doesn't input a username [400]                              Written
     *  - Doesn't input a passcode [400]                              Written
     *  - Doesn't input an email [400]                                Written
     *  - Username is already taken [403]                             Written
     *  - Success case [200]                                          Written
     *
     * loginUser
     *  - Doesn't input a username [500]                              Written
     *  - Doesn't input a passcode [500]                              Written
     *  - User doesn't exist in database [401]                        Written
     *  - The passcode doesn't match what's in the UserData [401]     Written
     *  - Success case [200]                                          Written
     *
     * logoutUser
     *  - User isn't in AuthData [401]
     *  - Passed in null authToken (not logged in) [401]
     *  - Success case [200]
     *
     * createGame
     *  - User doesn't exist in AuthData [401]
     *  - Passed in null authToken (not logged in) [401]
     *  - Didn't input a game name
     *  - Success case [200]
     *
     * getAllGameData
     *  - User doesn't exist in AuthData [401]
     *  - Passed in null authToken (not logged in) [401]
     *  - Success case [200]
     *
     * joinGame
     *  - User doesn't exist in AuthData [401]
     *  - Passed in null authToken (not logged in) [401]
     *  - Color space is already taken [403]
     *  - Success case [200]
     *
     * clearAllDatabases
     *  - Not sure how to get an error, just make sure it works [200]
     *
     */

    private Service serviceTest;

    @BeforeEach
    public void setUp(){

        serviceTest = new Service();

    }

    @Nested
    @DisplayName("registerNewUser Tests")
    class registerUserTests{

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
    class loginUserTests{


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

}
