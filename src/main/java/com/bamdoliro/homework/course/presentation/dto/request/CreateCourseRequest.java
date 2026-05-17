package com.bamdoliro.homework.course.presentation.dto.request;

import java.time.LocalDate;

public record CreateCourseRequest(
        String title,
        String description,
        Integer price,
        Integer capacity,
        LocalDate startDate,
        LocalDate endDate
) {
}
