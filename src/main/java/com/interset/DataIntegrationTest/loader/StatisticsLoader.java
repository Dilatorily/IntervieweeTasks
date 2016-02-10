package com.interset.DataIntegrationTest.loader;

import java.util.concurrent.BlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.interset.DataIntegrationTest.dto.BlockingQueueDTO;
import com.interset.DataIntegrationTest.dto.DoneDTO;
import com.interset.DataIntegrationTest.dto.StatisticsDTO;

public class StatisticsLoader implements Loader<StatisticsDTO>, Runnable {

    private boolean isTransformationComplete;

    private BlockingQueue<BlockingQueueDTO> inputQueue;

    private ObjectWriter writer;

    public StatisticsLoader(BlockingQueue<BlockingQueueDTO> inputQueue) {
        isTransformationComplete = false;
        this.inputQueue = inputQueue;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        writer = mapper.writer().withDefaultPrettyPrinter();
    }

    @Override
    public void run() {
        while (!isTransformationComplete) {
            BlockingQueueDTO dto = inputQueue.poll();
            if (dto != null) {
                process(dto);
            }
        }
    }

    private void process(BlockingQueueDTO dto) {
        if (dto instanceof StatisticsDTO) {
            load((StatisticsDTO) dto);
        } else if (dto instanceof DoneDTO) {
            isTransformationComplete = true;
        } else {
            System.err.println("The parsed object is of an invalid type!");
        }
    }

    @Override
    public void load(StatisticsDTO statistics) {
        try {
            System.out.println(writer.writeValueAsString(statistics));
        } catch (JsonProcessingException e) {
            System.err.println("We could not print the statistics! Please verify if the statistics are valid.");
            e.getStackTrace();
        }
    }
}