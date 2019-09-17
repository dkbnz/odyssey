package models.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import java.util.Arrays;
import java.util.Date;

public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    private Date timestamp;
    private String message;

    public ApiError(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public static JsonNode unauthorized() {
        return Json.toJson(Arrays.asList(new ApiError("You are not logged in.")));
    }

    public static JsonNode notFound() {
        return Json.toJson(Arrays.asList(new ApiError("Resource not found.")));
    }

    public static JsonNode notFound(Errors message) {
        return Json.toJson(Arrays.asList(new ApiError(message.toString())));
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

    public static JsonNode badRequest(Errors message) {
        return Json.toJson(Arrays.asList(new ApiError(message.toString())));
    }
}