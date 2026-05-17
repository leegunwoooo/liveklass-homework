package com.bamdoliro.homework.enrollment.application;

import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.domain.EnrollmentStatus;
import com.bamdoliro.homework.enrollment.infrastructure.EnrollmentRepository;
import com.bamdoliro.homework.enrollment.presentation.dto.response.EnrollmentResponse;
import com.bamdoliro.homework.global.fixture.EnrollmentFixture;
import com.bamdoliro.homework.global.fixture.UserFixture;
import com.bamdoliro.homework.user.domain.Role;
import com.bamdoliro.homework.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CancelEnrollmentUseCaseTest {

    @InjectMocks
    private CancelEnrollmentUseCase cancelEnrollmentUseCase;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Test
    void 수강신청을_취소한다() {
        // given
        User user = UserFixture.createClassmate();
        Enrollment enrollment = EnrollmentFixture.createEnrollment();
        enrollment.getCourse().increaseCurrentCount();

        given(enrollmentRepository.findById(enrollment.getId()))
                .willReturn(Optional.of(enrollment));
        given(enrollmentRepository.findFirstByCourseAndStatusOrderByWaitlistCountAsc(any(), any()))
                .willReturn(Optional.empty());

        //when
        EnrollmentResponse response = cancelEnrollmentUseCase.cancelEnrollment(enrollment.getId(), user);

        //then
        assertEquals(EnrollmentStatus.CANCELLED, response.status());

        verify(enrollmentRepository, times(1)).findById(enrollment.getId());
        verify(enrollmentRepository, times(1)).findFirstByCourseAndStatusOrderByWaitlistCountAsc(any(), any());
    }

    @Test
    void 수강신청을_취소할_때_자신이_아니면_예외가_발생한다(){
        //given
        User otherUser = User.create(2L, Role.ROLE_CLASSMATE);
        Enrollment enrollment = EnrollmentFixture.createEnrollment();
        enrollment.getCourse().increaseCurrentCount();

        given(enrollmentRepository.findById(enrollment.getId()))
                .willReturn(Optional.of(enrollment));

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> cancelEnrollmentUseCase.cancelEnrollment(enrollment.getId(), otherUser));

        verify(enrollmentRepository, times(1)).findById(enrollment.getId());
    }

    @Test
    void 수강신청을_취소하면_대기열_첫번째_유저가_PENDING이된다() {
        // given
        User user = UserFixture.createClassmate();
        Enrollment enrollment = EnrollmentFixture.createEnrollment();
        enrollment.getCourse().increaseCurrentCount();
        Enrollment waited = EnrollmentFixture.createWaitedEnrollment();

        given(enrollmentRepository.findById(enrollment.getId()))
                .willReturn(Optional.of(enrollment));
        given(enrollmentRepository.findFirstByCourseAndStatusOrderByWaitlistCountAsc(any(), any()))
                .willReturn(Optional.of(waited));

        // when
        cancelEnrollmentUseCase.cancelEnrollment(enrollment.getId(), user);

        // then
        assertEquals(EnrollmentStatus.PENDING, waited.getStatus());
    }

}
