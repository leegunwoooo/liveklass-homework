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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConfirmEnrollmentUseCaseTest {

    @InjectMocks
    private ConfirmEnrollmentUseCase confirmEnrollmentUseCase;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Test
    void 수강신청을_확정짓는다(){
        //given
        Enrollment enrollment = EnrollmentFixture.createEnrollment();
        User user = UserFixture.createClassmate();

        given(enrollmentRepository.findById(enrollment.getId()))
                .willReturn(Optional.of(enrollment));

        //when
        EnrollmentResponse response = confirmEnrollmentUseCase.confirm(enrollment.getId(), user);

        //then
        assertEquals(EnrollmentStatus.CONFIRMED, response.status());

        verify(enrollmentRepository, times(1)).findById(enrollment.getId());
    }

    @Test
    void 수강신청을_확정지을_때_자신이_아니면_예외가_발생한다(){
        //given
        Enrollment enrollment = EnrollmentFixture.createEnrollment();
        User othrerUser = User.create(2L, Role.ROLE_CREATOR);

        given(enrollmentRepository.findById(enrollment.getId()))
                .willReturn(Optional.of(enrollment));

        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> confirmEnrollmentUseCase.confirm(enrollment.getId(), othrerUser));

        verify(enrollmentRepository, times(1)).findById(enrollment.getId());
    }
}
