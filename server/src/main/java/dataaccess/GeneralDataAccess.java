package dataaccess;

import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import dataaccess.DataAccessFramework;

public class GeneralDataAccess implements DataAccessFramework{

    public UserData addUserData(UserData newUserData) {
        return null;
    }


    public UserData getUserData(String username) {
        return null;
    }


    public AuthData addAuthData(AuthData newAuthData) {
        return null;
    }


    public AuthData getAuthData(String authToken) {
        return null;
    }


    public GameData addGameData(GameData newGameData) {
        return null;
    }


    public GameData getGameData(int gameIdToGet) {
        return null;
    }


    public GameData updateGameData(int gameIdToGet, GameData updatedGame) {
        return null;
    }


    public GameData[] getAllGameData() {
        return new GameData[0];
    }

}
