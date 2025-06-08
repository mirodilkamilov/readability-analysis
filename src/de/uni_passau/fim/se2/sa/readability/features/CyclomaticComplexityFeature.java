package de.uni_passau.fim.se2.sa.readability.features;


import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.BodyDeclaration;
import de.uni_passau.fim.se2.sa.readability.utils.CyclomaticComplexityVisitor;
import de.uni_passau.fim.se2.sa.readability.utils.Parser;

public class CyclomaticComplexityFeature extends FeatureMetric {

    /**
     * Computes the cyclomatic complexity metric based on the given code snippet.
     *
     * @return Cyclomatic complexity of the given code snippet.
     */
    @Override
    public double computeMetric(String codeSnippet) {
        if (codeSnippet == null || codeSnippet.isBlank()) {
            return 1.0;
        }

        BodyDeclaration<?> bodyDeclaration;
        try {
            bodyDeclaration = Parser.parseJavaSnippet(codeSnippet);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        CyclomaticComplexityVisitor complexityVisitor = new CyclomaticComplexityVisitor();
        bodyDeclaration.accept(complexityVisitor, null);

        return complexityVisitor.getComplexity();
    }

    @Override
    public String getIdentifier() {
        return "CyclomaticComplexity";
    }
}