package dataaccess;

import chess.model.UserData;
import chess.model.AuthData;
import chess.model.GameData;

public interface DataAccessFramework {

    AuthData addUserData(UserData newUserData) throws DataAccessException;
    // Note that this is an AuthData return despite the UserData input because it simplifies
    // the process of creating a user and then immediately authorizes their login.

    UserData getUserData(String username) throws DataAccessException;

    AuthData addAuthData(String username) throws DataAccessException;

    AuthData getAuthData(String authToken) throws DataAccessException;

    void deleteAuthData(AuthData dataToDelete) throws DataAccessException;

    GameData addGameData(String gameName) throws DataAccessException;

    GameData getGameData(int gameIdToGet) throws DataAccessException;

    void updateGameData(Integer gameID, GameData newGame) throws DataAccessException;

    GameData[] getAllGameData(); // Should this be a collection instead? Or is a list fine?

    void deleteAllData() throws DataAccessException;

}
