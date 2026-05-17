package com.bamdoliro.homework.course.presentation.dto.response;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.domain.CourseStatus;

public record SimpleCourseResponse(
        Long id,
        String title,
        Integer price,
        Integer capacity,
        Integer currentApplicantCount,
        CourseStatus courseStatus
) {
    public static SimpleCourseResponse of(Course course) {
        return new SimpleCourseResponse(
                course.getId(),
                course.getTitle(),
                course.getPrice(),
                course.getCapacity(),
                course.getCurrentApplicantCount(),
                course.getCourseStatus()
        );
    }
}
