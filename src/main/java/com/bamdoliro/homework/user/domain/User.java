package com.bamdoliro.homework.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    public boolean isCreator() {
        return role == Role.ROLE_CREATOR;
    }

    @Builder
    private User(Long id, Role role){
        this.id = id;
        this.role = role;
    }

    public static User create(Long id, Role role){
        return User.builder()
                .id(id)
                .role(role)
                .build();
    }

}
