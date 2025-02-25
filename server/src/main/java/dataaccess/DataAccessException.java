package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{

    int errorCode;
    String message;

    public DataAccessException(int errorCode, String message) {

        this.message = message;
        this.errorCode = errorCode;

    }

    public int getErrorCode(){

        return errorCode;

    }

    public String getMessage(){

        return message;

    }

}
