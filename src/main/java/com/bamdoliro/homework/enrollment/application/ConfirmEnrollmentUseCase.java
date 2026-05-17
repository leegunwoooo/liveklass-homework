package com.bamdoliro.homework.enrollment.application;

import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.infrastructure.EnrollmentRepository;
import com.bamdoliro.homework.enrollment.presentation.dto.response.EnrollmentResponse;
import com.bamdoliro.homework.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ConfirmEnrollmentUseCase {

    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public EnrollmentResponse confirm(Long enrollmentId, User user) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강신청을 찾을 수 없습니다."));

        validIsApplicant(user, enrollment);

        enrollment.confirm();

        return EnrollmentResponse.of(enrollment);
    }

    private void validIsApplicant(User user, Enrollment enrollment) {
        if(!enrollment.isApplicant(user.getId())){
            throw new IllegalArgumentException("자기자신의 신청만 결제완료할 수 있습니다.");
        }
    }
}
