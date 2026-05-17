package com.bamdoliro.homework.enrollment.application;

import com.bamdoliro.homework.course.application.GetClassmatesByCourseUseCase;
import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.infrastructure.EnrollmentRepository;
import com.bamdoliro.homework.course.presentation.dto.response.ClassmateResponse;
import com.bamdoliro.homework.global.fixture.CourseFixture;
import com.bamdoliro.homework.global.fixture.EnrollmentFixture;
import com.bamdoliro.homework.global.fixture.UserFixture;
import com.bamdoliro.homework.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetClassmateByCourseUseCaseTest {

    @InjectMocks
    private GetClassmatesByCourseUseCase getClassmatesByCourseUseCase;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Test
    void 강의의_수강생_목록을_조회한다() {
        // given
        User user = UserFixture.createCreator();
        Course course = CourseFixture.createOpenCourse();
        Enrollment enrollment1 = EnrollmentFixture.createEnrollment();
        Enrollment enrollment2 = EnrollmentFixture.createEnrollment();
        List<Enrollment> enrollments = List.of(enrollment1, enrollment2);

        given(courseRepository.findById(course.getId()))
                .willReturn(Optional.of(course));

        given(enrollmentRepository.findByCourse(course))
                .willReturn(enrollments);

        // when
        List<ClassmateResponse> responses = getClassmatesByCourseUseCase.classmateByCourseId(course.getId(), user);

        // then
        assertEquals(2, responses.size());

        verify(courseRepository, times(1)).findById(course.getId());

        verify(enrollmentRepository, times(1)).findByCourse(course);
    }

    @Test
    void 수강생목록을_조회할_때_크리에이터가_아니면_예외가_발생한다() {
        // given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createOpenCourse();

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> getClassmatesByCourseUseCase.classmateByCourseId(course.getId(), user));

        verify(courseRepository, never()).findById(any());

        verify(enrollmentRepository, never()).findByCourse(any());
    }

    @Test
    void 강의가_존재하지_않으면_예외가_발생한다() {
        // given
        User user = UserFixture.createCreator();
        Long courseId = -1L;
        given(courseRepository.findById(courseId))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> getClassmatesByCourseUseCase.classmateByCourseId(courseId, user));

        verify(enrollmentRepository, never()).findByCourse(any());
    }
}

