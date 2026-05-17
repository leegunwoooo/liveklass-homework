package com.bamdoliro.homework.enrollment.application;

import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.domain.EnrollmentStatus;
import com.bamdoliro.homework.enrollment.infrastructure.EnrollmentRepository;
import com.bamdoliro.homework.enrollment.presentation.dto.response.EnrollmentResponse;
import com.bamdoliro.homework.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CancelEnrollmentUseCase {

    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public EnrollmentResponse cancelEnrollment(Long enrollmentId, User user) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청입니다ㅏ."));

        validIsApplicant(user, enrollment);

        enrollment.cancel();

        enrollmentRepository.findFirstByCourseAndStatusOrderByWaitlistCountAsc(enrollment.getCourse(), EnrollmentStatus.WAITED)
                .ifPresent(Enrollment::promote);

        return EnrollmentResponse.of(enrollment);
    }

    private void validIsApplicant(User user, Enrollment enrollment) {
        if(!enrollment.isApplicant(user.getId())){
            throw new IllegalArgumentException("자기자신만 취소할 수 있습니다.");
        }
    }
}
