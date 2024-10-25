package com.businessapi.services;

import com.businessapi.dto.request.FeedbackSaveRequestDTO2;
import com.businessapi.dto.response.FeedbackReportDTO;
import com.businessapi.entities.Feedback;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.UtilityServiceException;
import com.businessapi.repository.FeedbackRepository;
import com.businessapi.util.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public Boolean saveFeedback(FeedbackSaveRequestDTO2 dto) {
        feedbackRepository.save(Feedback
                .builder()
                .description(dto.description())
                .rating(dto.rating())
                .authId(SessionManager.getMemberIdFromAuthenticatedMember())
                .build());

        return true;
    }

    public Boolean deleteFeedback() {
        Long authId = SessionManager.getMemberIdFromAuthenticatedMember();
        Feedback feedback = feedbackRepository.findByAuthId(authId);
        if (feedback == null) {
            throw new UtilityServiceException(ErrorType.FEEDBACK_NOT_FOUND);
        }
        feedback.setStatus(EStatus.DELETED);
        feedbackRepository.save(feedback);
        return true;

    }

    public Boolean updateFeedback(FeedbackSaveRequestDTO2 dto) {
        Long authId = SessionManager.getMemberIdFromAuthenticatedMember();

        Feedback feedback = feedbackRepository.findByAuthId(authId);



        if (feedback == null) {
            throw new UtilityServiceException(ErrorType.FEEDBACK_NOT_FOUND);
        }
        if (feedback.getStatus() == EStatus.DELETED) {
            throw new UtilityServiceException(ErrorType.FEEDBACK_ALREADY_DELETED);
        }



        feedback.setDescription(dto.description() != null ? dto.description() : feedback.getDescription());
        feedback.setRating(dto.rating() != null ? dto.rating() : feedback.getRating());
        feedbackRepository.save(feedback);
        return true;
    }

    public FeedbackReportDTO getFeedbackReport() {
        Long totalFeedbacks = feedbackRepository.count();
        Double averageRating = feedbackRepository.findAverageRating();

        return new FeedbackReportDTO(totalFeedbacks, averageRating);
    }

    public Feedback getFeedbackByUser() {
        Long authId = SessionManager.getMemberIdFromAuthenticatedMember();
        return feedbackRepository.findByAuthId(authId);

    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }
}




