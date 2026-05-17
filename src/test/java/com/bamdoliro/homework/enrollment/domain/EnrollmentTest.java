package com.bamdoliro.homework.enrollment.domain;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.global.fixture.CourseFixture;
import com.bamdoliro.homework.global.fixture.UserFixture;
import com.bamdoliro.homework.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnrollmentTest {

    @Test
    void 수강신청을_생성하면_상태는_PENDING이다() {
        //given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createDraftCourse();

        //when
        Enrollment enrollment = Enrollment.create(course, user);

        //then
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.PENDING);
    }

    @Test
    void 대기열_수강신청을_생성하면_상태는_WAITED이다() {
        //given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createDraftCourse();

        //when
        Enrollment enrollment = Enrollment.createWaitedList(course, user, 1);

        //then
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.WAITED);
        assertThat(enrollment.getWaitlistCount()).isEqualTo(1);
    }

    @Test
    void PENDING_상태에서_결제를_하면_CONFIRMED가_된다() {
        //given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createDraftCourse();
        Enrollment enrollment = Enrollment.create(course, user);

        //when
        enrollment.confirm();

        //then
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CONFIRMED);
    }

    @Test
    void PENDING_상태가_아닐_때_확정하면_예외가_발생한다() {
        //given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createDraftCourse();
        Enrollment enrollment = Enrollment.create(course, user);
        enrollment.confirm();

        //when & then
        assertThrows(IllegalStateException.class, enrollment::confirm);
    }

    @Test
    void 수강신청을_취소하면_CANCELLED가_된다() {
        //given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createDraftCourse();
        course.increaseCurrentCount();
        Enrollment enrollment = Enrollment.create(course, user);

        //when
        enrollment.cancel();

        //then
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
    }

    @Test
    void 이미_취소된_수강신청을_취소하면_예외가_발생한다() {
        //given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createDraftCourse();
        course.increaseCurrentCount();
        Enrollment enrollment = Enrollment.create(course, user);
        enrollment.cancel();

        //when & then
        assertThrows(IllegalStateException.class, enrollment::cancel);
    }

    @Test
    void 신청자이면_True를_반환한다() {
        //given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createDraftCourse();
        Enrollment enrollment = Enrollment.create(course, user);

        //when & then
        assertThat(enrollment.isApplicant(user.getId())).isTrue();
    }

    @Test
    void 신청자가_아니면_False를_반환한다() {
        //given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createDraftCourse();
        Enrollment enrollment = Enrollment.create(course, user);

        //when & then
        assertThat(enrollment.isApplicant(999L)).isFalse();
    }

    @Test
    void 대기열에서_승격하면_PENDING이_된다() {
        //given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createDraftCourse();
        Enrollment enrollment = Enrollment.createWaitedList(course, user, 1);

        //when
        enrollment.promote();

        //then
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.PENDING);
    }
}
