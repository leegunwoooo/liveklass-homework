package com.bamdoliro.homework.global.fixture;

import com.bamdoliro.homework.user.domain.Role;
import com.bamdoliro.homework.user.domain.User;

public class UserFixture {

    public static User createClassmate(){
        return User.create(
                1L,
                Role.ROLE_CLASSMATE
        );
    }

    public static User createCreator(){
        return User.create(
                1L,
                Role.ROLE_CREATOR
        );
    }

}
