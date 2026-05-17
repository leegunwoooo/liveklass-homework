package com.bamdoliro.homework.enrollment.application;

import com.bamdoliro.homework.enrollment.infrastructure.EnrollmentRepository;
import com.bamdoliro.homework.enrollment.presentation.dto.response.EnrollmentResponse;
import com.bamdoliro.homework.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetMyEnrollmentListUseCase {

    private final EnrollmentRepository enrollmentRepository;

    @Transactional(readOnly = true)
    public Page<EnrollmentResponse> getMyEnrollmentList(User user, Pageable pageable) {
        return enrollmentRepository.findByUser(user, pageable)
                .map(EnrollmentResponse::of);
    }

}
