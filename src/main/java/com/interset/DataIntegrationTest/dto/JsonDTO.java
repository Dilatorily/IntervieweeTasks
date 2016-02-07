package com.interset.DataIntegrationTest.dto;

import java.nio.file.Path;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeZoneDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.interset.DataIntegrationTest.enums.Activity;

public class JsonDTO implements BlockingQueueDTO {

    private long eventId;

    private String user;

    private String ipAddr;

    private Path file;

    private Activity activity;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "MM/dd/yyyy hh:mm:ssa")
    private LocalDateTime timestamp;

    @JsonDeserialize(using = DateTimeZoneDeserializer.class)
    private DateTimeZone timeOffset;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public DateTimeZone getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(DateTimeZone timeOffset) {
        this.timeOffset = timeOffset;
    }
}