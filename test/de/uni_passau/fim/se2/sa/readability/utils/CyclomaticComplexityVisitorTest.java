package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.BodyDeclaration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CyclomaticComplexityVisitorTest {
    final CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();

    @Test
    public void testVisit_IfStmt() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void helloWorld() {
                    if(a>0){}
                }
                """);
        bd.accept(visitor, null);
        assertEquals(2.0, visitor.getComplexity());
    }

    @Test
    public void testVisit_IfStmtWithMultipleConditions() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void helloWorld() {
                    if(a>0 && a<5 || a==3){}
                }
                """);
        bd.accept(visitor, null);
        assertEquals(4.0, visitor.getComplexity());
    }

    @Test
    public void testVisit_SwitchEntry() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void testMethod(){
                     switch(x) {
                          case 1: doSomething(); break;
                          case 2: doSomethingElse(); break;
                          default: fallback(); break;
                     }
                }
                """);
        bd.accept(visitor, null);
        assertEquals(3.0, visitor.getComplexity());
    }

    @Test
    public void testVisit_SwitchEntryWithoutCase() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void testMethod(){
                     switch(x) {
                          default: fallback(); break;
                     }
                }
                """);
        bd.accept(visitor, null);
        assertEquals(1.0, visitor.getComplexity());
    }

    @Test
    public void testVisit_Loops() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void testMethod(){
                     for (int i = 0; i < 10; i++) { }
                     while (y < 5) { }
                     do { } while(y < 5);
                }
                """);
        bd.accept(visitor, null);
        assertEquals(4.0, visitor.getComplexity());
    }

    @Test
    public void testVisit_CatchClause() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void testMethod() {
                    try {
                        int x = 1 / 0;
                    } catch (ArithmeticException e) {
                        System.out.println("Error");
                    }
                }
                """);
        bd.accept(visitor, null);
        assertEquals(2.0, visitor.getComplexity());
    }

    @Test
    public void testVisit_ForEachStmt() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void testMethod() {
                    for (int i : numbers) {
                        System.out.println(i);
                    }
                }
                """);
        bd.accept(visitor, null);
        assertEquals(2.0, visitor.getComplexity());
    }

    @Test
    public void testVisit_ConditionalExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void testMethod() {
                    int result = (a > b) ? a : b;
                }
                """);
        bd.accept(visitor, null);
        assertEquals(2.0, visitor.getComplexity());
    }
}
