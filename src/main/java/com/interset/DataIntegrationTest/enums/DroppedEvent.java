package com.interset.DataIntegrationTest.enums;

public enum DroppedEvent {

    HAS_NO_ACTIVITY_MAPPING("No action mapping"),
    IS_DUPLICATED_EVENT_ID("Duplicates");

    private String name;

    DroppedEvent(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
