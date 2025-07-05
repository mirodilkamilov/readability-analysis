# Readability Analysis — Software Analysis Course (University of Passau)

This project implements a **readability analysis tool** for Java methods using a binary logistic regression classifier.
The goal is to predict whether a Java method is *readable* or *not readable*, based on human-assessed ratings.

## Overview

The project consists of two main phases:

1. **Preprocessing Phase**

    * Extract features from Java methods:
        * **LINES** — Number of lines in the method
        * **TOKEN\_ENTROPY** — Shannon entropy of the tokens
        * **H\_VOLUME** — Halstead volume metric
        * **CYCLOMATIC\_COMPLEXITY** — McCabe's cyclomatic complexity
    * Generate a CSV dataset mapping each method to its features and readability label.

2. **Training & Evaluation Phase**

    * Train a **binary logistic regression model** using the WEKA library.
    * Evaluate the model on the generated dataset.

For the complete task description, please refer
to [Readability_Analysis_Software_Analysis(SS25).pdf](Readability_Analysis_Software_Analysis%28SS25%29.pdf)

## Dataset

* Input: 200 `.jsnp` Java method files with corresponding ground truth labels.
* Output: Feature-enriched CSV dataset for training and evaluation.

## Achieved Test Coverage

* **Line Coverage:** 90%
* **Branch Coverage:** 90%
* **Mutation Score:** 80%
