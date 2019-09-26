package models.util;

public enum Errors {
    PROFILE_NOT_FOUND("Requested profile not found."),
    DESTINATION_NOT_FOUND("Requested destination not found."),
    OBJECTIVE_NOT_FOUND("Requested objective not found."),
    PHOTO_NOT_FOUND("Requested photo not found."),
    QUEST_NOT_FOUND("Requested quest not found."),
    QUEST_ATTEMPT_NOT_FOUND("Requested quest attempt not found."),
    TRIP_NOT_FOUND("Requested trip not found."),
    DUPLICATE_DESTINATION("A destination with these details already exists either in your destinations or " +
            "public destinations lists."),
    DUPLICATE_PROFILE("Duplicate profile found."),
    INVALID_LATITUDE("Given latitude is not valid."),
    INVALID_LONGITUDE("Given longitude is not valid."),
    INVALID_PHOTO_SIZE( "Images can't be larger than 5MB."),
    INVALID_PHOTO_TYPE( "Images must be a jpeg or png."),
    INVALID_NO_IMAGES_PROVIDED("No images to upload."),
    OBJECTIVE_IN_USE("Cannot delete, objective is currently used in a quest."),
    HASH_FAIL("Unable to hash the user password."),
    INVALID_NATIONALITY_TRAVELLER_TYPES("Invalid number of Nationalities/Traveller Types."),
    DELETING_DEFAULT_ADMIN("You can not delete the default administrator."),
    REMOVE_DEFAULT_ADMIN_STATUS("You can not remove the default administrator as an admin."),
    START_OWN_QUEST("You cannot start your own quest."),
    QUEST_ATTEMPT_EXISTS("You have already started this quest."),
    INVALID_JSON_FORMAT("Invalid Json format."),
    MAX_RIDDLE_LENGTH("Objective riddles must not exceed 255 characters in length"),
    NO_OBJECTIVE_RIDDLE("A riddle must be provided"),
    NO_OBJECTIVE_OWNER("This objective does not have an owner"),
    NO_OBJECTIVE_DESTINATION("Objectives must have a destination"),
    NO_OBJECTIVE_RADIUS("You must select a range for an objective destination's check in"),
    INVALID_PAGE_SIZE_REQUESTED("Invalid page size provided."),
    INVALID_PAGE_NUMBER_REQUESTED("Invalid page number provided."),
    HINT_NOT_FOUND("The requested hint is not found."),
    VOTE_ALREADY_EXISTS("You already have voted on this hint."),
    HINT_OBJECTIVE_SOLVED("You cannot request a hint for an objective you have already solved."),
    HINT_OBJECTIVE_OWNER("You cannot request a hint for an objective you own."),
    NO_QUEST_TITLE("A quest title must be provided."),
    LONG_QUEST_TITLE("A quest title must be provided."),
    NO_START_DATE("You must provide a start date."),
    NO_END_DATE("You must provide an end date."),
    STAR_DATE_BEFORE_END_DATE("Start date must be before end date."),
    NO_QUEST_OWNER("This quest does not have an owner."),
    QUEST_NEED_ONE_OBJECTIVE("You must provide at least one objective for a quest."),
    QUEST_CANNOT_BE_EDITED("You cannot edit this part of the quest as it has been started");


    String value;

    Errors(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

