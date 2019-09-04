package models.points;

public enum Action {
    DESTINATION_CREATED("DESTINATION_CREATED"),
    QUEST_CREATED("QUEST_CREATED"),
    OBJECTIVE_CREATED("OBJECTIVE_CREATED"),
    RIDDLE_SOLVED("RIDDLE_SOLVED"),
    CHECKED_IN("CHECKED_IN"),
    QUEST_COMPLETED("QUEST_COMPLETED");

    String value;

    Action(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
