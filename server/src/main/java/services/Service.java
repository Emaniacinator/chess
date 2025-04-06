package services;

import chess.ChessGame;
import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import dataaccess.DataAccessException;
import dataaccess.DataAccessFramework;
import dataaccess.GeneralDataAccess;
import dataaccess.MySqlDataAccess;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

import static chess.ChessGame.TeamColor.*;

public class Service {

    DataAccessFramework dataAccess;

    public Service(){

        try{

            dataAccess = new MySqlDataAccess();

        }

        catch(DataAccessException exception){

            dataAccess = new GeneralDataAccess();

        }

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

        if (loginUserData.password() == null || loginUserData.username() == null
                || loginUserData.password().isEmpty()  || loginUserData.username().isEmpty()){

            throw new DataAccessException(500, "Error: Did not input either username or passcode");

        }

        UserData checkData = dataAccess.getUserData(loginUserData.username());

        if (!BCrypt.checkpw(loginUserData.password(), checkData.password())){

            throw new DataAccessException(401, "Error: Incorrect passcode or username");

        }

        return dataAccess.addAuthData(checkData.username());

    }


    public void logoutUser(String authToken) throws DataAccessException{

        AuthData checkedData = dataAccess.getAuthData(authToken);

        dataAccess.deleteAuthData(checkedData);

    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException{

        AuthData checkLogin = dataAccess.getAuthData(authToken);

        if (gameName == null || gameName.isEmpty()){

            throw new DataAccessException(400, "Error: No game name specified");

        }

        return dataAccess.addGameData(gameName);

    }


    public GameData[] getAllGameData(String authToken) throws DataAccessException{

        AuthData checkLogin = dataAccess.getAuthData(authToken);

        return dataAccess.getAllGameData();

    }


    // I really dislike this implementation of updating the usernames, but I'll have to get back to it tomorrow.
    // Also it's not finished hahaha
    public GameData joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException{

        AuthData checkLogin = dataAccess.getAuthData(authToken);

        if (playerColor == null || gameID < 0){

            throw new DataAccessException(400, "Error: Input an invalid gameID or playerColor");

        }

        GameData foundGame = dataAccess.getGameData(gameID);

        if (playerColor == WHITE && (foundGame.whiteUsername() == null || foundGame.whiteUsername().isEmpty())){

            GameData updatedGame = foundGame.setWhiteUsername(checkLogin.username());
            dataAccess.updateGameData(gameID, updatedGame);

        }

        else if (playerColor == BLACK && (foundGame.blackUsername() == null || foundGame.blackUsername().isEmpty())){

            GameData updatedGame = foundGame.setBlackUsername(checkLogin.username());
            dataAccess.updateGameData(gameID, updatedGame);

        }

        else{

            throw new DataAccessException(403, "Error: There is already a user in that game for the " + playerColor + " team");

        }

        return dataAccess.getGameData(gameID);

    }


    public void clearAllDatabases() throws DataAccessException{

        dataAccess.deleteAllData();

    }


    // If it makes me write a test case for this one I'm going to cry
    public DataAccessFramework getDataAccess(){

        return dataAccess;

    }

}
