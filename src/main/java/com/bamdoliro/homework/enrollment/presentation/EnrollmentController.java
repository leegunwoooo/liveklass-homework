package com.bamdoliro.homework.enrollment.presentation;

import com.bamdoliro.homework.enrollment.application.CancelEnrollmentUseCase;
import com.bamdoliro.homework.enrollment.application.ConfirmEnrollmentUseCase;
import com.bamdoliro.homework.enrollment.application.EnrollCourseUseCase;
import com.bamdoliro.homework.enrollment.application.GetMyEnrollmentListUseCase;
import com.bamdoliro.homework.enrollment.presentation.dto.response.EnrollmentResponse;
import com.bamdoliro.homework.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollCourseUseCase enrollCourseUseCase;
    private final ConfirmEnrollmentUseCase confirmEnrollmentUseCase;
    private final CancelEnrollmentUseCase cancelEnrollmentUseCase;
    private final GetMyEnrollmentListUseCase getMyEnrollmentListUseCase;

    @PostMapping("/{courseId}")
    public ResponseEntity<EnrollmentResponse> enroll(
            @PathVariable Long courseId,
            User user
    )
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollCourseUseCase.enroll(courseId, user));
    }

    @PatchMapping("/{enrollId}/confirm")
    public ResponseEntity<EnrollmentResponse> confirm(
            @PathVariable Long enrollId,
            User user
    )
    {
        return ResponseEntity
                .ok(confirmEnrollmentUseCase.confirm(enrollId, user));
    }

    @PatchMapping("/{enrollId}/cancel")
    public ResponseEntity<EnrollmentResponse> cancel(
            @PathVariable Long enrollId,
            User user
    )
    {
        return ResponseEntity
                .ok(cancelEnrollmentUseCase.cancelEnrollment(enrollId, user));
    }

    @GetMapping
    public ResponseEntity<Page<EnrollmentResponse>> getAllEnrollments(
            User user,
            @PageableDefault(size = 10) Pageable pageable
    )
    {
        return ResponseEntity
                .ok(getMyEnrollmentListUseCase.getMyEnrollmentList(user, pageable));
    }

}
