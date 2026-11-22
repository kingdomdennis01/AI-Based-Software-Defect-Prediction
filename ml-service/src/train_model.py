from pathlib import Path
import pandas as pd
import joblib

from sklearn.model_selection import (
    train_test_split,
    StratifiedKFold,
    cross_validate
)

from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import StandardScaler

from sklearn.metrics import (
    accuracy_score,
    precision_score,
    recall_score,
    f1_score,
    confusion_matrix,
    make_scorer
)
# Find and load the dataset
dataset_path = (
        Path(__file__).resolve().parents[1]
        / "data"
        / "raw"
        / "cm1.csv"
)

df = pd.read_csv(dataset_path)

# Check the original dataset
print("\n--- DATASET CHECK ---")
print("Original dataset shape:", df.shape)
print("Exact duplicate rows:", df.duplicated().sum())

# Create a temporary dataset without exact duplicate rows
df_model = df.drop_duplicates().copy()

print("Dataset shape after duplicate removal:", df_model.shape)

print("\nClass distribution after duplicate removal:")
print(df_model["defects"].value_counts())

# Separate features and target
X = df_model.drop(columns=["defects"])
y = df_model["defects"]

# Split the data
X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.2,
    random_state=42,
    stratify=y
)
# Create the machine learning model
model = DecisionTreeClassifier(
    class_weight="balanced",
    random_state=42
)

# Train the model
model.fit(X_train, y_train)

print("Model training completed successfully!")

# Make predictions on the test data
y_pred = model.predict(X_test)

# Evaluate the model
accuracy = accuracy_score(y_test, y_pred)
precision = precision_score(
    y_test, y_pred, pos_label=True, zero_division=0
)
recall = recall_score(
    y_test, y_pred, pos_label=True, zero_division=0
)
f1 = f1_score(
    y_test, y_pred, pos_label=True, zero_division=0
)

# Display the results
print("\nMODEL EVALUATION RESULTS")
print("Accuracy:", round(accuracy, 4))
print("Precision:", round(precision, 4))
print("Recall:", round(recall, 4))
print("F1-score:", round(f1, 4))

print("\nCONFUSION MATRIX")
print(confusion_matrix(y_test, y_pred, labels=[False, True]))
# Create the Random Forest model
rf_model = RandomForestClassifier(
    n_estimators=100,
    class_weight="balanced",
    random_state=42
)

# Train the Random Forest model
rf_model.fit(X_train, y_train)

# Make predictions
rf_pred = rf_model.predict(X_test)

# Evaluate the model
print("\nRANDOM FOREST RESULTS")
print("Accuracy:", round(accuracy_score(y_test, rf_pred), 4))
print("Precision:", round(
    precision_score(y_test, rf_pred, pos_label=True, zero_division=0), 4
))
print("Recall:", round(
    recall_score(y_test, rf_pred, pos_label=True, zero_division=0), 4
))
print("F1-score:", round(
    f1_score(y_test, rf_pred, pos_label=True, zero_division=0), 4
))

print("\nRANDOM FOREST CONFUSION MATRIX")
print(confusion_matrix(y_test, rf_pred, labels=[False, True]))

# Create the Logistic Regression model
lr_model = make_pipeline(
    StandardScaler(),
    LogisticRegression(
        class_weight="balanced",
        max_iter=2000
    )
)

# Train the model
lr_model.fit(X_train, y_train)

# Make predictions
lr_pred = lr_model.predict(X_test)

# Evaluate the model
print("\nLOGISTIC REGRESSION RESULTS")
print("Accuracy:", round(accuracy_score(y_test, lr_pred), 4))
print("Precision:", round(
    precision_score(y_test, lr_pred, pos_label=True, zero_division=0), 4
))
print("Recall:", round(
    recall_score(y_test, lr_pred, pos_label=True, zero_division=0), 4
))
print("F1-score:", round(
    f1_score(y_test, lr_pred, pos_label=True, zero_division=0), 4
))

print("\nLOGISTIC REGRESSION CONFUSION MATRIX")
print(confusion_matrix(y_test, lr_pred, labels=[False, True]))

# Compare the three models
comparison = pd.DataFrame({
    "Model": [
        "Decision Tree",
        "Random Forest",
        "Logistic Regression"
    ],
    "Accuracy": [
        accuracy_score(y_test, y_pred),
        accuracy_score(y_test, rf_pred),
        accuracy_score(y_test, lr_pred)
    ],
    "Precision": [
        precision_score(y_test, y_pred, pos_label=True, zero_division=0),
        precision_score(y_test, rf_pred, pos_label=True, zero_division=0),
        precision_score(y_test, lr_pred, pos_label=True, zero_division=0)
    ],
    "Recall": [
        recall_score(y_test, y_pred, pos_label=True, zero_division=0),
        recall_score(y_test, rf_pred, pos_label=True, zero_division=0),
        recall_score(y_test, lr_pred, pos_label=True, zero_division=0)
    ],
    "F1-score": [
        f1_score(y_test, y_pred, pos_label=True, zero_division=0),
        f1_score(y_test, rf_pred, pos_label=True, zero_division=0),
        f1_score(y_test, lr_pred, pos_label=True, zero_division=0)
    ]
})

print("\nMODEL COMPARISON TABLE")
print(comparison.round(4).to_string(index=False))

# Set up 5-fold stratified cross-validation
cv = StratifiedKFold(
    n_splits=5,
    shuffle=True,
    random_state=42
)

# Define evaluation metrics
scoring = {
    "accuracy": "accuracy",
    "precision": make_scorer(
        precision_score, pos_label=True, zero_division=0
    ),
    "recall": make_scorer(
        recall_score, pos_label=True, zero_division=0
    ),
    "f1": make_scorer(
        f1_score, pos_label=True, zero_division=0
    )
}

# Compare the three models using training data
models = {
    "Decision Tree": model,
    "Random Forest": rf_model,
    "Logistic Regression": lr_model
}

print("\n5-FOLD CROSS-VALIDATION RESULTS")

for name, estimator in models.items():
    results = cross_validate(
        estimator,
        X_train,
        y_train,
        cv=cv,
        scoring=scoring
    )

    print(f"\n{name}")

    for metric in scoring:
        scores = results[f"test_{metric}"]
        print(
            f"{metric.title()}: "
            f"{scores.mean():.4f} "
            f"(standard deviation: {scores.std():.4f})"
        )

 # Save the Logistic Regression model
model_dir = Path(__file__).resolve().parents[1] / "models"
model_dir.mkdir(parents=True, exist_ok=True)

model_path = model_dir / "defect_prediction_model.joblib"

joblib.dump(lr_model, model_path)

print("\nMODEL SAVED SUCCESSFULLY!")
print("Saved to:", model_path)