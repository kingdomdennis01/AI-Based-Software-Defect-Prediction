package com.aidefectprediction.backend.repository;

import com.aidefectprediction.backend.entity.PredictionRecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredictionRecordRepository
        extends JpaRepository<PredictionRecord, Long> {

    List<PredictionRecord> findByUser_IdOrderByCreatedAtDesc(
            Long userId
    );
}
