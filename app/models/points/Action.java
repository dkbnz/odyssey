package models.points;

public enum Action {
    DESTINATION_CREATED("DESTINATION_CREATED"),
    RIDDLE_SOLVED("RIDDLE_SOLVED"),
    CHECKED_IN("CHECKED_IN");

    String value;

    Action(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
