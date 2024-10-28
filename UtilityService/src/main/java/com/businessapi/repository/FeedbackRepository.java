package com.businessapi.repository;

import com.businessapi.entities.Feedback;
import com.businessapi.entities.enums.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    Feedback findByAuthId(Long authId);
    Optional<Feedback> findByAuthIdAndStatus(Long authId, EStatus status);
    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double findAverageRating();
}
