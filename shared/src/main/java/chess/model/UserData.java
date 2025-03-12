package chess.model;

import com.google.gson.*;

public record UserData(String username, String password, String email) {

    public UserData createCopy(){

        return new UserData(username, password, email);

    }

}
