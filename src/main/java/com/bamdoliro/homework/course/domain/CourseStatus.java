package com.bamdoliro.homework.course.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CourseStatus {
    DRAFT("초안"),
    OPEN("모집중"),
    CLOSED("모집마감");

    private final String description;
}
