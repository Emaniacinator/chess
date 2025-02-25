package dataaccess;

import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GeneralDataAccess implements DataAccessFramework{


    int userDataIterator = 1;
    int authDataIterator = 1;
    int gameDataIterator = 1;
    private HashMap<Integer, UserData> userDataMap = new HashMap<>();
    private HashMap<Integer, AuthData> authDataMap = new HashMap<>();
    private HashMap<Integer, GameData> gameDataMap = new HashMap<>();


    public GeneralDataAccess(){



    }


    public AuthData addUserData(UserData newUserData) throws DataAccessException{

        if (newUserData.username() == null){

            throw new DataAccessException(400, "Error: Can't register without a username");

        }

        if (newUserData.passcode() == null){

            throw new DataAccessException(400, "Error: Can't register without a passcode");

        }

        if (newUserData.email() == null){

            throw new DataAccessException(400, "Error: Can't register without an email");

        }

        userDataMap.put(userDataIterator, newUserData);
        userDataIterator++;

        return addAuthData(newUserData.username());

    }


    public UserData getUserData(String username) throws DataAccessException{

        for (Map.Entry<Integer, UserData> currentData : userDataMap.entrySet()){

            UserData checkedData = currentData.getValue();
            if (Objects.equals(checkedData.username(), username)){

                return checkedData;

            }

        }

        throw new DataAccessException(404, "Error: User is not in database");

    }


    public AuthData addAuthData(String username) {

        AuthData addedData = new AuthData(username, UUID.randomUUID().toString());

        authDataMap.put(authDataIterator, addedData);
        authDataIterator++;

        return addedData;

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
