from pathlib import Path

import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel


# 1. Find and load the saved machine-learning model
BASE_DIR = Path(__file__).resolve().parents[1]

MODEL_PATH = (
        BASE_DIR / "models" / "defect_prediction_model.joblib"
)

model = joblib.load(MODEL_PATH)


# 2. Define the input features required by the model
FEATURE_COLUMNS = [
    "loc", "v(g)", "ev(g)", "iv(g)", "n", "v", "l", "d", "i",
    "e", "b", "t", "lOCode", "lOComment", "lOBlank",
    "locCodeAndComment", "uniq_Op", "uniq_Opnd",
    "total_Op", "total_Opnd", "branchCount"
]


# 3. Create the API application
app = FastAPI(
    title="AI-Based Software Defect Prediction API"
)


# 4. Define the home endpoint
@app.get("/")
def home():
    return {
        "message": "Software Defect Prediction API is running"
    }


# 5. Define the expected prediction request
class PredictionRequest(BaseModel):
    features: dict[str, float]


# 6. Create the prediction endpoint
@app.post("/predict")
def predict(request: PredictionRequest):

    missing = [
        column for column in FEATURE_COLUMNS
        if column not in request.features
    ]

    extra = [
        column for column in request.features
        if column not in FEATURE_COLUMNS
    ]

    if missing:
        raise HTTPException(
            status_code=422,
            detail={"missing_features": missing}
        )

    if extra:
        raise HTTPException(
            status_code=422,
            detail={"unexpected_features": extra}
        )

    # Arrange features in the order used during training
    input_data = pd.DataFrame(
        [[request.features[column] for column in FEATURE_COLUMNS]],
        columns=FEATURE_COLUMNS
    )

    # Predict the defect label
    prediction = bool(model.predict(input_data)[0])

    # Calculate the estimated probability of a defect
    probabilities = model.predict_proba(input_data)[0]
    classes = list(model.classes_)
    defect_index = classes.index(True)

    defect_probability = float(probabilities[defect_index])

    # Return the result as JSON
    return {
        "predicted_defect": prediction,
        "probability_of_defect": round(defect_probability, 4),
        "model": "Logistic Regression"
    }

