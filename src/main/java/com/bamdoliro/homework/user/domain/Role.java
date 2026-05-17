package com.bamdoliro.homework.user.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ROLE_CREATOR("크리에이터"),
    ROLE_CLASSMATE("수강자");

    private final String description;
}
