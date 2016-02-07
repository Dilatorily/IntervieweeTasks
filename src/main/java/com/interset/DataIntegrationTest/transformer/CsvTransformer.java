package com.interset.DataIntegrationTest.transformer;

import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.interset.DataIntegrationTest.dto.BlockingQueueDTO;
import com.interset.DataIntegrationTest.dto.CsvDTO;
import com.interset.DataIntegrationTest.dto.DoneDTO;
import com.interset.DataIntegrationTest.dto.JsonDTO;
import com.interset.DataIntegrationTest.enums.Action;

public class CsvTransformer implements Transformer<JsonDTO, CsvDTO>, Runnable {

    private boolean isExtractionComplete;

    private BlockingQueue<BlockingQueueDTO> inputQueue;

    private BlockingQueue<BlockingQueueDTO> outputQueue;

    public CsvTransformer(BlockingQueue<BlockingQueueDTO> inputQueue, BlockingQueue<BlockingQueueDTO> outputQueue) {
        isExtractionComplete = false;
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    @Override
    public CsvDTO transform(JsonDTO input) {
        parseTimestamp(input);
        parseAction(input);
        parseUser(input);

        parseFolder(input);
        parseFileName(input);
        input.getIpAddr();

        CsvDTO csvDto = null;

        return csvDto;
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
        while (!isExtractionComplete) {
            BlockingQueueDTO dto = inputQueue.poll();
            if (dto != null) {
                if (dto instanceof JsonDTO) {
                    transform((JsonDTO) dto);
                } else if (dto instanceof DoneDTO) {
                    isExtractionComplete = true;
                    try {
                        outputQueue.put(dto);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("invalid dto");
                }
            }
        }
    }

}