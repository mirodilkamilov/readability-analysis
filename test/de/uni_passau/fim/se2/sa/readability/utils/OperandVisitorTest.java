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
    public void testVisit_LocalClassDeclarationStmt() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                @Test public void testHHH1780() throws Exception {
                    // verifies the tree contains a NOT->EXISTS subtree
                    class Verifier { }
                }
                """);
        bd.accept(visitor, null);

        assertEquals(3, visitor.getNumberOfUniqueOperands());
        assertEquals(3, visitor.getTotalNumberOfOperands());
    }

    @Test
    public void testVisit_VariableDeclarator() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    String message = "sdsds";
                    int x = 14, y = 3;
                }
                """);
        bd.accept(visitor, null);

        assertEquals(8, visitor.getNumberOfUniqueOperands());
        assertEquals(8, visitor.getTotalNumberOfOperands());
    }

    @Test
    public void testVisit_MethodCallExpr() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public void printMessage(){
                    for (int i = 0; i < files.length; i++) {
                          File file = files[i];
                          String fileName = file.getName();
                          if (file.isFile() && fileName.endsWith(".jsnp")) {
                              String codeSnippet = Files.readString(Path.of(file.toURI()));
                              addCsvEntry(csv, fileName);
                
                              for (FeatureMetric featureMetric : featureMetrics) {
                                  addCsvEntry(csv, featureMetric.computeMetric(codeSnippet));
                              }
                          }
                
                          String truthLabel = Double.parseDouble(meanScores[i]) >= TRUTH_THRESHOLD ? "Y" : "N";
                          csv.append(truthLabel);
                          if (i != files.length - 1) {
                              csv.append("\\n");
                          }
                      }
                }
                """);
        bd.accept(visitor, null);

        assertEquals(35, visitor.getNumberOfUniqueOperands());
        assertEquals(58, visitor.getTotalNumberOfOperands());
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

    @Test
    public void testVisit_EverythingMixed() throws ParseException {
        BodyDeclaration<?> bd = Parser.parseJavaSnippet("""
                public <T> void testMethod(T type){
                    int a = 10;
                    int b = 20;
                    boolean isValid = true;
                    char label = 'Y';
                    List<T> list = new ArrayList<>();
                
                    if (a < b && isValid == false) {
                        System.out.println("a is less than b");
                        System.out.println(1000);
                        System.out.println(3.1415);
                        System.out.println(31415L);
                    }
                
                    for (int i = 0; i < 5; i++) {
                        System.out.println("For loop i = " + i);
                    }
                
                    int i = 0;
                    while (i < 3) {
                        System.out.println("While loop i = " + i);
                        i++;
                    }
                
                    int j = 0;
                    do {
                        System.out.println("Do-while loop j = " + j);
                        j++;
                        break;
                    } while (j < 2);
                
                    int day = 2;
                    switch (day) {
                        case 1:
                            System.out.println("Monday");
                            break;
                        case 2:
                            System.out.println("Tuesday");
                            break;
                        default:
                            System.out.println("Another day");
                    }
                
                    try {
                        int result = 10 / 0;
                    } catch (ArithmeticException e) {
                        System.out.println("Caught an exception: " + e.getMessage());
                    }
                
                    List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
                    for (String name : names) {
                        System.out.println("Hello " + name);
                    }
                
                    String message = (a > b) ? "a is greater" : (a == b ? "a equals b" : "b is greater");
                    System.out.println("Ternary result: " + message);
                }
                """);
        bd.accept(visitor, null);

        assertEquals(55, visitor.getNumberOfUniqueOperands());
        assertEquals(124, visitor.getTotalNumberOfOperands());
    }
}
