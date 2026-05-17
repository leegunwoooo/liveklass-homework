package com.bamdoliro.homework.course.presentation.dto.response;

import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.domain.EnrollmentStatus;

public record ClassmateResponse(
        Long userId,
        EnrollmentStatus status
) {
    public static ClassmateResponse of(Enrollment enrollment) {
        return new ClassmateResponse(
                enrollment.getUser().getId(),
                enrollment.getStatus()
        );
    }
}
