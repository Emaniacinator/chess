package dataaccess;

import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GeneralDataAccess implements DataAccessFramework{


    final private HashMap<Integer, UserData> userDataMap = new HashMap<>();
    final private HashMap<Integer, AuthData> authDataMap = new HashMap<>();
    final private HashMap<Integer, GameData> gameDataMap = new HashMap<>();


    public GeneralDataAccess(){



    }


    public AuthData addUserData(UserData newUserData) {

        // Should this return AuthData instead? Like, make and then return it? Or should that be done by the Service

        userDataMap.put(newUserData.hashCode(), newUserData);

        return addAuthData(newUserData.username());

    }


    public UserData getUserData(String username) {

        for (Map.Entry<Integer, UserData> currentData : userDataMap.entrySet()){

            UserData checkedData = currentData.getValue();
            if (Objects.equals(checkedData.username(), username)){

                return checkedData;

            }

        }

        return null;

    }


    public AuthData addAuthData(String username) {

        return new AuthData(username, UUID.randomUUID().toString());

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


    public void deleteAllData(){

        userDataMap.clear();
        authDataMap.clear();
        gameDataMap.clear();

    }

}
