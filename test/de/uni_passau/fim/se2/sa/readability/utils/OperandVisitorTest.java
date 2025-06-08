package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.BodyDeclaration;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperandVisitorTest {
    final OperandVisitor visitor = new OperandVisitor();

    @Test
    public void testVisit_BooleanLiteralExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void helloWorld() {
                    boolean isValid = false;
                    String message = "false";
                    isValid = true;
                }
                """);
        bd.accept(visitor, null);

        Map<String, Integer> operands = visitor.getOperandsPerMethod();
        assertEquals(6, visitor.getNumberOfUniqueOperands());
        assertEquals(8, visitor.getTotalNumberOfOperands());
        assertEquals(2, operands.get("false"));
        assertEquals(1, operands.get("true"));
    }

    @Test
    public void testVisit_CharLiteralExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    Char ch;
                    ch = 'H';
                }
                """);
        bd.accept(visitor, null);

        Map<String, Integer> operands = visitor.getOperandsPerMethod();
        assertEquals(4, visitor.getNumberOfUniqueOperands());
        assertEquals(5, visitor.getTotalNumberOfOperands());
        assertEquals(1, operands.get("H"));
    }

    @Test
    public void testVisit_NumberLiteralExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    int x = 1;
                    Integer y = 0;
                    Double z = 3.1415;
                    z = null;
                    long l = 10L;
                    System.out.println(30 + x);
                }
                """);
        bd.accept(visitor, null);

        assertEquals(16, visitor.getNumberOfUniqueOperands());
        assertEquals(18, visitor.getTotalNumberOfOperands());
    }

    @Test
    public void testVisit_StringLiteralExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    String message = "sdsds";
                    // "comment"
                    message = "another";
                    message = null;
                    System.out.println("30 + x");
                }
                """);
        bd.accept(visitor, null);

        assertEquals(10, visitor.getNumberOfUniqueOperands());
        assertEquals(12, visitor.getTotalNumberOfOperands());
    }

    @Test
    public void testVisit_NullLiteralExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    String message = "sdsds";
                    // "comment"
                    message = "null";
                    message = null;
                    message = NULL;
                    System.out.println("30 + x");
                }
                """);
        bd.accept(visitor, null);

        assertEquals(9, visitor.getNumberOfUniqueOperands());
        assertEquals(14, visitor.getTotalNumberOfOperands());
    }

    @Test
    public void testVisit_ConstructorDeclaration() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public class HelloWorld{
                    public HelloWorld(){
                        super();
                        System.out.println("Constructor Called");
                    }
                }
                """);
        bd.accept(visitor, null);

        assertEquals(5, visitor.getNumberOfUniqueOperands());
        assertEquals(5, visitor.getTotalNumberOfOperands());
    }

    @Test
    public void testVisit_MemberValuePair() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                @Counters(a=15, b=4)
                public void helloWorld(){
                    System.out.println("");
                }
                """);
        bd.accept(visitor, null);

        assertEquals(9, visitor.getNumberOfUniqueOperands());
        assertEquals(9, visitor.getTotalNumberOfOperands());
    }

    @Test
    public void testVisit_TypeParameter() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public <T> helloWorld(T type){
                    List<Integer> list;
                    if (list instanceof ArrayList) {
                        for (int i = 0; i < 10; i++) {
                            System.out.println(i);
                        }
                    }
                    return type;
                }
                """);
        bd.accept(visitor, null);

        assertEquals(13, visitor.getNumberOfUniqueOperands());
        assertEquals(19, visitor.getTotalNumberOfOperands());
    }

    @Test
    public void testVisit_BreakStmt() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void helloWorld(int a){
                    switch(a){
                        case 1: doSmth();
                        case 2: doSmthElse(); break;
                        default: whatever();
                    }
                
                    _loop119:
                    do {
                        if ((_tokenSet_6.member(LA(1)))) {
                            statement();
                        }
                        else {
                            break _loop119;
                        }
                
                    } while (true);
                }
                """);
        bd.accept(visitor, null);

        assertEquals(13, visitor.getNumberOfUniqueOperands());
        assertEquals(16, visitor.getTotalNumberOfOperands());
    }

    @Test
    public void testVisit_ClassOrInterfaceType() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                     Object[] listeners = listenerList.getListenerList();
                     TreeModelEvent e = null;
                     for (int i = listeners.length - 2; i >= 0; i -= 2) {
                         if (listeners[i] == TreeModelListener.class) {
                             if (e == null)
                                 e = new TreeModelEvent(
                                   source,
                                   path,
                                   childIndices,
                                   children);
                             ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
                         }
                     }
                }
                """);
        bd.accept(visitor, null);

        assertEquals(19, visitor.getNumberOfUniqueOperands());
        assertEquals(33, visitor.getTotalNumberOfOperands());
    }

    @Test
    public void testVisit_NameExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                     Object[] listeners = listenerList.getListenerList();
                     TreeModelEvent e = null;
                     for (int i = listeners.length - 2; i >= 0; i -= 2) {
                         if (listeners[i] == TreeModelListener.class) {
                             if (e == null)
                                 e = new TreeModelEvent(
                                   source,
                                   path,
                                   childIndices,
                                   children);
                             ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
                         }
                     }
                }
                """);
        bd.accept(visitor, null);

        assertEquals(19, visitor.getNumberOfUniqueOperands());
        assertEquals(33, visitor.getTotalNumberOfOperands());
    }
}
