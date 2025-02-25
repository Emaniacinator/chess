package services;

import chess.model.AuthData;
import chess.model.UserData;
import dataaccess.GeneralDataAccess;

public class Service {


    GeneralDataAccess dataAccess = new GeneralDataAccess();

    public Service(){



    }


    public AuthData registerNewUser(UserData newUserData){

        UserData checkData = dataAccess.getUserData(newUserData.username());
        if (checkData == null){

            return dataAccess.addUserData(newUserData);

        }

        // Maybe throw an exception here instead, actually
        return null;

    }


    public void clearAllDatabases(){

        dataAccess.deleteAllData();

    }

}
