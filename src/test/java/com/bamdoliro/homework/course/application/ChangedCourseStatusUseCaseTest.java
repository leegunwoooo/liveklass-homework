package com.bamdoliro.homework.course.application;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.domain.CourseStatus;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.course.presentation.dto.response.CourseResponse;
import com.bamdoliro.homework.global.fixture.CourseFixture;
import com.bamdoliro.homework.global.fixture.UserFixture;
import com.bamdoliro.homework.user.domain.Role;
import com.bamdoliro.homework.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChangedCourseStatusUseCaseTest {

    @InjectMocks
    private ChangedCourseStatusUseCase changedCourseStatusUseCase;

    @Mock
    private CourseRepository courseRepository;

    @Test
    void 강의의_상태를_정상적으로_변경한다(){
        //given
        Course course = CourseFixture.createDraftCourse();
        User user = UserFixture.createCreator();

        given(courseRepository.findById(course.getId()))
                .willReturn(Optional.of(course));

        //when
        CourseResponse response = changedCourseStatusUseCase
                .changeCourseStatus(course.getId(), CourseStatus.OPEN, user);

        //then
        assertEquals(CourseStatus.OPEN, response.courseStatus());

        verify(courseRepository, times(1)).findById(course.getId());
    }

    @Test
    void 강의의_상태를_변경하는_유저가_크리에이터가_아니면_예외가_발생한다(){
        //given
        Course course = CourseFixture.createDraftCourse();
        User user = UserFixture.createClassmate();

        given(courseRepository.findById(course.getId()))
                .willReturn(Optional.of(course));

        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> changedCourseStatusUseCase.changeCourseStatus(course.getId(), CourseStatus.OPEN, user));

        verify(courseRepository, times(1)).findById(course.getId());
    }

    @Test
    void 강의의_상태를_변경하는_유저가_자신이_아니면_예외가_발생한다(){
        //given
        Course course = CourseFixture.createDraftCourse();
        User othrerUser = User.create(2L, Role.ROLE_CREATOR);

        given(courseRepository.findById(course.getId()))
                .willReturn(Optional.of(course));

        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> changedCourseStatusUseCase.changeCourseStatus(course.getId(), CourseStatus.OPEN, othrerUser));

        verify(courseRepository, times(1)).findById(course.getId());
    }

    @Test
    void 변경할_수_없는_상태값이_들어오면_예외가_발생한다(){
        //given
        Course course = CourseFixture.createDraftCourse();
        User user = UserFixture.createCreator();

        given(courseRepository.findById(course.getId()))
                .willReturn(Optional.of(course));

        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> changedCourseStatusUseCase.changeCourseStatus(course.getId(), CourseStatus.DRAFT, user));

        verify(courseRepository, times(1)).findById(course.getId());
    }

}
