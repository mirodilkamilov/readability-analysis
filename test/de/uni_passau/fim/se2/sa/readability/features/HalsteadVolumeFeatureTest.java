package de.uni_passau.fim.se2.sa.readability.features;

import com.github.javaparser.ParseException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HalsteadVolumeFeatureTest {
    private final HalsteadVolumeFeature hvFeature = new HalsteadVolumeFeature();

    @Test
    public void testComputeMetric_EmptyString() {
        assertEquals(0.0, hvFeature.computeMetric(""));
    }

    @Test
    public void testComputeMetric_Null() {
        assertEquals(0.0, hvFeature.computeMetric(null));
    }

    @Test
    public void testComputeMetric_ReturnDoubleType() throws NoSuchMethodException {
        Method method = NumberLinesFeature.class.getMethod("computeMetric", String.class);
        assertEquals(double.class, method.getReturnType());
    }

    @Test
    public void testComputeMetric_WhitespaceOnlyInput() {
        assertEquals(0.0, hvFeature.computeMetric("   \n\t  "));
    }

    @Test
    public void testComputeMetric_InvalidJavaCode() {
        Exception e = assertThrows(
                RuntimeException.class,
                () -> hvFeature.computeMetric("public void(){{")
        );
        assertEquals(ParseException.class, e.getCause().getClass());
    }

    @Test
    public void testComputeMetric_HelloWorldCode() {
        String helloWorld = """
                public class HelloWorld {
                    public static void main(String[] args) {
                        System.out.println("Hello, World!");
                    }
                }
                """;
        String result = String.format("%.3f", hvFeature.computeMetric(helloWorld));
        assertEquals("19.651", result);
    }

    @Test
    public void testComputeMetric_SimpleCode() {
        String helloWorld = """
                public void testMethod(){
                     int x = 10;
                     int y = 20;
                     int z = x + y;
                     x = x + y;
                     y = x > 10 ? 10 : 20;
                
                     List<Integer> list;
                     if (list instanceof ArrayList) {
                         for (int i = 0; i < 10; i++) {
                             System.out.println(i);
                         }
                     }
                
                }
                """;
        String result = String.format("%.3f", hvFeature.computeMetric(helloWorld));
        assertEquals("190.165", result);
    }

    @Test
    public void testGetIdentifier_ReturnsExactMatch() {
        assertEquals("HalsteadVolume", hvFeature.getIdentifier());
    }
}
