package de.uni_passau.fim.se2.sa.readability.features;

public class NumberLinesFeature extends FeatureMetric {

    /**
     * Computes the number of lines of the given code snippet.
     * Since we are interested in determining the readability of a code snippet, this also includes comments.
     *
     * @return source code lines of the given code snippet.
     */
    @Override
    public double computeMetric(String codeSnippet) {
        if (codeSnippet == null || codeSnippet.isBlank()) {
            return 0.0;
        }

        if (codeSnippet.endsWith("\n") || codeSnippet.endsWith("\r")) {
            codeSnippet = codeSnippet.replaceFirst("\\R\\z", "");
        }

        return codeSnippet.split("\\R", -1).length;
    }

    @Override
    public String getIdentifier() {
        return "NumberLines";
    }
}
