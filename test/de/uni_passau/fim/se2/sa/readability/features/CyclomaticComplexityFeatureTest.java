package de.uni_passau.fim.se2.sa.readability.features;

import com.github.javaparser.ParseException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CyclomaticComplexityFeatureTest {
    private final CyclomaticComplexityFeature cyFeature = new CyclomaticComplexityFeature();

    @Test
    public void testComputeMetric_EmptyString() {
        assertEquals(1.0, cyFeature.computeMetric(""));
    }

    @Test
    public void testComputeMetric_Null() {
        assertEquals(1.0, cyFeature.computeMetric(null));
    }

    @Test
    public void testComputeMetric_ReturnDoubleType() throws NoSuchMethodException {
        Method method = NumberLinesFeature.class.getMethod("computeMetric", String.class);
        assertEquals(double.class, method.getReturnType());
    }

    @Test
    public void testComputeMetric_WhitespaceOnlyInput() {
        assertEquals(1.0, cyFeature.computeMetric("   \n\t  "));
    }

    @Test
    public void testComputeMetric_InvalidJavaCode() {
        Exception e = assertThrows(
                RuntimeException.class,
                () -> cyFeature.computeMetric("public void(){{")
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
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("1.0", result);
    }

    @Test
    public void testComputeMetric_IfCondition() {
        String helloWorld = """
                void foo() {
                    if (x > 0) {
                        System.out.println("Positive");
                    }
                }
                """;
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("2.0", result);
    }

    @Test
    public void testComputeMetric_IfWithMultipleConditions() {
        String helloWorld = """
                void foo() {
                    if ((x > 0 && x < 5) || x == 10) {
                        System.out.println("Positive and less than 5 OR 10");
                    }
                }
                """;
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("4.0", result);
    }

    @Test
    public void testComputeMetric_TernaryCondition() {
        String helloWorld = """
                int result = (x > 0) ? 1 : -1;
                """;
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("2.0", result);
    }

    @Test
    public void testComputeMetric_TernaryWithMultipleConditions() {
        String helloWorld = """
                int result = (x > 0 && x < 5) || x == 10 ? 1 : -1;
                """;
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("4.0", result);
    }

    @Test
    public void testComputeMetric_SwitchCode() {
        String helloWorld = """
                public void testMethod(){
                     switch(x) {
                          case 1: doSomething(); break;
                          case 2: doSomethingElse(); break;
                          default: fallback(); break;
                     }
                }
                """;
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("3.0", result);
    }

    @Test
    public void testComputeMetric_SwitchCodeWithoutCase() {
        String helloWorld = """
                public void testMethod(){
                     switch(x) {
                          default: fallback(); break;
                     }
                }
                """;
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("1.0", result);
    }

    @Test
    public void testComputeMetric_Loops() {
        String helloWorld = """
                public void testMethod(){
                     for (int i = 0; i < 10; i++) { }
                     while (y < 5) { }
                     do { } while(y < 5);
                }
                """;
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("4.0", result);
    }

    @Test
    public void testComputeMetric_TryCatch() {
        String helloWorld = """
                public void testMethod(){
                     try {
                        risky();
                     } catch (IOException e) { }
                }
                """;
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("2.0", result);
    }

    @Test
    public void testComputeMetric_ForEachStmt() {
        String helloWorld = """
                public void testMethod() {
                    for (int i : numbers) {
                        System.out.println(i);
                    }
                }
                """;
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("2.0", result);
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
                
                         try {
                            risky();
                         } catch (IOException e) { }
                     }
                
                }
                """;
        String result = String.format("%.1f", cyFeature.computeMetric(helloWorld));
        assertEquals("5.0", result);
    }

    @Test
    public void testGetIdentifier_ReturnsExactMatch() {
        assertEquals("CyclomaticComplexity", cyFeature.getIdentifier());
    }

}
