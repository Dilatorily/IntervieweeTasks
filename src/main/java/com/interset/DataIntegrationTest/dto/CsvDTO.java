package com.interset.DataIntegrationTest.dto;

import java.nio.file.Path;

import org.joda.time.DateTime;

import com.interset.DataIntegrationTest.enums.Action;

public class CsvDTO implements BlockingQueueDTO {

    private DateTime timestamp;

    private Action action;

    private String user;

    private Path folder;

    private String fileName;

    private String ip;

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Path getFolder() {
        return folder;
    }

    public void setFolder(Path folder) {
        this.folder = folder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return timestamp.toString();
    }
}