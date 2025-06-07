package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public class OperandVisitor extends VoidVisitorAdapter<Void> {

    /**
     * Maps operand names to the number of their occurrences in the given code snippet.
     */
    private final Map<String, Integer> operandsPerMethod;

    public OperandVisitor() {
        operandsPerMethod = new HashMap<>();
    }

    public Map<String, Integer> getOperandsPerMethod() {
        return operandsPerMethod;
    }

    public int getNumberOfUniqueOperands() {
        return operandsPerMethod.size();
    }

    public int getTotalNumberOfOperands() {
        return operandsPerMethod.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public void visit(final BooleanLiteralExpr n, final Void arg) {
        addOperand(String.valueOf(n.getValue()));
        super.visit(n, arg);
    }

    @Override
    public void visit(final CharLiteralExpr n, final Void arg) {
        addOperand(n.getValue());
        super.visit(n, arg);
    }

    @Override
    public void visit(final IntegerLiteralExpr n, final Void arg) {
        addOperand(n.getValue());
        super.visit(n, arg);
    }

    @Override
    public void visit(final DoubleLiteralExpr n, final Void arg) {
        addOperand(n.getValue());
        super.visit(n, arg);
    }

    @Override
    public void visit(final LongLiteralExpr n, final Void arg) {
        addOperand(n.getValue());
        super.visit(n, arg);
    }

    @Override
    public void visit(final StringLiteralExpr n, final Void arg) {
        addOperand(n.getValue());
        super.visit(n, arg);
    }

    @Override
    public void visit(final NullLiteralExpr n, final Void arg) {
        addOperand("null");
        super.visit(n, arg);
    }

    @Override
    public void visit(final MethodDeclaration n, final Void arg) {
        addOperand(n.getNameAsString());
        super.visit(n, arg);
    }

    // Counting single identifiers
    @Override
    public void visit(final VariableDeclarator n, final Void arg) {
        addOperand(n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(final MethodCallExpr n, final Void arg) {
        addOperand(n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(final ClassOrInterfaceType n, final Void arg) {
        addOperand(n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(final FieldAccessExpr n, final Void arg) {
        addOperand(n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(final Parameter n, final Void arg) {
        addOperand(n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(final NameExpr n, final Void arg) {
        if (!n.hasParentNode() || n.getParentNode().isEmpty()) {
            super.visit(n, arg);
            return;
        }

        String nodeName = n.getNameAsString();
        nodeName = nodeName.equalsIgnoreCase("null") ? "null" : nodeName;
        addOperand(nodeName);

        super.visit(n, arg);
    }

    private void addOperand(String operandName) {
        operandsPerMethod.merge(operandName, 1, Integer::sum);
    }
}
