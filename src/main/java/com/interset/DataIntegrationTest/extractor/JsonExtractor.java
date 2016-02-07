package com.interset.DataIntegrationTest.extractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interset.DataIntegrationTest.dto.BlockingQueueDTO;
import com.interset.DataIntegrationTest.dto.DoneDTO;
import com.interset.DataIntegrationTest.dto.JsonDTO;

public class JsonExtractor implements Extractor<Path>, Runnable {

    private ObjectMapper mapper;

    private Path path;

    private List<BlockingQueue<BlockingQueueDTO>> outputQueues;

    public JsonExtractor(Path path, List<BlockingQueue<BlockingQueueDTO>> outputQueues) {
        mapper = new ObjectMapper();
        this.path = path;
        this.outputQueues = outputQueues;
    }

    @Override
    public void extract(Path path) {
        try {
            Files.lines(path).map(line -> parseLine(line)).forEachOrdered(dto -> putToOutputQueues(dto));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private JsonDTO parseLine(String line) {
        JsonDTO parsedJson = null;
        try {
            parsedJson = mapper.readValue(line, JsonDTO.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return parsedJson;
    }

    private void putToOutputQueues(JsonDTO dto) {
        outputQueues.forEach(queue -> {
            try {
                queue.put(dto);
            } catch (InterruptedException e) {
                // TODO
                e.getStackTrace();
            }
        });
    }

    private void completeExtraction () {
        outputQueues.forEach(queue -> {
            try {
                queue.put(new DoneDTO());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        extract(path);
        completeExtraction();
    }
}