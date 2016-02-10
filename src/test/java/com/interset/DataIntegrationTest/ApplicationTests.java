package com.interset.DataIntegrationTest;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApplicationTests {

    private final OutputStream errorStream = new ByteArrayOutputStream();


    @Before
    public void setUp() {
        System.setErr(new PrintStream(errorStream));
    }

    @After
    public void tearDown() {
        System.setErr(null);
    }

    @Test
    public void main_validParameters_completedEtl() {
        Application.main(new String[] {"src/test/resources/metadataObjects100.json", "target/output.csv"});

    }

    @Test
    public void main_insufficientNumberOfParameters_exceptionThrownAndLogged() {
        Application.main(new String[] {"input.json"});
        assertEquals("We currently only expect 2 arguments! A path to a JSON file to read, and a path for a CSV file to write.\n", errorStream.toString());
    }

    @Test
    public void main_invalidJsonPath_exceptionThrownAndLogged() {
        Application.main(new String[] {"\0", "target/output.csv"});
        assertEquals("We couldn't convert the JSON file argument into a valid path!\n", errorStream.toString());
    }

    @Test
    public void main_absentJsonFile_exceptionThrownAndLogged() {
        Application.main(new String[] {"src/test/resources/metadataObjects1000.json", "target/output.csv"});
        assertEquals("JSON file [src/test/resources/metadataObjects1000.json] doesn't exist!\n", errorStream.toString());
    }

    @Test
    public void main_invalidCsvPath_exceptionThrownAndLogged() {
        Application.main(new String[] {"src/test/resources/metadataObjects100.json", "\0"});
        assertEquals("We couldn't convert the CSV file argument into a valid path!\n", errorStream.toString());
    }

    @Test
    public void main_absentCsvFolder_exceptionThrownAndLogged() {
        Application.main(new String[] {"src/test/resources/metadataObjects100.json", "test/output.csv"});
        assertEquals("We can't write to the directory [test] to create the CSV file! Does the directory exist?\n", errorStream.toString());
    }
}
