package com.interset.DataIntegrationTest.loader;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.interset.DataIntegrationTest.dto.BlockingQueueDTO;
import com.interset.DataIntegrationTest.dto.CsvDTO;
import com.interset.DataIntegrationTest.dto.DoneDTO;

public class CsvLoader implements Loader<CsvDTO>, Runnable {

    private static final Object[] HEADERS = {"TIMESTAMP", "ACTION", "USER", "FOLDER", "FILENAME", "IP"};
    
    private boolean isTransformationComplete;
    
    private CSVPrinter output;
    
    private BlockingQueue<BlockingQueueDTO> inputQueue;
    
    public CsvLoader(BlockingQueue<BlockingQueueDTO> inputQueue, Path outputPath) throws IOException {
        isTransformationComplete = false;
        this.inputQueue = inputQueue;
        output = new CSVPrinter(new FileWriter(outputPath.toFile()), CSVFormat.DEFAULT.withRecordSeparator("\n"));
        output.printRecord(HEADERS);
    }

    @Override
    public void load(CsvDTO input) {
        try {
            output.printRecord(input.toCsvLine());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(!isTransformationComplete) {
            BlockingQueueDTO dto = null;
            dto = inputQueue.poll();
            
            if (dto != null) {
                if (dto instanceof CsvDTO) {
                    load((CsvDTO) dto);
                } else if (dto instanceof DoneDTO) {
                    isTransformationComplete = true;
                } else {
                    // TODO: Throw an exception
                }
            }
        }
        
        try {
            output.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}