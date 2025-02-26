package server;

import chess.model.UserData;
import dataaccess.DataAccessException;
import jdk.jshell.spi.ExecutionControl;
import spark.*;
import com.google.gson.*;
import services.Service;
import chess.model.AuthData;

import java.util.Map;

public class Server {


    private final Service services = new Service();


    public Server(){



    }


    public int run(int desiredPort) {

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.post("/user", this::registerUserHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGameHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);
        Spark.delete("/db", this::clearAllDatabases);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    private void exceptionHandler(DataAccessException exception, Request request, Response response){

        int errorCode = exception.getErrorCode();
        String errorMessage = exception.getMessage();
        response.status(errorCode);
        response.body(new Gson().toJson(Map.of("error", errorCode, "message", errorMessage)));


    }


    public Object registerUserHandler(Request request, Response response) throws DataAccessException{ // Does this need to throw some kind of exception or is it good?

        // I have NO CLUE why, but for some reason this keeps reading the wrong data in and thus keeps failing test cases.
        UserData newUser = new Gson().fromJson(request.body(), UserData.class);

        AuthData resultingAuth = services.registerNewUser(newUser);

        return new Gson().toJson(resultingAuth);

    }


    public Object loginHandler(Request request, Response response) throws DataAccessException{

        UserData loginUserData = new Gson().fromJson(request.body(), UserData.class);

        AuthData resultingAuth = services.loginUser(loginUserData);

        return new Gson().toJson(resultingAuth);

    }


    public Object logoutHandler(Request request, Response response) throws DataAccessException{

        String authToken = request.headers("authorization");

        services.logoutUser(authToken);

        return new Gson().toJson("");

    }


    public Object listGameHandler(Request request, Response response){

        return null;

    }


    public Object createGameHandler(Request request, Response response){

        return null;

    }


    public Object joinGameHandler(Request request, Response response){

        return null;

    }


    public Object clearAllDatabases(Request request, Response response){

        services.clearAllDatabases();

        return new JsonObject();

    }

}
