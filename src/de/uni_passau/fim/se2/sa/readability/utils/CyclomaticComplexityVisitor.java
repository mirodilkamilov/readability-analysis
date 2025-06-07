package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {

    private int complexity = 1;

    public int getComplexity() {
        return complexity;
    }

    @Override
    public void visit(final IfStmt n, final Void arg) {
        complexity++;
        super.visit(n, arg);
    }

    @Override
    public void visit(final CatchClause n, final Void arg) {
        complexity++;
        super.visit(n, arg);
    }

    @Override
    public void visit(final ForStmt n, final Void arg) {
        complexity++;
        super.visit(n, arg);
    }

    @Override
    public void visit(final ForEachStmt n, final Void arg) {
        complexity++;
        super.visit(n, arg);
    }

    @Override
    public void visit(final WhileStmt n, final Void arg) {
        complexity++;
        super.visit(n, arg);
    }

    @Override
    public void visit(final DoStmt n, final Void arg) {
        complexity++;
        super.visit(n, arg);
    }

    @Override
    public void visit(final SwitchEntry n, final Void arg) {
        if (n.getTokenRange().isPresent() && !n.getTokenRange().get().getBegin()
                .getText()
                .equalsIgnoreCase("default")) {
            complexity++;
        }
        super.visit(n, arg);
    }

    @Override
    public void visit(final ConditionalExpr n, final Void arg) {
        complexity++;
        super.visit(n, arg);
    }

    @Override
    public void visit(final BinaryExpr n, final Void arg) {
        String operatorName = n.getOperator().name();
        if (operatorName.equalsIgnoreCase("and") || operatorName.equalsIgnoreCase("or")) {
            complexity++;
        }
        super.visit(n, arg);
    }

}