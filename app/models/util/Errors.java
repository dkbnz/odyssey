package models.util;

public enum Errors {
    PROFILE_NOT_FOUND("Requested profile not found"),
    DESTINATION_NOT_FOUND("Requested destination not found"),
    OBJECTIVE_NOT_FOUND("Requested objective not found"),
    PHOTO_NOT_FOUND("Requested photo not found"),
    DUPLICATE_DESTINATION("A destination with these details already exists either in your destinations or " +
            "public destinations lists"),
    INVALID_LATITUDE("Given latitude is not valid"),
    INVALID_LONGITUDE("Given longitude is not valid"),
    INVALID_PHOTO_SIZE_TYPE( "Invalid photo size/type"),
    INVALID_NO_IMAGES_PROVIDED("No images to upload"),
    INVALID_PHOTO_TYPE( "Invalid photo size"),
    OBJECTIVE_IN_USE("Cannot delete, objective is currently used in a quest"),

    RIDDLE_NOT_PROVIDED("No riddle provided");



    String value;

    Errors(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

