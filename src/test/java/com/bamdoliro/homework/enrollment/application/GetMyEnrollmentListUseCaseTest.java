package com.bamdoliro.homework.enrollment.application;

import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.infrastructure.EnrollmentRepository;
import com.bamdoliro.homework.enrollment.presentation.dto.response.EnrollmentResponse;
import com.bamdoliro.homework.global.fixture.EnrollmentFixture;
import com.bamdoliro.homework.global.fixture.UserFixture;
import com.bamdoliro.homework.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GetMyEnrollmentListUseCaseTest {

    @InjectMocks
    private GetMyEnrollmentListUseCase getMyEnrollmentListUseCase;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Test
    void 자신의_수강신청_목록을_조회한다() {
        // given
        User user = UserFixture.createClassmate();
        Pageable pageable = PageRequest.of(0, 10);
        Enrollment enrollment1 = EnrollmentFixture.createEnrollment();
        Enrollment enrollment2 = EnrollmentFixture.createEnrollment();
        Page<Enrollment> enrollments = new PageImpl<>(List.of(enrollment1, enrollment2));

        given(enrollmentRepository.findByUser(user, pageable))
                .willReturn(enrollments);

        // when
        Page<EnrollmentResponse> responses = getMyEnrollmentListUseCase.getMyEnrollmentList(user, pageable);

        // then
        assertEquals(2, responses.getContent().size());

        assertEquals(enrollment1.getCourseTitle(), responses.getContent().get(0).courseTitle());

        verify(enrollmentRepository, times(1)).findByUser(user, pageable);
    }

    @Test
    void 수강신청_목록이_없으면_빈_페이지를_반환한다() {
        // given
        User user = UserFixture.createClassmate();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Enrollment> emptyPage = Page.empty(pageable);

        given(enrollmentRepository.findByUser(user, pageable))
                .willReturn(emptyPage);

        // when
        Page<EnrollmentResponse> responses = getMyEnrollmentListUseCase.getMyEnrollmentList(user, pageable);

        // then
        assertTrue(responses.isEmpty());

        verify(enrollmentRepository, times(1)).findByUser(user, pageable);
    }
}
