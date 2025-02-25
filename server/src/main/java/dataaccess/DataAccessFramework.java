package dataaccess;

import chess.model.UserData;
import chess.model.AuthData;
import chess.model.GameData;

public interface DataAccessFramework {

    AuthData addUserData(UserData newUserData);
    // Note that this is an AuthData return despite the UserData input because it simplifies
    // the process of creating a user and then immediately authorizint their login.

    UserData getUserData(String username);

    AuthData addAuthData(String username);

    AuthData getAuthData(String authToken);

    static void deleteAuthData(AuthData dataToDelete){};

    GameData addGameData(GameData newGameData);

    GameData getGameData(int gameIdToGet);

    GameData updateGameData(int gameIdToGet, GameData updatedGame);

    GameData[] getAllGameData(); // Should this be a collection instead? Or is a list fine?

}
