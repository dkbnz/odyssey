package models;

import java.sql.Timestamp;

public class ApiError {

    private Timestamp timestamp;
    private String message;

    public ApiError(String message) {
        this.message = message;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
