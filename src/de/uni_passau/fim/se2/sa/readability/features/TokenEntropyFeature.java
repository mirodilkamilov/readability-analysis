package de.uni_passau.fim.se2.sa.readability.features;

import com.github.javaparser.JavaToken;
import com.github.javaparser.ParseException;
import de.uni_passau.fim.se2.sa.readability.utils.Parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class TokenEntropyFeature extends FeatureMetric {

    public static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    /**
     * Computes the entropy metric based on the tokens of the given code snippet.
     * Since we are interested in the readability of code as perceived by a human, tokens also include whitespaces and the like.
     *
     * @return token entropy of the given code snippet.
     */
    @Override
    public double computeMetric(String codeSnippet) {
        if (codeSnippet == null || codeSnippet.isBlank()) {
            return 0.0;
        }

        Map<String, Integer> tokenDictionary = new HashMap<>();
        long totalNumOfTokens = 0L;
        Iterator<JavaToken> tokenIterator;

        try {
            tokenIterator = Parser.parseJavaSnippet(codeSnippet)
                    .getTokenRange()
                    .orElseThrow()
                    .iterator();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Token extraction failed: Token range is not available.");
        }

        while (tokenIterator.hasNext()) {
            String currentToken = tokenIterator.next().getText();
            Integer numOfOccurrences = tokenDictionary.get(currentToken);

            if (numOfOccurrences == null) {
                numOfOccurrences = 1;
            } else {
                numOfOccurrences++;
            }

            tokenDictionary.put(currentToken, numOfOccurrences);
            totalNumOfTokens++;
        }

        double tokenEntropy = 0.0;
        for (Map.Entry<String, Integer> token : tokenDictionary.entrySet()) {
            double probOfEncounteringToken = (double) token.getValue() / totalNumOfTokens;
            tokenEntropy -= probOfEncounteringToken * log2(probOfEncounteringToken);
        }

        return tokenEntropy;
    }

    @Override
    public String getIdentifier() {
        return "TokenEntropy";
    }
}
