package com.bamdoliro.homework.course.presentation.dto.response;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.domain.CourseStatus;

import java.time.LocalDate;

public record CourseResponse(
        Long id,
        String title,
        String description,
        Long userId,
        Integer price,
        Integer capacity,
        Integer currentApplicantCount,
        CourseStatus courseStatus,
        LocalDate startDate,
        LocalDate endDate
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getUser().getId(),
                course.getPrice(),
                course.getCapacity(),
                course.getCurrentApplicantCount(),
                course.getCourseStatus(),
                course.getStartDate(),
                course.getEndDate()
        );
    }
}
