package services;

import chess.model.AuthData;
import chess.model.UserData;
import dataaccess.DataAccessException;
import dataaccess.GeneralDataAccess;

import java.util.Objects;

public class Service {


    GeneralDataAccess dataAccess = new GeneralDataAccess();

    public Service(){



    }


    public AuthData registerNewUser(UserData newUserData) throws DataAccessException{

        // When would this possibly throw a 400 or 500 error?

        try{

            UserData checkData = dataAccess.getUserData(newUserData.username());

        }

        catch (DataAccessException exception) {

            return dataAccess.addUserData(newUserData);

        }

        // Maybe throw an exception here instead, actually
        throw new DataAccessException(403, "Error: User is already in database");

    }


    public AuthData loginUser(UserData loginUserData) throws DataAccessException{

        if (loginUserData.password() == null || loginUserData.username() == null){

            throw new DataAccessException(500, "Error: Did not input either username or passcode");

        }

        UserData checkData = dataAccess.getUserData(loginUserData.username());

        if (!Objects.equals(loginUserData.password(), checkData.password())){

            throw new DataAccessException(401, "Error: Incorrect passcode or username");

        }

        return dataAccess.addAuthData(checkData.username());

    }


    public void clearAllDatabases(){

        dataAccess.deleteAllData();

    }

}
