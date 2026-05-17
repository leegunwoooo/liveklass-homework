package com.bamdoliro.homework.enrollment.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EnrollmentStatus {
    PENDING("신청완료, 결제대기"),
    CONFIRMED("결제완료, 수강확정"),
    CANCELLED("취소됨"),
    WAITED("대기중");

    private final String description;
}
