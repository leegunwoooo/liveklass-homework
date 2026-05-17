package com.bamdoliro.homework.enrollment.presentation.dto.response;

import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.domain.EnrollmentStatus;

public record EnrollmentResponse(
        Long id,
        Long courseId,
        String courseTitle,
        EnrollmentStatus status,
        Integer waitlistCount
) {
    public static EnrollmentResponse of(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getCourseId(),
                enrollment.getCourseTitle(),
                enrollment.getStatus(),
                enrollment.getWaitlistCount()
        );
    }
}
