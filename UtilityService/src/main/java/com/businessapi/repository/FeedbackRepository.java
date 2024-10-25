package com.businessapi.repository;

import com.businessapi.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    Feedback findByAuthId(Long authId);
    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double findAverageRating();
}
