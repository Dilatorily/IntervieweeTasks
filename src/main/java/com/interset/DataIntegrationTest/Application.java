package com.interset.DataIntegrationTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.interset.DataIntegrationTest.dto.BlockingQueueDTO;
import com.interset.DataIntegrationTest.exception.InsufficientArgumentsException;
import com.interset.DataIntegrationTest.extractor.JsonExtractor;
import com.interset.DataIntegrationTest.loader.CsvLoader;
import com.interset.DataIntegrationTest.loader.StatisticsLoader;
import com.interset.DataIntegrationTest.transformer.JsonTransformer;

public class Application {

    private static Application instance;

    private Application() {
        // Singleton to simplify the unit tests
    }

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public static void main(String[] args) {
        Application application = Application.getInstance();
        try {
            Path[] paths = application.getAndValidatePaths(args);

            // Initialize Queues
            // Since we are using LinkedBlockingQueue, we could explode the memory with a large enough dataset
            BlockingQueue<BlockingQueueDTO> transformQueue = new LinkedBlockingQueue<BlockingQueueDTO>();
            List<BlockingQueue<BlockingQueueDTO>> loadQueues = new ArrayList<BlockingQueue<BlockingQueueDTO>>();
            loadQueues.add(new LinkedBlockingQueue<BlockingQueueDTO>());
            loadQueues.add(new LinkedBlockingQueue<BlockingQueueDTO>());

            // Configure the ETL with the queues and paths
            JsonExtractor extractor = new JsonExtractor(paths[0], transformQueue);
            JsonTransformer csvTransformer = new JsonTransformer(transformQueue, loadQueues);
            CsvLoader csvLoader = new CsvLoader(loadQueues.get(0), paths[1]);
            StatisticsLoader statisticsLoader = new StatisticsLoader(loadQueues.get(1));

            // Initialize the ETL threads
            // We could possibly multithread the transformer, but it would need a refactor of the ETL
            Thread extractorThread = new Thread(extractor);
            Thread csvTransformerThread = new Thread(csvTransformer);
            Thread csvLoaderThread = new Thread(csvLoader);
            Thread statisticsLoaderThread = new Thread(statisticsLoader);

            // Start the ETL threads
            extractorThread.start();
            csvTransformerThread.start();
            csvLoaderThread.start();
            statisticsLoaderThread.start();
        } catch (InsufficientArgumentsException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private Path[] getAndValidatePaths(String[] paths) throws InsufficientArgumentsException, IOException {
        verifyRequiredNumberOfPaths(paths.length);

        Path[] validatedPaths = new Path[2];
        validatedPaths[0] = parseAndVerifyJsonPath(paths[0]);
        validatedPaths[1] = parseAndVerifyCsvPath(paths[1]);

        return validatedPaths;
    }

    private void verifyRequiredNumberOfPaths(int length) throws InsufficientArgumentsException {
        if (length != 2) {
            throw new InsufficientArgumentsException("We currently only expect 2 arguments! A path to a JSON file to read, and a path for a CSV file to write.");
        }
    }

    private Path parseAndVerifyJsonPath(String path) throws IOException {
        Path file = null;

        try {
            file = Paths.get(path);
        } catch (InvalidPathException e) {
            throw new IOException("We couldn't convert the JSON file argument into a valid path!");
        }

        if (!Files.exists(file)) {
            throw new IOException("JSON file [" + path + "] doesn't exist!");
        }

        return file;
    }

    private Path parseAndVerifyCsvPath(String path) throws IOException {
        Path file = null;

        try {
            file = Paths.get(path);
        } catch (InvalidPathException e) {
            throw new IOException("We couldn't convert the CSV file argument into a valid path!");
        }

        if (!Files.isWritable(file.getParent())) {
            throw new IOException("We can't write to the directory [" + file.getParent().toString() + "] to create the CSV file! Does the directory exist?");
        }

        return file;
    }
}