package com.bamdoliro.homework.course.domain;

import com.bamdoliro.homework.global.fixture.UserFixture;
import com.bamdoliro.homework.user.domain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourseTest {

    @Test
    void 강의를_생성하면_상태는_DRAFT다(){
        // given
        User user = UserFixture.createCreator();

        // when
        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                30,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        // then
        assertThat(course.getCourseStatus()).isEqualTo(CourseStatus.DRAFT);
    }

    @Test
    void 강의를_생성할_때_가격이_음수이면_예외가_발생한다(){
        // given
        User user = UserFixture.createCreator();

        // when & then
        assertThatThrownBy(() -> Course.create(
                "제목",
                "설명",
                user,
                -1,
                30,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 강의를_생성할_때_최대인원이_음수면_예외가_발생한다(){
        // given
        User user = UserFixture.createCreator();

        // when & then
        assertThatThrownBy(() -> Course.create(
                "제목",
                "설명",
                user,
                10000,
                -30,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 강의를_생성할_때_종료일자가_시작일자보다_이른_날짜이면_예외가_발생한다(){
        // given
        User user = UserFixture.createCreator();

        // when & then
        assertThatThrownBy(() -> Course.create(
                "제목",
                "설명",
                user,
                10000,
                10,
                LocalDate.now(),
                LocalDate.now().minusDays(7)
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 수강일자가_지나면_만료된다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(1)
        );

        // when & then
        assertThat(course.isExpired()).isTrue();
    }

    @Test
    void 수강일자가_지나지않으면_만료되지않는다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when & then
        assertThat(course.isExpired()).isFalse();
    }

    @Test
    void 정원이_가득차면_True를_반환한다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when
        course.increaseCurrentCount();

        // then
        assertThat(course.isMaxCapacity()).isTrue();
    }

    @Test
    void 정원이_가득차지않으면_False를_반환한다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                2,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when
        course.increaseCurrentCount();

        // then
        assertThat(course.isMaxCapacity()).isFalse();
    }

    @Test
    void 신청자수가_증가한다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when
        course.increaseCurrentCount();

        // then
        assertThat(course.getCurrentApplicantCount()).isEqualTo(1);
    }

    @Test
    void 신청자수가_감소한다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        course.increaseCurrentCount();

        // when
        course.decreaseCurrentCount();

        // then
        assertThat(course.getCurrentApplicantCount()).isEqualTo(0);
    }

    @Test
    void 신청자수가_음수로_감소하려고하면_예외를_반환한다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when & then
        assertThrows(IllegalArgumentException.class,
                course::decreaseCurrentCount);
    }

    @Test
    void 강의의_상태가_DRAFT에서_OPEN으로_변경된다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when
        course.open();

        // then
        assertThat(course.getCourseStatus()).isEqualTo(CourseStatus.OPEN);
    }

    @Test
    void 강의의_상태가_DRAFT가_아닐_떄_OPEN으로_변경을_시도하면_예외가_발생한다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        course.open();

        // when & then
        assertThrows(IllegalArgumentException.class,
                course::open);
    }

    @Test
    void 강의의_상태가_OPEN에서_CLOSE으로_변경된다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        course.open();

        // when
        course.close();

        // then
        assertThat(course.getCourseStatus()).isEqualTo(CourseStatus.CLOSED);
    }

    @Test
    void 강의의_상태가_OPEN이_아닐_떄_CLOSE으로_변경을_시도하면_예외가_발생한다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when & then
        assertThrows(IllegalArgumentException.class,
                course::close);
    }

    @Test
    void 강의_제작자일_경우_True를_반환한다() {
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when & then
        assertThat(course.isOwner(user.getId())).isTrue();
    }

    @Test
    void 강의_제작자가_아닐_경우_False를_반환한다() {
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when & then
        assertThat(course.isOwner(999L)).isFalse();
    }

    @Test
    void 강의의_상태가_OPEN이면_True를_반환한다(){
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        course.open();

        // when & then
        assertThat(course.isOpen()).isTrue();
    }

    @Test
    void 강의의_상태가_OPEN이_아니라면면_False를_반환한다() {
        // given
        User user = UserFixture.createCreator();

        Course course = Course.create(
                "제목",
                "설명",
                user,
                10000,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when & then
        assertThat(course.isOpen()).isFalse();
    }
}