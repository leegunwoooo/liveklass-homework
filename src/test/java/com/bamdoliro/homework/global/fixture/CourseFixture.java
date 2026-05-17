package com.bamdoliro.homework.global.fixture;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.presentation.dto.request.CreateCourseRequest;
import com.bamdoliro.homework.user.domain.Role;
import com.bamdoliro.homework.user.domain.User;

import java.time.LocalDate;

public class CourseFixture {

    public static Course createDraftCourse() {
        return Course.create(
                "제목",
                "설명",
                User.create(
                        1L,
                        Role.ROLE_CREATOR
                ),
                10000,
                30,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );
    }

    public static Course createOpenCourse(){
        Course course = Course.create(
                "제목",
                "설명",
                User.create(
                        1L,
                        Role.ROLE_CREATOR
                ),
                10000,
                30,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        course.open();

        return course;
    }

    public static Course createClosedCourse(){
        Course course = Course.create(
                "제목",
                "설명",
                User.create(
                        1L,
                        Role.ROLE_CREATOR
                ),
                10000,
                30,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        course.open();

        course.close();

        return course;
    }

    public static Course createExpiredCourse() {
        Course course = Course.create(
                "만료된 강의",
                "이미 종료된 강의입니다.",
                UserFixture.createCreator(),
                10000,
                30,
                LocalDate.now().minusDays(10),
                LocalDate.now().minusDays(1)
        );

        course.open();

        return course;
    }

    public static CreateCourseRequest createCreateCourseRequest() {
        return new CreateCourseRequest(
                "제목",
                "설며",
                10000,
                10,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );
    }

}
