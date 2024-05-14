package de.uni_passau.fim.se2.sa.readability.features;


public class CyclomaticComplexityFeature extends FeatureMetric {

    /**
     * Computes the cyclomatic complexity metric based on the given code snippet.
     *
     * @return Cyclomatic complexity of the given code snippet.
     */
    @Override
    public double computeMetric(String codeSnippet) {
        // Implement the CyclomaticComplexityFeature using the CyclomaticComplexityVisitor
        throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public String getIdentifier() {
        return "CyclomaticComplexity";
    }
}