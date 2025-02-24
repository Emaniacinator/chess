package dataaccess;

import chess.model.UserData;
import chess.model.AuthData;
import chess.model.GameData;

public interface DataAccessFramework {

    UserData addUserData(UserData newUserData);

    UserData getUserData(String username);

    AuthData addAuthData(AuthData newAuthData);

    AuthData getAuthData(String authToken);

    static void deleteAuthData(AuthData dataToDelete){};

    GameData addGameData(GameData newGameData);

    GameData getGameData(int gameIdToGet);

    GameData updateGameData(int gameIdToGet, GameData updatedGame);

    GameData[] getAllGameData(); // Should this be a collection instead? Or is a list fine?

}
