package dataaccess;

import chess.ChessGame;
import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccessFramework{

    // Please note that in this framework, all ChessGame instances have been made into JSON.

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

        var jsonToAdd = new Gson().toJson(newUserData);

        String newUserUpdateString = "INSERT INTO userDataTable (username, password, email, json) VALUES (?, ?, ?, ?)";

        updateDatabase(newUserUpdateString, newUserData.username(), newUserData.password(), newUserData.email(), jsonToAdd);

        return addAuthData(newUserData.username());

    }


    public UserData getUserData(String username) throws DataAccessException {

        try (var connection = DatabaseManager.getConnection()){

            String getterStatement = "SELECT json FROM chess.userDataTable WHERE username = ?";

            try (var preparedStatement = connection.prepareStatement(getterStatement)){

                preparedStatement.setString(1, username);

                System.out.println(preparedStatement);

                try (var responseStatement = preparedStatement.executeQuery()){

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

            String authGetter = "SELECT json FROM chess.authDataTable WHERE authToken = ?";

            try (var preparedStatement = connection.prepareStatement(authGetter)){

                preparedStatement.setString(1, authToken);

                try (var responseStatement = preparedStatement.executeQuery()){

                    var makeJson = responseStatement.getString("json");

                    return new Gson().fromJson(makeJson, AuthData.class);

                }

            }

        }

        catch(SQLException foundNothing){

            throw new DataAccessException(401, "Error: No authorized user in database");

        }

    }


    public GameData addGameData(String gameName) {

        return null;

    }


    public GameData getGameData(int gameIdToGet) throws DataAccessException {

        return null;

    }


    public GameData[] getAllGameData() {

        return new GameData[0];

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
                    "`whiteUsername` varchar(256) NOT NULL, " +
                    "`blackUsername` varchar(256) NOT NULL, " +
                    "`gameName` varchar(256) NOT NULL, " +
                    "`game` varchar(1024) NOT NULL, " +
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
