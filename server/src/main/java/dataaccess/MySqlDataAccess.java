package dataaccess;

import chess.ChessGame;
import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.mindrot.jbcrypt.BCrypt;


import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccessFramework{

    // Please note that in this framework, all ChessGame instances have been made into JSON.

    int gameDataIterator = 0;

    public MySqlDataAccess() throws DataAccessException {

        configureDatabase();

    }


    public AuthData addUserData(UserData newUserData) throws DataAccessException {

        if (newUserData.username() == null || newUserData.username().isEmpty()){

            throw new DataAccessException(400, "Error: Can't register without a username");

        }

        if (newUserData.password() == null || newUserData.password().isEmpty()){

            throw new DataAccessException(400, "Error: Can't register without a passcode");

        }

        if (newUserData.email() == null || newUserData.email().isEmpty()){

            throw new DataAccessException(400, "Error: Can't register without an email");

        }

        UserData malleable = newUserData.createCopy();

        var jsonToAdd = new Gson().toJson(malleable);

        String newUserUpdateString = "INSERT INTO userDataTable (username, password, email, json) VALUES (?, ?, ?, ?)";

        //updateDatabase(newUserUpdateString, newUserData.username(), BCrypt.hashpw(malleable.password(), BCrypt.gensalt()), newUserData.email(), jsonToAdd);
        updateDatabase(newUserUpdateString, newUserData.username(), newUserData.password(), newUserData.email(), jsonToAdd);

        return addAuthData(newUserData.username());

    }


    public UserData getUserData(String username) throws DataAccessException {

        try (var connection = DatabaseManager.getConnection()){

            String getterStatement = "SELECT json FROM userDataTable WHERE username = ?";

            try (var preparedStatement = connection.prepareStatement(getterStatement)){

                preparedStatement.setString(1, username);

                System.out.println(preparedStatement);

                try (var responseStatement = preparedStatement.executeQuery()){

                    responseStatement.next();

                    var toJson = responseStatement.getString("json");

                    return new Gson().fromJson(toJson, UserData.class);

                }

            }

        }

        catch (SQLException foundNothing){

            throw new DataAccessException(401, "Error: User is not in database");

        }

    }


    public AuthData addAuthData(String username) throws DataAccessException {

        if (username == null || username.isEmpty()){

            throw new DataAccessException(500, "No valid username to make AuthData for");

        }

        String newAuthDataUpdateString = "INSERT INTO authDataTable (username, authToken, json) VALUES (?, ?, ?)";

        AuthData addedData = new AuthData(username, UUID.randomUUID().toString());

        var jsonToAdd = new Gson().toJson(addedData);

        updateDatabase(newAuthDataUpdateString, addedData.username(), addedData.authToken(), jsonToAdd);

        return addedData;

    }


    public AuthData getAuthData(String authToken) throws DataAccessException {

        if (authToken == null || authToken.isEmpty()){

            throw new DataAccessException(401, "Error: Not logged in");

        }

        try (var connection = DatabaseManager.getConnection()){

            String authGetter = "SELECT json FROM authDataTable WHERE authToken = ?";

            try (var preparedStatement = connection.prepareStatement(authGetter)){

                preparedStatement.setString(1, authToken);

                try (var responseStatement = preparedStatement.executeQuery()){

                    responseStatement.next();

                    var makeJson = responseStatement.getString("json");

                    return new Gson().fromJson(makeJson, AuthData.class);

                }

            }

        }

        catch(SQLException foundNothing){

            throw new DataAccessException(401, "Error: No authorized user in database");

        }

    }


    public void deleteAuthData(AuthData dataToDelete) throws DataAccessException {

        if (dataToDelete == null || Objects.equals(dataToDelete.authToken(), "")) {

            throw new DataAccessException(401, "Error: No AuthData to delete");

        }

        String authDeleteString = "DELETE FROM chess.authDataTable WHERE authToken = ?";

        updateDatabase(authDeleteString, dataToDelete.authToken());

    }


    public GameData addGameData(String gameName)  throws DataAccessException{

        if (gameName == null || gameName.isEmpty()){

            throw new DataAccessException(400, "Error: No received game name");

        }

        ChessGame defaultBoard = new ChessGame();

        gameDataIterator++;

        GameData addedGame =  new GameData(gameDataIterator, null, null, gameName, defaultBoard);

        String newGameDataString = "INSERT INTO gameDataTable (gameID, whiteUsername, blackUsername, gameName, game, json) VALUES (?, ?, ?, ?, ?, ?)";

        var jsonOfBoard = new Gson().toJson(defaultBoard);

        var jsonToAdd = new Gson().toJson(addedGame);

        updateDatabase(newGameDataString, gameDataIterator, null, null, gameName, jsonOfBoard, jsonToAdd);

        return addedGame;

    }


    public GameData getGameData(int gameIDToGet) throws DataAccessException {

        try (var connection = DatabaseManager.getConnection()){

            String gameGetter = "SELECT json FROM gameDataTable WHERE gameID = ?";

            try (var preparedStatement = connection.prepareStatement(gameGetter)){

                preparedStatement.setInt(1, gameIDToGet);

                try (var responseStatement = preparedStatement.executeQuery()){

                    responseStatement.next();

                    var makeJson = responseStatement.getString("json");

                    return new Gson().fromJson(makeJson, GameData.class);

                }

            }

        }

        catch (SQLException foundNothing){

            throw new DataAccessException(400, "Error: No game with that ID in database");

        }

    }


    public void updateGameData(Integer gameID, GameData newGame) throws DataAccessException {

        try{

            GameData originalGame = getGameData(gameID);

            String updateGameDataString = "UPDATE gameDataTable SET whiteUsername = ?, blackUsername = ?, game = ?, json = ? WHERE gameID = ?";

            updateDatabase(updateGameDataString, newGame.whiteUsername(), newGame.blackUsername(), new Gson().toJson(newGame.game().getBoard()), new Gson().toJson(newGame), gameID);

        }

        catch(DataAccessException foundNothing){

            throw new DataAccessException(400, "Error: No game data to update");

        }

    }


    public GameData[] getAllGameData() throws DataAccessException{

        ArrayList<GameData> allGameData = new ArrayList<>();

        try(var connection = DatabaseManager.getConnection()){

            String allGamesRequest = "SELECT json FROM gameDataTable";

            var preparedStatement = connection.prepareStatement(allGamesRequest);

            var responseStatement = preparedStatement.executeQuery();

            while(responseStatement.next()){

                var gameAsJson = responseStatement.getString("json");

                allGameData.add(new Gson().fromJson(gameAsJson, GameData.class));

            }

        }

        catch(Exception exception){

            throw new DataAccessException(500, "Error: Unexpected error while reading from game data");

        }

        int listSize = allGameData.size();

        GameData[] returnArray = new GameData[listSize];

        int iteratorHelper = 0;

        for(GameData currentData : allGameData){

            returnArray[iteratorHelper] = currentData;
            iteratorHelper++;

        }

        return returnArray;

    }


    public void deleteAllData() throws DataAccessException{

        updateDatabase("TRUNCATE userDataTable");
        updateDatabase("TRUNCATE authDataTable");
        updateDatabase("TRUNCATE gameDataTable");

    }


    // json *should* just read through as text so you  *should* be fine in theory
    private void updateDatabase(String inputStatement, Object... parameters) throws DataAccessException{

        try(var connection = DatabaseManager.getConnection()){

            try (var updateStatement = connection.prepareStatement(inputStatement, RETURN_GENERATED_KEYS)){ // Maybe I don't like this try section honestly.

                for(int i = 0; i < parameters.length; i++){

                    var currentParameter = parameters[i];

                    if (currentParameter instanceof Integer test){

                        updateStatement.setInt(i + 1, test);

                    }

                    else if (currentParameter instanceof String test){

                        updateStatement.setString(i + 1, test);

                    }

                    else if (currentParameter == null){

                        updateStatement.setNull(i + 1, NULL);

                    }

                }

                updateStatement.executeUpdate();

            }

        }

        catch(SQLException exception){

            throw new DataAccessException(500, String.format("Unable to update database for statement: %s \n Error message: %s", inputStatement, exception.getMessage()));

        }

    }


    private final String[] databaseSetupStatements = {

            // You may not need an ID field for the userDataTable and authDataTable if you set this up well
            // When you turn the game into json, it *should* read through as text. So you can update your whole
            // program to reflect this

            "CREATE TABLE IF NOT EXISTS userDataTable (" +
                    "`username` varchar(256) NOT NULL, " +
                    "`password` varchar(256) NOT NULL, " +
                    "`email` varchar(256) NOT NULL, " +
                    "`json` TEXT DEFAULT NULL)",

            "CREATE TABLE IF NOT EXISTS authDataTable (" +
                    "`username` varchar(256) NOT NULL, " +
                    "`authToken` varchar(256) NOT NULL, " +
                    "`json` TEXT DEFAULT NULL)",

            "CREATE TABLE IF NOT EXISTS gameDataTable (" + // This one has a flaw of never incrementing since you deleted the auto_increment part.
                    "`gameID` int NOT NULL, " +
                    "`whiteUsername` varchar(256), " +
                    "`blackUsername` varchar(256), " +
                    "`gameName` varchar(256) NOT NULL, " +
                    "`game` TEXT NOT NULL, " +
                    "`json` TEXT DEFAULT NULL)"};


    private void configureDatabase() throws DataAccessException {

        DatabaseManager.createDatabase();

        System.out.println("Starting configuration...");

        try(var connection = DatabaseManager.getConnection()){

            System.out.println("Connection acquired");

            for (String statement : databaseSetupStatements){

                try(var updateStatement = connection.prepareStatement(statement)){

                    updateStatement.executeUpdate();

                }

            }

            System.out.println("Successfully configured database");

        }

        catch(SQLException exception){

            throw new DataAccessException(500, String.format("Unable to configure database: %s", exception.getMessage()));

        }

    }

}
