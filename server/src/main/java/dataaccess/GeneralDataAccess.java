package dataaccess;

import chess.ChessGame;
import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GeneralDataAccess implements DataAccessFramework{


    int gameDataIterator = 0;

    private static HashMap<Integer, UserData> userDataMap = new HashMap<>();
    private static HashMap<Integer, AuthData> authDataMap = new HashMap<>();
    private static HashMap<Integer, GameData> gameDataMap = new HashMap<>();


    public GeneralDataAccess(){



    }


    public AuthData addUserData(UserData newUserData) throws DataAccessException{

        if (newUserData.username() == null || newUserData.username().isEmpty()){

            throw new DataAccessException(400, "Error: Can't register without a username");

        }

        if (newUserData.password() == null || newUserData.password().isEmpty()){

            throw new DataAccessException(400, "Error: Can't register without a passcode");

        }

        if (newUserData.email() == null || newUserData.email().isEmpty()){

            throw new DataAccessException(400, "Error: Can't register without an email");

        }

        userDataMap.put(newUserData.hashCode(), newUserData);

        return addAuthData(newUserData.username());

    }


    public UserData getUserData(String username) throws DataAccessException{

        for (Map.Entry<Integer, UserData> currentData : userDataMap.entrySet()){

            UserData checkedData = currentData.getValue();
            if (Objects.equals(checkedData.username(), username)){

                return checkedData;

            }

        }

        throw new DataAccessException(401, "Error: User is not in database");

    }


    public AuthData addAuthData(String username) throws DataAccessException{

        if (username == null || username.isEmpty()){

            throw new DataAccessException(500, "No valid username to make AuthData for");

        }

        AuthData addedData = new AuthData(username, UUID.randomUUID().toString());

        authDataMap.put(addedData.hashCode(), addedData);

        return addedData;

    }


    public AuthData getAuthData(String authToken) throws DataAccessException{

        if (authToken == null || authToken.isEmpty()){

            throw new DataAccessException(401, "Error: Not logged in");

        }

        for (Map.Entry<Integer, AuthData> currentData : authDataMap.entrySet()){

            AuthData checkedData = currentData.getValue();
            if (Objects.equals(checkedData.authToken(), authToken)){

                 return checkedData;

            }

        }

        throw new DataAccessException(401, "Error: No authorized user in database");

    }


    public static void deleteAuthData(AuthData dataToDelete) throws DataAccessException{

        authDataMap.values().remove(dataToDelete);

    }


    public GameData addGameData(String gameName) {

        ChessGame defaultBoard = new ChessGame();
        gameDataIterator++;
        GameData newGame =  new GameData(gameDataIterator, null, null, gameName, defaultBoard);
        gameDataMap.put(gameDataIterator, newGame);

        return newGame;

    }


    public GameData getGameData(int gameIDToGet) throws DataAccessException {

        for (Map.Entry<Integer, GameData> currentData : gameDataMap.entrySet()){

            GameData checkedData = currentData.getValue();
            if (Objects.equals(checkedData.gameID(), gameIDToGet)){

                return checkedData;

            }

        }

        throw new DataAccessException(400, "Error: No game with that ID in database");

    }


    // Probably this will need to be removed but for now it lets the code compile
    public void updateGameData(Integer gameID, GameData newGame) throws DataAccessException{

        gameDataMap.remove(gameID);
        gameDataMap.put(gameID, newGame);

    }


    public GameData[] getAllGameData() {

        int arraySize = gameDataMap.size();

        return gameDataMap.values().toArray(new GameData[arraySize]);

    }


    public void deleteAllData(){

        userDataMap.clear();
        authDataMap.clear();
        gameDataMap.clear();

        gameDataIterator = 0;

    }

}
