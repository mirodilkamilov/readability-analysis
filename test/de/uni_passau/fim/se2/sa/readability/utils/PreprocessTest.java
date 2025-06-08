package de.uni_passau.fim.se2.sa.readability.utils;

import de.uni_passau.fim.se2.sa.readability.features.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PreprocessTest {
    private Path tempDir;
    private File truthFile;
    private StringBuilder csv = new StringBuilder();
    private List<FeatureMetric> featureMetrics;
    private final int numOfSnippets = 10;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("preprocessTest");

        // Create .jsnp snippet files
        for (int i = 0; i < numOfSnippets; i++) {
            Path snippet = tempDir.resolve(i + ".jsnp");
            Files.writeString(snippet, """
                    public void test() {
                        int a = 3;
                        int b = 5;
                        System.out.println(a + b > 10 ? "good" : "bad");
                    }
                    """);
        }

        csv.append("File,NumberLines,TokenEntropy,HalsteadVolume,CyclomaticComplexity,Truth\n");
        String truthContent = """
                Rater,Snippet1,Snippet2,Snippet3,Snippet4,Snippet5,Snippet6,Snippet7,Snippet8,Snippet9,Snippet10
                Evaluator1,5,4,5,5,5,4,2,3,5,3
                Evaluator2,3,3,2,3,3,5,3,2,2,3
                Evaluator3,4,4,2,3,4,3,3,3,3,2
                Evaluator4,5,4,4,5,5,5,4,5,5,5
                Evaluator5,2,4,4,5,4,5,1,3,2,4
                Evaluator6,5,5,5,5,5,5,5,5,5,5
                Evaluator7,5,3,4,5,4,4,4,3,3,2
                Evaluator8,4,3,2,3,3,5,3,1,4,3
                Evaluator9,5,2,4,5,3,5,2,3,5,2
                Mean,4.22,3.6,3.56,4.33,4,4.56,3,3.11,3.78,3.22
                """;
        truthFile = File.createTempFile("truth", ".csv");
        Files.writeString(truthFile.toPath(), truthContent);

        featureMetrics = List.of(
                new NumberLinesFeature(),
                new TokenEntropyFeature(),
                new HalsteadVolumeFeature(),
                new CyclomaticComplexityFeature()
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        truthFile.delete();
    }

    @Test
    void testCollectCSVBody_SourceDirectoryDoesNotExist() {
        Path sourceDir = Path.of("nonexistent");
        IOException e = assertThrows(
                IOException.class,
                () -> Preprocess.collectCSVBody(sourceDir, truthFile, csv, featureMetrics)
        );
        assertEquals("Source directory does not exist or is not a directory.", e.getMessage());
    }

    @Test
    void testCollectCSVBody_SourceIsNotDirectory() throws IOException {
        Path sourceDir = File.createTempFile("truth", ".txt").toPath();
        IOException e = assertThrows(
                IOException.class,
                () -> Preprocess.collectCSVBody(sourceDir, truthFile, csv, featureMetrics)
        );
        assertEquals("Source directory does not exist or is not a directory.", e.getMessage());
    }

    @Test
    void testCollectCSVBody_EmptySourceDirectory() throws IOException {
        Path sourceDir = Files.createTempDirectory("emptyDir");
        IOException e = assertThrows(
                IOException.class,
                () -> Preprocess.collectCSVBody(sourceDir, truthFile, csv, featureMetrics)
        );
        assertEquals("Source directory is empty.", e.getMessage());
        Files.delete(sourceDir);
    }

    @Test
    void testCollectCSVBody_TruthFileDoesNotExist() {
        File truthFile = new File("truth.txt");
        IOException e = assertThrows(IOException.class, () ->
                Preprocess.collectCSVBody(tempDir, truthFile, csv, featureMetrics)
        );
        assertEquals("Truth file does not exist or not csv file.", e.getMessage());
        truthFile.delete();
    }

    @Test
    void testCollectCSVBody_TruthFileInvalidExtension() throws IOException {
        File truthFile = File.createTempFile("truth", ".txt");
        IOException e = assertThrows(IOException.class, () ->
                Preprocess.collectCSVBody(tempDir, truthFile, csv, featureMetrics)
        );
        assertEquals("Truth file does not exist or not csv file.", e.getMessage());
        truthFile.delete();
    }

    @Test
    void testCollectCSVBody_MeanLineMissing() throws IOException {
        File truthFile = File.createTempFile("truth", ".csv");
        Files.writeString(truthFile.toPath(), "Rater,Snippet1,Snippet2,Snippet3\nEvaluator1,5,4,5\nEvaluator2,4,5,4");
        IOException e = assertThrows(IOException.class, () ->
                Preprocess.collectCSVBody(tempDir, truthFile, csv, featureMetrics)
        );
        assertEquals("Invalid truth file: Mean values are missing.", e.getMessage());
        truthFile.delete();
    }

    @Test
    void testCollectCSVBody_MeanValuesMismatch() throws IOException {
        File truthFile = File.createTempFile("truth", ".csv");
        Files.writeString(truthFile.toPath(), """
                Rater,Snippet1,Snippet2,Snippet3
                Evaluator1,5,4,5
                Evaluator2,4,5,4
                Mean,4.5,4.5,4.5
                """);
        IOException e = assertThrows(IOException.class, () ->
                Preprocess.collectCSVBody(tempDir, truthFile, csv, featureMetrics)
        );
        assertEquals("Invalid truth file: The number of entries does not match with the number of files in the source directory.", e.getMessage());
        truthFile.delete();
    }

    @Test
    void testCollectCSVBody_SkipsNonJsnpFiles() throws IOException {
        Path nonJsnpFile = tempDir.resolve("note.txt");
        Files.writeString(nonJsnpFile, "this should be ignored");
        Preprocess.collectCSVBody(tempDir, truthFile, csv, featureMetrics);

        // Should still generate CSV with 10 rows, ignoring .txt file
        long lines = csv.toString().lines().count();
        assertEquals(numOfSnippets + 1, lines); // header + 10 snippets
    }

    @Test
    void testCollectCSVBody_CorrectCSVStructure() throws IOException {
        Preprocess.collectCSVBody(tempDir, truthFile, csv, featureMetrics);
        String[] csvLines = csv.toString().split("\n");
        String csvHeader = csvLines[0];
        String[] csvDataLines = Arrays.copyOfRange(csvLines, 1, csvLines.length);
        assertEquals(numOfSnippets + 1, csv.toString().lines().count());

        assertTrue(csvHeader.endsWith(",Truth"), "Last column should be Truth.");
        Arrays.stream(csvDataLines).forEach(
                line -> assertTrue(line.endsWith(",Y") || line.endsWith(",N"), "Last column should only contain Y or N values.")
        );

        List<String> actualFilenames = Arrays.stream(csvDataLines)
                .map(line -> line.split(",")[0])
                .toList();

        // Compare with sorted list of filenames
        List<String> expectedSortedFilenames = new ArrayList<>(actualFilenames);
        expectedSortedFilenames.sort(Comparator.comparingInt(Preprocess::extractLeadingNumber));

        String[] truthLines = Files.readString(truthFile.toPath()).split("\n");
        String truthMeanLine = truthLines[truthLines.length - 1];
        assertTrue(truthMeanLine.startsWith("Mean,"), "Invalid truth file: Mean values are missing.");

        String[] truthMeans = truthMeanLine.replaceFirst("Mean,", "").split(",");
        assertEquals(truthMeans.length, csvDataLines.length);
        for (int i = 0; i < truthMeans.length; i++) {
            String truthMean = truthMeans[i];
            double mean = Double.parseDouble(truthMean);
            assertTrue(csvDataLines[i].endsWith(mean >= Preprocess.TRUTH_THRESHOLD ? "Y" : "N"));
            assertTrue(csvDataLines[i].startsWith(expectedSortedFilenames.get(i)));
            assertEquals(csvHeader.split(",").length, csvDataLines[i].split(",").length);
        }

        assertEquals(expectedSortedFilenames, actualFilenames, "CSV entries must be sorted by filename.");
    }

    @Test
    public void testExtractLeadingNumber_ValidNumberFilename() {
        assertEquals(123, Preprocess.extractLeadingNumber("123.jsnp"));
        assertEquals(0, Preprocess.extractLeadingNumber("0.txt"));
        assertEquals(42, Preprocess.extractLeadingNumber("42.json"));
    }

    @Test
    public void testExtractLeadingNumber_NonNumericFilename() {
        assertEquals(Integer.MAX_VALUE, Preprocess.extractLeadingNumber("abc.jsnp"));
        assertEquals(Integer.MAX_VALUE, Preprocess.extractLeadingNumber("file.txt"));
        assertEquals(Integer.MAX_VALUE, Preprocess.extractLeadingNumber("test123.jsnp"));
    }

    @Test
    public void testExtractLeadingNumber_FilenameWithoutExtension() {
        assertEquals(123, Preprocess.extractLeadingNumber("123"));
        assertEquals(Integer.MAX_VALUE, Preprocess.extractLeadingNumber("file"));
    }

    @Test
    public void testExtractLeadingNumber_EmptyOrNullFilename() {
        assertEquals(Integer.MAX_VALUE, Preprocess.extractLeadingNumber(""));
        assertEquals(Integer.MAX_VALUE, Preprocess.extractLeadingNumber(null));
    }

    @Test
    public void testExtractLeadingNumber_NegativeAndLargeNumbers() {
        assertEquals(-99, Preprocess.extractLeadingNumber("-99.txt"));
        assertEquals(999999, Preprocess.extractLeadingNumber("999999.log"));
    }
}
