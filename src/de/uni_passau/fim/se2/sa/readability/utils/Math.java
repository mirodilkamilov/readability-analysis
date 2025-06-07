package de.uni_passau.fim.se2.sa.readability.utils;

import java.util.Map;

public class Math {
    public static double log2(double x) {
        return java.lang.Math.log(x) / java.lang.Math.log(2);
    }

    public static double tokenEntropy(Map<String, Integer> tokenDictionary, long totalNumOfTokens) {
        double tokenEntropy = 0.0;
        for (Map.Entry<String, Integer> token : tokenDictionary.entrySet()) {
            double probOfEncounteringToken = (double) token.getValue() / totalNumOfTokens;
            tokenEntropy -= probOfEncounteringToken * log2(probOfEncounteringToken);
        }
        return tokenEntropy;
    }

    public static double halsteadVolume(OperatorVisitor operatorVisitor, OperandVisitor operandVisitor) {
        int programLength = operatorVisitor.getTotalNumberOfOperators() + operandVisitor.getTotalNumberOfOperands();
        int programVocabulary = operatorVisitor.getNumberOfUniqueOperators() + operandVisitor.getNumberOfUniqueOperands();
        return programLength * log2(programVocabulary);
    }
}
