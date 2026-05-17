package com.bamdoliro.homework.course.application;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.domain.CourseStatus;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.course.presentation.dto.response.SimpleCourseResponse;
import com.bamdoliro.homework.global.fixture.CourseFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GetCourseListUseCaseTest {

    @InjectMocks
    private GetCourseListUseCase getCourseListUseCase;

    @Mock
    private CourseRepository courseRepository;

    @Test
    void 모든_강의를_조회한다(){
        //given
        List<Course> courseList = List.of(
                CourseFixture.createDraftCourse(),
                CourseFixture.createDraftCourse(),
                CourseFixture.createDraftCourse()
        );

        given(courseRepository.findAll())
                .willReturn(courseList);

        //when
        List<SimpleCourseResponse> response = getCourseListUseCase.getCourseListByStatus(null);

        //then
        assertEquals(courseList.size(), response.size());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void 특정_상태의_강의들을_조회한다(){
        //given
        List<Course> courseList = List.of(
                CourseFixture.createClosedCourse(),
                CourseFixture.createOpenCourse(),
                CourseFixture.createDraftCourse()
        );

        given(courseRepository.findByCourseStatus(CourseStatus.OPEN))
                .willReturn(List.of(courseList.get(1)));

        //when
        List<SimpleCourseResponse> response = getCourseListUseCase.getCourseListByStatus(CourseStatus.OPEN);

        //then
        assertEquals(1, response.size());

        verify(courseRepository, times(1)).findByCourseStatus(CourseStatus.OPEN);
    }
}
