package de.uni_passau.fim.se2.sa.readability.features;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.BodyDeclaration;
import de.uni_passau.fim.se2.sa.readability.utils.OperandVisitor;
import de.uni_passau.fim.se2.sa.readability.utils.OperatorVisitor;
import de.uni_passau.fim.se2.sa.readability.utils.Parser;

import static de.uni_passau.fim.se2.sa.readability.utils.Math.halsteadVolume;

public class HalsteadVolumeFeature extends FeatureMetric {

    /**
     * Computes the Halstead Volume metric based on the given code snippet.
     *
     * @return Halstead Volume of the given code snippet.
     */
    @Override
    public double computeMetric(String codeSnippet) {
        BodyDeclaration<?> bodyDeclaration;
        try {
            bodyDeclaration = Parser.parseJavaSnippet(codeSnippet);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        OperatorVisitor operatorVisitor = new OperatorVisitor();
        OperandVisitor operandVisitor = new OperandVisitor();
        bodyDeclaration.accept(operatorVisitor, null);
        bodyDeclaration.accept(operandVisitor, null);

        return halsteadVolume(operatorVisitor, operandVisitor);
    }

    @Override
    public String getIdentifier() {
        return "HalsteadVolume";
    }
}
