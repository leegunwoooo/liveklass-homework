package com.bamdoliro.homework.course.presentation;

import com.bamdoliro.homework.course.application.ChangedCourseStatusUseCase;
import com.bamdoliro.homework.course.application.CreateCourseUseCase;
import com.bamdoliro.homework.course.application.GetCourseListUseCase;
import com.bamdoliro.homework.course.application.GetCourseUseCase;
import com.bamdoliro.homework.course.domain.CourseStatus;
import com.bamdoliro.homework.course.presentation.dto.request.CreateCourseRequest;
import com.bamdoliro.homework.course.presentation.dto.response.CourseResponse;
import com.bamdoliro.homework.course.presentation.dto.response.SimpleCourseResponse;
import com.bamdoliro.homework.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CreateCourseUseCase createCourseUseCase;
    private final ChangedCourseStatusUseCase changedCourseStatusUseCase;
    private final GetCourseUseCase getCourseUseCase;
    private final GetCourseListUseCase getCourseListUseCase;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(
            @RequestBody CreateCourseRequest request, User user
    )
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createCourseUseCase.createCourse(request, user));
    }

    @PatchMapping("/{courseId}/status")
    public ResponseEntity<CourseResponse> changedCourseStatus(
            @PathVariable Long courseId,
            @RequestParam CourseStatus status,
            User user
    )
    {
        return ResponseEntity.ok(changedCourseStatusUseCase.changeCourseStatus(courseId, status, user));
    }

    @GetMapping
    public ResponseEntity<List<SimpleCourseResponse>> getCoursesListByStatus(
            @RequestParam(required = false) CourseStatus status
    )
    {
        return ResponseEntity.ok(getCourseListUseCase.getCourseListByStatus(status));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponse> getCourseById(
            @PathVariable Long courseId
    )
    {
        return ResponseEntity.ok(getCourseUseCase.getCourse(courseId));
    }
}
