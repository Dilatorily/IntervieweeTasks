package com.interset.DataIntegrationTest.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Activity {

    @JsonProperty("createdDoc")
    CREATED_DOCUMENT(true),

    @JsonProperty("deletedDoc")
    DELETED_DOCUMENT(true),

    @JsonProperty("viewedDoc")
    VIEWED_DOCUMENT(true),

    @JsonProperty("addedText")
    ADDED_TEXT(true),

    @JsonProperty("changedText")
    CHANGED_TEXT(true),

    @JsonProperty("deletedText")
    DELETED_TEXT(true),

    @JsonProperty("hashed")
    HASHED(false),

    @JsonProperty("replicated")
    REPLICATED(false),

    @JsonProperty("archived")
    ARCHIVED(true),

    @JsonProperty("restored")
    RESTORED(false);
    
    private boolean isMapped;
    
    Activity(boolean isMapped) {
        this.isMapped = isMapped;
    }
    
    public boolean getIsMapped() {
        return isMapped;
    }
}