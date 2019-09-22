package models.points;

public enum Action {
    DESTINATION_CREATED("DESTINATION_CREATED"),
    TRIP_CREATED("TRIP_CREATED"),
    QUEST_CREATED("QUEST_CREATED"),
    HINT_CREATED("HINT_CREATED"),
    RIDDLE_SOLVED("RIDDLE_SOLVED"),
    CHECKED_IN("CHECKED_IN"),
    POINTS_GAINED("POINTS_GAINED"),
    LOGIN_STREAK("LOGIN_STREAK"),
    INTERNATIONAL_QUEST_COMPLETED("INTERNATIONAL_QUEST_COMPLETED"),
    LARGE_QUEST_COMPLETED("LARGE_QUEST_COMPLETED"),
    DISTANCE_QUEST_COMPLETED("DISTANCE_QUEST_COMPLETED"),
    QUEST_COMPLETED("QUEST_COMPLETED"),
    HINT_UPVOTED("HINT_UPVOTED"),
    HINT_UPVOTE_REMOVED("HINT_UPVOTE_REMOVED");


    String value;

    Action(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
