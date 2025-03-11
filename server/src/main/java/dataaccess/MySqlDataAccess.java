package dataaccess;

import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlDataAccess implements DataAccessFramework{


    public MySqlDataAccess() throws DataAccessException {

        configureDatabase();

    }


    public AuthData addUserData(UserData newUserData) throws DataAccessException {

        String newUserUpdateString = "UPDATE userData SET [info in table] WHERE [pass in the id]";

        return null;

    }


    public UserData getUserData(String username) throws DataAccessException {

        return null;

    }


    public AuthData addAuthData(String username) throws DataAccessException {

        return null;

    }


    public AuthData getAuthData(String authToken) throws DataAccessException {

        return null;

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


    /* private int updateDatabase(String inputStatement, Object... parameters) throws DataAccessException{

        // Will this need to consider what database table it's being put into? I think that
        // it probably will, but I'm not sure how to specify that yet.

        try(var connection = DatabaseManager.getConnection()){

            try (var updateStatement = connection.prepareStatement(inputStatement, RETURN_GENERATED_KEYS)){ // Maybe I don't like this try section honestly.

                int objectPosition = 0;

                String updateString = "";

                for(int i = 0; i < parameters.length; i++){

                    var currentParameter = parameters[i];

                    if (currentParameter instanceof Integer test){



                    }

                }

            }

        }

        catch(SQLException exception){

            throw new DataAccessException(500, String.format("Unable to update database for statement: %s \n Error message: %s", inputStatement, exception.getMessage()));

        }

    } */


    private final String[] databaseSetupStatements = {

            // You may not need an ID field for the userDataTable and authDataTable if you set this up well
            "CREATE TABLE IF NOT EXISTS userDataTable (" +
                    "'username' varchar(256) NOT NULL, " +
                    "'password' varchar(256) NOT NULL, " +
                    "'email' varchar(256) NOT NULL, " +
                    "`json` TEXT DEFAULT NULL",

            "CFREATE TABLE IF NOT EXISTS authDataTable (" +
                    "'username' varchar(256) NOT NULL, " +
                    "'authToken' varchar(256) NOT NULL, " +
                    "`json` TEXT DEFAULT NULL",

            "CREATE TABLE IF NOT EXISTS gameDataTable (" +
                    "'gameID' int NOT NULL AUTO_INCREMENT, " +
                    "'whiteUsername' varchar(256) NOT NULL, " +
                    "'blackUsername' varchar(256) NOT NULL, " +
                    "'gameName' varchar(256) NOT NULL, " +
                    "'game' ChessGame, " +
                    "`json` TEXT DEFAULT NULL"};


    private void configureDatabase() throws DataAccessException {

        DatabaseManager.createDatabase();

        try(var connection = DatabaseManager.getConnection()){

            for (String statement : databaseSetupStatements){

                try(var updateStatement = connection.prepareStatement(statement)){

                    updateStatement.executeUpdate();

                }

            }

        }

        catch(SQLException exception){

            throw new DataAccessException(500, String.format("Unable to configure database: %s", exception.getMessage()));

        }

    }


}
