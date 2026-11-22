from pathlib import Path
import pandas as pd
from sklearn.model_selection import train_test_split

# Find the dataset inside the project folder
dataset_path = (
        Path(__file__).resolve().parents[1]
        / "data"
        / "raw"
        / "cm1.csv"
)

# Check whether the dataset exists
if not dataset_path.exists():
    raise FileNotFoundError(
        f"Dataset not found at: {dataset_path}"
    )

# Load the dataset
df = pd.read_csv(dataset_path)

# Display the first five rows
print("FIRST FIVE ROWS:")
print(df.head())

# Display dataset size
print("\nDATASET SIZE:")
print(f"Rows: {df.shape[0]}")
print(f"Columns: {df.shape[1]}")

# Display column names
print("\nCOLUMN NAMES:")
print(df.columns.tolist())

# Check missing values
print("\nMISSING VALUES:")
print(df.isnull().sum())
print("\nDEFECT LABEL COUNTS:")
print(df["defects"].value_counts())
print("\nDEFECT LABEL PERCENTAGES:")
print(df["defects"].value_counts(normalize=True) * 100)
print("\nFEATURES AND TARGET:")

X = df.drop(columns=["defects"])
y = df["defects"]
X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.2,
    random_state=42,
    stratify=y
)

print("\nTRAINING AND TESTING DATA:")
print("X_train:", X_train.shape)
print("X_test:", X_test.shape)
print("y_train:", y_train.shape)
print("y_test:", y_test.shape)

print("Features shape:", X.shape)
print("Target shape:", y.shape)

print("\nDATA TYPES:")
print(df.dtypes)

print("\nDUPLICATE ROWS:")
print(df.duplicated().sum())

# Inspect duplicate records
duplicate_rows = df[df.duplicated(keep=False)]

print("\nDUPLICATE RECORDS - FIRST 10 ROWS:")
print(duplicate_rows.head(10).to_string(index=False))

print("\nTOTAL RECORDS IN DUPLICATE GROUPS:")
print(len(duplicate_rows))

# Create a cleaned copy without exact duplicate rows
df_clean = df.drop_duplicates()

print("\nDATASET BEFORE AND AFTER DUPLICATE REMOVAL:")
print("Original rows:", len(df))
print("Rows after removing duplicates:", len(df_clean))

print("\nDEFECT LABEL COUNTS AFTER DUPLICATE REMOVAL:")
print(df_clean["defects"].value_counts())

print("\nDEFECT LABEL PERCENTAGES AFTER DUPLICATE REMOVAL:")
print((df_clean["defects"].value_counts(normalize=True) * 100).round(2))