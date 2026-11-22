package com.aidefectprediction.backend.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "prediction_records")
public class PredictionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "input_features_json", nullable = false, length = 4000)
    private String inputFeaturesJson;

    @Column(name = "predicted_defect", nullable = false)
    private boolean predictedDefect;

    @Column(name = "defect_probability", nullable = false)
    private double defectProbability;

    @Column(name = "model_name", nullable = false, length = 100)
    private String modelName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public PredictionRecord() {
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getInputFeaturesJson() {
        return inputFeaturesJson;
    }

    public void setInputFeaturesJson(String inputFeaturesJson) {
        this.inputFeaturesJson = inputFeaturesJson;
    }

    public boolean isPredictedDefect() {
        return predictedDefect;
    }

    public void setPredictedDefect(boolean predictedDefect) {
        this.predictedDefect = predictedDefect;
    }

    public double getDefectProbability() {
        return defectProbability;
    }

    public void setDefectProbability(double defectProbability) {
        this.defectProbability = defectProbability;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
