package de.uni_passau.fim.se2.sa.readability.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class ClassifyTest {
    File csvFile;

    @BeforeEach
    void setUp() throws IOException {
        csvFile = File.createTempFile("preprocess", ".csv");
        Files.writeString(csvFile.toPath(), """
                Feature1,Feature2,Truth
                1.0,2.0,Y
                1.5,1.8,N
                1.0,2.0,Y
                1.5,1.8,N
                2.0,2.2,Y
                1.3,2.1,N
                2.1,1.9,Y
                2.0,2.2,Y
                1.3,2.1,N
                2.1,1.9,Y
                1.0,2.0,Y
                1.5,1.8,N
                2.0,2.2,Y
                1.3,2.1,N
                2.1,1.9,Y
                """);
    }

    @AfterEach
    void tearDown() {
        csvFile.delete();
    }

    @Test
    public void testLoadDataset() throws Exception {
        Instances dataset = Classify.loadDataset(csvFile);
        assertNotNull(dataset);
        assertEquals(3, dataset.numAttributes(), "Expected 3 attributes (2 features + 1 class)");
        assertEquals(15, dataset.numInstances(), "Expected 15 instances");
        assertEquals(dataset.classIndex(), dataset.numAttributes() - 1, "Class index should be last attribute");
    }

    @Test
    public void testTrainAndEvaluate() throws Exception {
        Instances dataset = Classify.loadDataset(csvFile);
        Evaluation evaluation = Classify.trainAndEvaluate(dataset);
        assertNotNull(evaluation);
        assertTrue(evaluation.pctCorrect() >= 0.0 && evaluation.pctCorrect() <= 100.0,
                "Accuracy should be within 0 to 100");
    }
}
