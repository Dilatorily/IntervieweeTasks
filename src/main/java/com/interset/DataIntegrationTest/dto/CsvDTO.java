package com.interset.DataIntegrationTest.dto;

import org.joda.time.DateTime;

import com.interset.DataIntegrationTest.enums.Action;

public class CsvDTO implements BlockingQueueDTO {
    
    private DateTime timestamp;

    private Action action;

    private String user;

    private String folder;

    private String fileName;

    private String ip;
    
    public CsvDTO(DateTime timestamp, Action action, String user, String folder, String fileName, String ip) {
        this.timestamp = timestamp;
        this.action = action;
        this.user = user;
        this.folder = folder;
        this.fileName = fileName;
        this.ip = ip;
    }

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

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
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

    public Object[] toCsvLine() {
        String[] line = {timestamp.toString(), action.toString(), user, folder, fileName, ip};
        return line;
    }
}