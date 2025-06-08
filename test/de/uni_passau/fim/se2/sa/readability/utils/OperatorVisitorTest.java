package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.BodyDeclaration;
import de.uni_passau.fim.se2.sa.readability.utils.OperatorVisitor.OperatorType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperatorVisitorTest {
    final OperatorVisitor visitor = new OperatorVisitor();

    @Test
    public void testVisit_FieldDeclaration() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public class HelloWorld {
                    String message = "Hello World";
                    Integer count;
                    boolean isValid = false;
                }
                """);
        bd.accept(visitor, null);

        Map<OperatorType, Integer> operators = visitor.getOperatorsPerMethod();
        assertEquals(1, visitor.getNumberOfUniqueOperators());
        assertEquals(3, visitor.getTotalNumberOfOperators());
        assertEquals(3, operators.get(OperatorType.ASSIGNMENT));
    }

    @Test
    public void testVisit_VariableDeclarationExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    int count = 5;
                    String message;
                    message = "Hello";
                    boolean isValid = false;
                }
                """);
        bd.accept(visitor, null);

        Map<OperatorType, Integer> operators = visitor.getOperatorsPerMethod();
        assertEquals(1, visitor.getNumberOfUniqueOperators());
        assertEquals(4, visitor.getTotalNumberOfOperators());
        assertEquals(4, operators.get(OperatorType.ASSIGNMENT));
    }

    @Test
    public void testVisit_AssignExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    String message;
                    message = "Hello";
                    int count = 5;
                    count += 5;
                    message = count >= 0 ? message : "not specified";
                }
                """);
        bd.accept(visitor, null);

        Map<OperatorType, Integer> operators = visitor.getOperatorsPerMethod();
        assertEquals(3, visitor.getNumberOfUniqueOperators());
        assertEquals(7, visitor.getTotalNumberOfOperators());
        assertEquals(5, operators.get(OperatorType.ASSIGNMENT));
    }

    @Test
    public void testVisit_BinaryExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    String message;
                    message = "Hello";
                    int count = 5;
                    count += 5;
                    count = count - 3;
                    message = count >= 0 && count < 15 ? message : "not specified";
                    if (count == 5 || count != 0){
                        System.out.println(message);
                    }
                }
                """);
        bd.accept(visitor, null);

        Map<OperatorType, Integer> operators = visitor.getOperatorsPerMethod();
        assertEquals(3, visitor.getNumberOfUniqueOperators());
        assertEquals(14, visitor.getTotalNumberOfOperators());
        assertEquals(6, operators.get(OperatorType.ASSIGNMENT));
        assertEquals(7, operators.get(OperatorType.BINARY));
        assertEquals(1, operators.get(OperatorType.CONDITIONAL));
    }

    @Test
    public void testVisit_UnaryExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    int count = 5;
                    count++;
                    --count;
                    count = !(count < 0) ? count : 0;
                }
                """);
        bd.accept(visitor, null);

        Map<OperatorType, Integer> operators = visitor.getOperatorsPerMethod();
        assertEquals(4, visitor.getNumberOfUniqueOperators());
        assertEquals(7, visitor.getTotalNumberOfOperators());
        assertEquals(2, operators.get(OperatorType.ASSIGNMENT));
        assertEquals(1, operators.get(OperatorType.BINARY));
        assertEquals(1, operators.get(OperatorType.CONDITIONAL));
        assertEquals(3, operators.get(OperatorType.UNARY));
    }

    @Test
    public void testVisit_InstanceOfExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    String message = "Hello";
                    if (message instanceof String){
                        System.out.println(message);
                    }
                }
                """);
        bd.accept(visitor, null);

        Map<OperatorType, Integer> operators = visitor.getOperatorsPerMethod();
        assertEquals(2, visitor.getNumberOfUniqueOperators());
        assertEquals(2, visitor.getTotalNumberOfOperators());
        assertEquals(1, operators.get(OperatorType.TYPE_COMPARISON));
    }
}
