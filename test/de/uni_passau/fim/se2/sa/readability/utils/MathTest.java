package de.uni_passau.fim.se2.sa.readability.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.log;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathTest {
    @Test
    void testLog2() {
        assertEquals(1.0, Math.log2(2), 1e-9);
        assertEquals(2.0, Math.log2(4), 1e-9);
        assertEquals(3.0, Math.log2(8), 1e-9);
        assertEquals(0.0, Math.log2(1), 1e-9);
    }

    @Test
    void testTokenEntropy_singleToken() {
        Map<String, Integer> tokens = new HashMap<>();
        tokens.put("a", 10);
        double entropy = Math.tokenEntropy(tokens, 10);
        assertEquals(0.0, entropy, 1e-9);
    }

    @Test
    void testTokenEntropy_multipleTokens() {
        Map<String, Integer> tokens = new HashMap<>();
        tokens.put("a", 1);
        tokens.put("b", 1);
        double entropy = Math.tokenEntropy(tokens, 2);
        double expected = -2 * (0.5 * log(0.5) / log(2));
        assertEquals(expected, entropy, 1e-9);
    }

    @Test
    void testTokenEntropy_realisticCase() {
        Map<String, Integer> tokens = new HashMap<>();
        tokens.put("width", 5);
        tokens.put("labelLinkPaint", 3);
        tokens.put("rangeGridlinePaint", 2);
        long total = 10;
        double entropy = Math.tokenEntropy(tokens, total);

        double expected =
                -(5.0 / 10) * Math.log2(5.0 / 10)
                        - (3.0 / 10) * Math.log2(3.0 / 10)
                        - (2.0 / 10) * Math.log2(2.0 / 10);

        assertEquals(expected, entropy, 1e-9);
    }

    @Test
    void testHalsteadVolume() {
        OperatorVisitor operatorVisitor = new OperatorVisitor() {
            public int getTotalNumberOfOperators() {
                return 5;
            }

            public int getNumberOfUniqueOperators() {
                return 3;
            }
        };

        OperandVisitor operandVisitor = new OperandVisitor() {
            public int getTotalNumberOfOperands() {
                return 7;
            }

            public int getNumberOfUniqueOperands() {
                return 4;
            }
        };

        double expected = (5 + 7) * Math.log2(3 + 4);
        double volume = Math.halsteadVolume(operatorVisitor, operandVisitor);

        assertEquals(expected, volume, 1e-9);
    }
}
