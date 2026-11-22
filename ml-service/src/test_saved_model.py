from pathlib import Path
import pandas as pd
import joblib

from sklearn.model_selection import train_test_split
from sklearn.metrics import (
    accuracy_score,
    precision_score,
    recall_score,
    f1_score,
    confusion_matrix
)

# 1. Find the project folder
BASE_DIR = Path(__file__).resolve().parents[1]

# 2. Find the dataset and saved model
dataset_path = BASE_DIR / "data" / "raw" / "cm1.csv"
model_path = BASE_DIR / "models" / "defect_prediction_model.joblib"

# 3. Load and prepare the dataset
df = pd.read_csv(dataset_path)
df = df.drop_duplicates().copy()

# 4. Separate features and target
X = df.drop(columns=["defects"])
y = df["defects"]

# 5. Recreate the same train-test split
X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.2,
    random_state=42,
    stratify=y
)

# 6. Load the saved model
saved_model = joblib.load(model_path)

# 7. Evaluate the saved model on ALL test records
y_pred = saved_model.predict(X_test)

print("\nSAVED MODEL EVALUATION")
print("Accuracy:", round(accuracy_score(y_test, y_pred), 4))

print(
    "Precision:",
    round(
        precision_score(
            y_test, y_pred, pos_label=True, zero_division=0
        ),
        4
    )
)

print(
    "Recall:",
    round(
        recall_score(
            y_test, y_pred, pos_label=True, zero_division=0
        ),
        4
    )
)

print(
    "F1-score:",
    round(
        f1_score(
            y_test, y_pred, pos_label=True, zero_division=0
        ),
        4
    )
)

print("\nCONFUSION MATRIX")
print(confusion_matrix(y_test, y_pred, labels=[False, True]))

# 8. Test ONE record separately
sample = X_test.iloc[[0]]

prediction = saved_model.predict(sample)[0]
actual = y_test.iloc[0]

probabilities = saved_model.predict_proba(sample)[0]
classes = saved_model.classes_

print("\nSINGLE RECORD PREDICTION")
print("Predicted defect label:", prediction)
print("Actual defect label:", actual)
print("Was the prediction correct?", prediction == actual)

print("\nPREDICTED PROBABILITIES:")
for label, probability in zip(classes, probabilities):
    print(f"{label}: {probability:.2%}")