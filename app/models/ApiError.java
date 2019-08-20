package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import java.sql.Timestamp;
import java.util.Arrays;

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

    public static JsonNode unauthorized() {
        return Json.toJson(Arrays.asList(new ApiError("You are not logged in.")));
    }

    public static JsonNode notFound() {
        return Json.toJson(Arrays.asList(new ApiError("Resource not found.")));
    }

    public static JsonNode forbidden() {
        return Json.toJson(Arrays.asList(new ApiError("You are not authorized to access this resource.")));
    }

    public static JsonNode forbidden(String message) {
        return Json.toJson(Arrays.asList(new ApiError(message)));
    }

    public static JsonNode invalidJson() {
        return Json.toJson(Arrays.asList(new ApiError("The JSON body received in the request was invalid.")));
    }

    public static JsonNode badRequest(String message) {
        return Json.toJson(Arrays.asList(new ApiError(message)));
    }
}
