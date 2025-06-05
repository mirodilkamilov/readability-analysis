package de.uni_passau.fim.se2.sa.readability.features;

import com.github.javaparser.ParseException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenEntropyFeatureTest {
    private final TokenEntropyFeature entropyFeature = new TokenEntropyFeature();

    @Test
    public void testComputeMetric_EmptyString() {
        assertEquals(0.0, entropyFeature.computeMetric(""));
    }

    @Test
    public void testComputeMetric_Null() {
        assertEquals(0.0, entropyFeature.computeMetric(null));
    }

    @Test
    public void testComputeMetric_ReturnDoubleType() throws NoSuchMethodException {
        Method method = TokenEntropyFeature.class.getMethod("computeMetric", String.class);
        assertEquals(double.class, method.getReturnType());
    }

    @Test
    public void testComputeMetric_WhitespaceOnlyInput() {
        assertEquals(0.0, entropyFeature.computeMetric("   \n\t  "));
    }

    @Test
    public void testComputeMetric_InvalidJavaCode() {
        Exception e = assertThrows(
                RuntimeException.class,
                () -> entropyFeature.computeMetric("public void(){{")
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
        String result = String.format("%.3f", entropyFeature.computeMetric(helloWorld));
        assertEquals("3.347", result);
    }

    @Test
    public void testGetIdentifier_ReturnsExactMatch() {
        assertEquals("TokenEntropy", entropyFeature.getIdentifier());
    }
}
