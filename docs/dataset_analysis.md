# CM1 Dataset Inspection Report

## 1. Dataset Overview

The CM1 dataset was selected for the AI-Based Software Defect Prediction project. It contains software metrics that can be used as input features and a Boolean target label named `defects`.

**Dataset source:** https://github.com/ApoorvaKrisna/NASA-promise-dataset-repository/blob/main/cm1.csv

## 2. Dataset Structure

The original dataset contains 498 records and 22 columns. Of these, 21 columns represent software metrics, while the `defects` column represents the target label.

The dataset contains numeric feature columns and a Boolean target column.

## 3. Missing Values and Duplicates

The initial inspection identified no missing values in the dataset. However, 56 duplicate records beyond their first occurrences were identified. In total, 94 records belong to groups containing exact duplicates.

Removing exact duplicate rows in a temporary copy reduces the dataset from 498 to 442 records. The original dataset has been preserved because further investigation is needed to determine how these duplicates should be handled in the final model evaluation.

## 4. Target Label Distribution

In the original dataset:
- False: 449 records (90.16%)
- True: 49 records (9.84%)

After removing exact duplicate rows in a temporary copy:
- False: 394 records (89.14%)
- True: 48 records (10.86%)

This distribution indicates class imbalance, because records labelled `False` are much more common than records labelled `True`.

## 5. Feature Overview

The dataset contains several groups of software metrics:

- **McCabe metrics:** `loc`, `v(g)`, `ev(g)`, and `iv(g)`, which describe code size and different aspects of software complexity.
- **Halstead metrics:** `n`, `v`, `l`, `d`, `i`, `e`, `b`, `t`, `uniq_Op`, `uniq_Opnd`, `total_Op`, and `total_Opnd`, which describe software operators, operands, and derived measures.
- **Code line metrics:** `lOCode`, `lOComment`, `lOBlank`, and `locCodeAndComment`.
- **Branch metric:** `branchCount`, which describes branching within a module's control-flow graph.
- **Target label:** `defects`, which indicates whether the module has one or more reported defects.

## 6. Initial Conclusion

The CM1 dataset is suitable for an initial binary classification experiment because it contains numeric software metrics and a labelled target variable. However, class imbalance and exact duplicate records must be considered when evaluating model performance.

The original dataset will be preserved, and the effect of duplicate removal will be investigated before finalising the evaluation results.