package de.uni_passau.fim.se2.sa.readability.utils;

import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;

public class Classify {

    /**
     * Loads the {@link Instances} dataset by parsing the CSV file specified via the cli.
     *
     * @param data the CSV file to load.
     * @return the {@link Instances} dataset ready to be classified.
     * @throws IOException if the CSV file specified via the cli could not be loaded.
     */
    public static Instances loadDataset(File data) throws IOException {
        throw new UnsupportedOperationException("Implement me");
    }

    /**
     * Trains and evaluates the "logistic" classifier on the given dataset.
     * For the evaluation, we apply a 10-fold cross-validation using a start seed with a value of 1.
     *
     * @param dataset The dataset to train and evaluate the logistic classifier on.
     * @return the evaluation object hosting the evaluation results.
     * @throws Exception if the classifier could not be generated successfully.
     */
    public static Evaluation trainAndEvaluate(Instances dataset) throws Exception {
        throw new UnsupportedOperationException("Implement me");
    }
}
