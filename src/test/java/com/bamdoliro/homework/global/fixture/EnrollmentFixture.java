package com.bamdoliro.homework.global.fixture;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.user.domain.User;

import java.time.LocalDate;

public class EnrollmentFixture {
    public static Enrollment createEnrollment(){
        User user = UserFixture.createClassmate();
        return Enrollment.create(
                Course.create(
                        "제목",
                        "설명",
                        user,
                        10000,
                        30,
                        LocalDate.now(),
                        LocalDate.now().plusDays(7)
                ),
                user
        );
    }

    public static Enrollment createWaitedEnrollment(){
        User user = UserFixture.createClassmate();
        return Enrollment.createWaitedList(
                Course.create(
                        "제목",
                        "설명",
                        user,
                        10000,
                        30,
                        LocalDate.now(),
                        LocalDate.now().plusDays(7)
                ),
                user,
                1
        );
    }
}
