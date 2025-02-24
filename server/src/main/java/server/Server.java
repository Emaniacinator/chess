package server;

import chess.model.UserData;
import spark.*;
import com.google.gson.*;
import services.Service;

public class Server {

    private final Service serviceHandler = new Service();

    public int run(int desiredPort) {

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    public Object registerUserHandler(Request request, Response response){ // Does this need to throw some kind of exception or is it good?

        UserData newUser = new Gson().fromJson(request.body(), UserData.class);
        // Check to see if the user exists
        // call the services module to add the user to the database
        // Have the services module return AuthData as a type, then
        // return that from this function

        return response;

    }

}
