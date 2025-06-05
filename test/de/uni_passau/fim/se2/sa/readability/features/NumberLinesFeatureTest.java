package de.uni_passau.fim.se2.sa.readability.features;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberLinesFeatureTest {
    private final NumberLinesFeature linesFeature = new NumberLinesFeature();

    @Test
    public void testComputeMetric_EmptyString() {
        assertEquals(0.0, linesFeature.computeMetric(""));
    }

    @Test
    public void testComputeMetric_Null() {
        assertEquals(0.0, linesFeature.computeMetric(null));
    }

    @Test
    public void testComputeMetric_ReturnDoubleType() throws NoSuchMethodException {
        Method method = NumberLinesFeature.class.getMethod("computeMetric", String.class);
        assertEquals(double.class, method.getReturnType());
    }

    @Test
    public void testComputeMetric_HelloWorld() {
        String helloWorld = """
                public class HelloWorld {
                    public static void main(String[] args) {
                        System.out.println("Hello, World!");
                    }
                }
                """;
        assertEquals(5.0, linesFeature.computeMetric(helloWorld));
    }

    @Test
    public void testComputeMetric_HelloWorldWithTrailingEmptyString() {
        String helloWorld = """
                public class HelloWorld {
                    public static void main(String[] args) {
                        System.out.println("Hello, World!");
                    }
                }
                
                """;
        assertEquals(6.0, linesFeature.computeMetric(helloWorld));
    }

    @Test
    public void testComputeMetric_HelloWorldWithEmptyLines() {
        String helloWorld = """
                
                public class HelloWorld {
                
                    public static void main(String[] args) 
                    {
                
                        System.out.println("Hello, World!");
                    }
                
                }
                
                
                """;
        assertEquals(12.0, linesFeature.computeMetric(helloWorld));
    }

    @Test
    public void testComputeMetric_HelloWorldWithComments() {
        String helloWorld = """
                /**
                * Simple Hello World class
                *
                * @see java docs
                */
                public class HelloWorld {
                    // Main method
                    public static void main(String[] args) {
                        System.out.println("Hello, World!");
                    }
                }
                """;
        assertEquals(11.0, linesFeature.computeMetric(helloWorld));
    }

    @Test
    public void testComputeMetric_CodeWithEverythingMixed() {
        String helloWorld = """
                /*
                 * This is a block comment
                 * spanning multiple lines
                 */
                public class Example {
                
                    // Single-line comment at the top
                
                    private int value;
                
                    public int getValue() {
                        return value;
                    }
                
                    public void setValue(int value) {
                        this.value = value; // should be counted
                    }
                
                    public void doSomething() {
                        int a = 10;
                        int b = 20;
                        System.out.println(a + b); // valid line
                    }
                
                    /*
                     * Another block comment
                     * that spans multiple lines
                     */
                
                    public void anotherMethod() {
                        String text = "Hello";
                        System.out.println(text);
                    }
                
                    // Last single-line comment
                }
                
                """;
        assertEquals(37.0, linesFeature.computeMetric(helloWorld));
    }

    @Test
    public void testGetIdentifier_ReturnsExactMatch() {
        assertEquals("NumberLines", linesFeature.getIdentifier());
    }
}
