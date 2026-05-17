package com.bamdoliro.homework.course.application;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.course.presentation.dto.response.CourseResponse;
import com.bamdoliro.homework.global.fixture.CourseFixture;
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
public class GetCourseUseCaseTest {

    @InjectMocks
    private GetCourseUseCase getCourseUseCase;

    @Mock
    private CourseRepository courseRepository;

    @Test
    void 강의를_조회한다(){
        //given
        Course course = CourseFixture.createDraftCourse();

        given(courseRepository.findById(course.getId()))
                .willReturn(Optional.of(course));

        //when
        CourseResponse response = getCourseUseCase.getCourse(course.getId());

        //then
        assertEquals(course.getTitle(), response.title());

        verify(courseRepository, times(1)).findById(course.getId());
    }

    @Test
    void 존재하지_않는_강의를_조회하면_예외가_발생한다(){
        //given
        Long courseId = -1L;

        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> getCourseUseCase.getCourse(courseId));

        verify(courseRepository, times(1)).findById(courseId);
    }
}
