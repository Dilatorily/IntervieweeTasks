package com.interset.DataIntegrationTest.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import com.interset.DataIntegrationTest.enums.Action;
import com.interset.DataIntegrationTest.enums.DroppedEvent;

public class StatisticsDTO implements BlockingQueueDTO {
    
    private int linesRead;
    
    private int droppedEventsCounts;
    
    private Map<DroppedEvent, Integer> droppedEvents;
    
    private Set<String> users;
    
    private int uniqueUsers;
    
    private Set<String> files;
    
    private int uniqueFiles;
    
    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime startDate;
    
    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime endDate;
    
    Map<Action, Integer> actions;
    
    public StatisticsDTO() {
        linesRead = 0;
        droppedEventsCounts = 0;
        droppedEvents = new HashMap<>();
        droppedEvents.put(DroppedEvent.IS_DUPLICATED_EVENT_ID, 0);
        droppedEvents.put(DroppedEvent.HAS_NO_ACTIVITY_MAPPING, 0);
        users = new HashSet<>();
        uniqueUsers = 0;
        files = new HashSet<>();
        uniqueFiles = 0;
        actions = new HashMap<>();
        actions.put(Action.ADD, 0);
        actions.put(Action.REMOVE, 0);
        actions.put(Action.ACCESSED, 0);
    }

    public int getLinesRead() {
        return linesRead;
    }
    
    public void incrementLinesRead() {
        ++linesRead;
    }

    public void incrementDroppedEventCount(DroppedEvent event) {
        int count = droppedEvents.get(event);
        droppedEvents.put(event, count + 1);
        ++droppedEventsCounts;
    }
    
    public int getDroppedEventsCounts() {
        return droppedEventsCounts;
    }
    
    public Map<DroppedEvent, Integer> getDroppedEvents() {
        return droppedEvents;
    }
    
    public void logUniqueUsers(String user) {
        if (!users.contains(user)) {
            users.add(user);
            ++uniqueUsers;
        }
    }
    
    public int getUniqueUsers() {
        return uniqueUsers;
    }
    
    public void logUniqueFiles(String filePath) {
        if (!files.contains(filePath)) {
            files.add(filePath);
            ++uniqueFiles;
        }
    }
    
    public int getUniqueFiles() {
        return uniqueFiles;
    }
    
    public void logDates(DateTime timestamp) {
        if (startDate == null || timestamp.isBefore(startDate)) {
            startDate = timestamp;
        }
        
        if (endDate == null || timestamp.isAfter(endDate)){
            endDate = timestamp;
        }
    }
    
    public DateTime getStartDate() {
        return startDate;
    }
    
    public DateTime getEndDate() {
        return endDate;
    }

    public void incrementActionCount(Action action) {
        int count = actions.get(action);
        actions.put(action, count + 1);
    }
    
    public Map<Action, Integer> getActions() {
        return actions;
    }
}