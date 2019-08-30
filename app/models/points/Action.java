package models.points;

import io.ebean.annotation.EnumValue;

public enum Action {
    //@EnumValue("DESTINATION_CREATED")
    DESTINATION_CREATED("DESTINATION_CREATED"),

    //@EnumValue("RIDDLE_SOLVED")
    RIDDLE_SOLVED("RIDDLE_SOLVED"),

    //@EnumValue("CHECKED_IN")
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
