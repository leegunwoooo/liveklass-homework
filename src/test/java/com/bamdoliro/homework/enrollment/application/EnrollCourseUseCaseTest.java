package com.bamdoliro.homework.enrollment.application;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.infrastructure.EnrollmentRepository;
import com.bamdoliro.homework.enrollment.presentation.dto.response.EnrollmentResponse;
import com.bamdoliro.homework.global.fixture.CourseFixture;
import com.bamdoliro.homework.global.fixture.EnrollmentFixture;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollCourseUseCaseTest {

    @InjectMocks
    private EnrollCourseUseCase enrollCourseUseCase;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Test
    void 강의를_신청한다(){
        //given
        User user = User.create(2L, Role.ROLE_CLASSMATE);
        Course course = CourseFixture.createOpenCourse();
        Enrollment enrollment = EnrollmentFixture.createEnrollment();

        given(courseRepository.findByIdWithLock(course.getId()))
                .willReturn(Optional.of(course));

        given(enrollmentRepository.existsByCourseAndUser(course, user))
                .willReturn(false);

        given(enrollmentRepository.save(any(Enrollment.class)))
                .willReturn(enrollment);

        //when
        EnrollmentResponse response = enrollCourseUseCase.enroll(course.getId(), user);

        //then
        assertEquals(enrollment.getId(), response.id());
        assertEquals(enrollment.getCourseId(), response.courseId());
        assertEquals(enrollment.getCourseTitle(), response.courseTitle());
        assertEquals(enrollment.getStatus(), response.status());
        assertEquals(enrollment.getWaitlistCount(), response.waitlistCount());

        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void 존재하지_않는_강의면_예외가_발생한다() {
        // given
        User user = UserFixture.createClassmate();
        Long courseId = 1L;

        given(courseRepository.findByIdWithLock(courseId))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> enrollCourseUseCase.enroll(courseId, user));

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void 만료된_강의면_예외가_발생한다() {
        // given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createExpiredCourse();

        given(courseRepository.findByIdWithLock(course.getId()))
                .willReturn(Optional.of(course));

        // when & then
        assertThrows(IllegalStateException.class,
                () -> enrollCourseUseCase.enroll(course.getId(), user));

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void 오픈하지_않은_강의에_신청하면_예외가_발생한다() {
        // given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createDraftCourse();

        given(courseRepository.findByIdWithLock(course.getId()))
                .willReturn(Optional.of(course));

        // when & then
        assertThrows(IllegalStateException.class,
                () -> enrollCourseUseCase.enroll(course.getId(), user));

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void 이미_신청한_강의면_예외가_발생한다() {
        // given
        User user = UserFixture.createClassmate();
        Course course = CourseFixture.createOpenCourse();

        given(courseRepository.findByIdWithLock(course.getId()))
                .willReturn(Optional.of(course));

        given(enrollmentRepository.existsByCourseAndUser(course, user))
                .willReturn(true);

        // when & then
        assertThrows(IllegalStateException.class,
                () -> enrollCourseUseCase.enroll(course.getId(), user));

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void 자신의_강의에_신청하면_예외가_발생한다() {
        // given
        User user = UserFixture.createCreator();
        Course course = CourseFixture.createOpenCourse();

        given(courseRepository.findByIdWithLock(course.getId()))
                .willReturn(Optional.of(course));

        given(enrollmentRepository.existsByCourseAndUser(course, user))
                .willReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> enrollCourseUseCase.enroll(course.getId(), user));

        verify(enrollmentRepository, never()).save(any());
    }
}
