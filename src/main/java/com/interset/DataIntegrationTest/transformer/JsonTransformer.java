package com.interset.DataIntegrationTest.transformer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.interset.DataIntegrationTest.dto.BlockingQueueDTO;
import com.interset.DataIntegrationTest.dto.CsvDTO;
import com.interset.DataIntegrationTest.dto.DoneDTO;
import com.interset.DataIntegrationTest.dto.JsonDTO;
import com.interset.DataIntegrationTest.dto.StatisticsDTO;
import com.interset.DataIntegrationTest.enums.Action;
import com.interset.DataIntegrationTest.enums.Activity;
import com.interset.DataIntegrationTest.enums.DroppedEvent;

public class JsonTransformer implements Transformer<JsonDTO, CsvDTO>, Runnable {

    private boolean isExtractionComplete;
    
    private StatisticsDTO statistics;
    
    private Set<Long> parsedEventIds;

    private BlockingQueue<BlockingQueueDTO> inputQueue;

    private List<BlockingQueue<BlockingQueueDTO>> outputQueues;

    public JsonTransformer(BlockingQueue<BlockingQueueDTO> inputQueue, List<BlockingQueue<BlockingQueueDTO>> outputQueues) {
        isExtractionComplete = false;
        statistics = new StatisticsDTO();
        parsedEventIds = new HashSet<Long>();
        this.inputQueue = inputQueue;
        this.outputQueues = outputQueues;
    }

    @Override
    public CsvDTO transform(JsonDTO input) {
        CsvDTO csvDto = null;
        
        if (validateDtoAndLogDroppedEvents(input)) {
            parsedEventIds.add(input.getEventId());
            
            DateTime timestamp = parseTimestamp(input);
            Action action = parseAction(input);
            String user = parseUser(input);
            String folder = parseFolder(input);
            String fileName = parseFileName(input);
            String ip = input.getIpAddr();
            csvDto = new CsvDTO(timestamp, action, user, folder, fileName, ip);
            
            statistics.logUniqueUsers(user);
            statistics.logUniqueFiles(folder + fileName);
            statistics.logDates(timestamp);
            statistics.incrementActionCount(action);
        }
        
        return csvDto;
    }

    private boolean validateDtoAndLogDroppedEvents(JsonDTO input) {
        return detectAndLogUniqueEventId(input.getEventId()) && detectAndLogMappedActivity(input.getActivity());
    }
    
    private boolean detectAndLogUniqueEventId(long eventId) {
        boolean isUnique = !parsedEventIds.contains(eventId);
        
        if (!isUnique) {
            statistics.incrementDroppedEventCount(DroppedEvent.IS_DUPLICATED_EVENT_ID);
        }
        
        return isUnique;
    }
    
    private boolean detectAndLogMappedActivity(Activity activity) {
        boolean isMapped = activity.getIsMapped();
        
        if (!isMapped) {
            statistics.incrementDroppedEventCount(DroppedEvent.HAS_NO_ACTIVITY_MAPPING);
        }
        
        return isMapped;
    }

    private DateTime parseTimestamp(JsonDTO dto) {
        DateTimeZone timeZone = dto.getTimeOffset();
        if (timeZone == null) {
            timeZone = DateTimeZone.UTC;
        }

        return new DateTime(dto.getTimestamp().toDateTime(), timeZone);
    }

    private Action parseAction(JsonDTO dto) {
        Action action = null;

        switch (dto.getActivity()) {
        case CREATED_DOCUMENT:
        case ADDED_TEXT:
        case CHANGED_TEXT:
            action = Action.ADD;
            break;
        case DELETED_DOCUMENT:
        case DELETED_TEXT:
        case ARCHIVED:
            action = Action.REMOVE;
            break;
        case VIEWED_DOCUMENT:
            action = Action.ACCESSED;
            break;
        default:
            action = Action.NONE;
            break;
        }

        return action;
    }

    private String parseUser(JsonDTO dto) {
        return dto.getUser().replace("@company.com", "");
    }

    private String parseFolder(JsonDTO dto) {
        String folder = dto.getFile().getParent().toString();
        if (folder.charAt(folder.length() - 1) != '/') {
            folder += "/";
        }
        return folder;
    }

    private String parseFileName(JsonDTO dto) {
        return dto.getFile().getFileName().toString();
    }

    @Override
    public void run() {
        //TODO: Cleanup logic
        while (!isExtractionComplete) {
            BlockingQueueDTO dto = inputQueue.poll();
            if (dto != null) {
                if (dto instanceof JsonDTO) {
                    statistics.incrementLinesRead();
                    CsvDTO transformedDto = transform((JsonDTO) dto);
                    if (transformedDto != null) {
                        try {
                            outputQueues.get(0).put(transformedDto);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else if (dto instanceof DoneDTO) {
                    isExtractionComplete = true;
                    try {
                        outputQueues.get(1).put(statistics);
                        outputQueues.get(0).put(dto);
                        outputQueues.get(1).put(dto);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    // TODO: Throw error
                    System.err.println("invalid dto");
                }
            }
        }
    }
}