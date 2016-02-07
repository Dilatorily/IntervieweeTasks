package com.interset.DataIntegrationTest.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Activity {

    @JsonProperty("createdDoc")
    CREATED_DOCUMENT,

    @JsonProperty("deletedDoc")
    DELETED_DOCUMENT,

    @JsonProperty("viewedDoc")
    VIEWED_DOCUMENT,

    @JsonProperty("addedText")
    ADDED_TEXT,

    @JsonProperty("changedText")
    CHANGED_TEXT,

    @JsonProperty("deletedText")
    DELETED_TEXT,

    @JsonProperty("hashed")
    HASHED,

    @JsonProperty("replicated")
    REPLICATED,

    @JsonProperty("archived")
    ARCHIVED,

    @JsonProperty("restored")
    RESTORED
}