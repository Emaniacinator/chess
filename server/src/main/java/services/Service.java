package services;

import chess.ChessGame;
import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import dataaccess.DataAccessException;
import dataaccess.GeneralDataAccess;

import java.util.Objects;

import static chess.ChessGame.TeamColor.*;

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


    public void logoutUser(String authToken) throws DataAccessException{

        AuthData checkedData = dataAccess.getAuthData(authToken);

        dataAccess.deleteAuthData(checkedData);

    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException{

        AuthData checkLogin = dataAccess.getAuthData(authToken);

        if (gameName == null){

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
    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameId) throws DataAccessException{

        AuthData checkLogin = dataAccess.getAuthData(authToken);

        if (playerColor == null || gameId < 0){

            throw new DataAccessException(400, "Error: Input an invalid gameID or playerColor");

        }

        GameData foundGame = dataAccess.getGameData(gameId + 1);

        if (playerColor == WHITE && foundGame.whiteUsername() == null){

            GameData updatedGame = foundGame.setWhiteUsername(checkLogin.username());
            dataAccess.updateGameData(gameId, updatedGame);

        }

        else if (playerColor == BLACK && foundGame.blackUsername() == null){

            GameData updatedGame = foundGame.setBlackUsername(checkLogin.username());
            dataAccess.updateGameData(gameId, updatedGame);

        }

        else{

            throw new DataAccessException(403, "Error: There is already a user in that game for the " + playerColor + " team");

        }

    }


    public void clearAllDatabases(){

        dataAccess.deleteAllData();

    }

}
