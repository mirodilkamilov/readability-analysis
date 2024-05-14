package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {

    private int complexity;

    public int getComplexity() {
        return complexity;
    }

}