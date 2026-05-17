package com.bamdoliro.homework.course.application;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.domain.CourseStatus;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.course.presentation.dto.response.SimpleCourseResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetCourseListUseCase {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<SimpleCourseResponse> getCourseListByStatus(CourseStatus status) {
        List<Course> courseList =
                status != null ? courseRepository.findByCourseStatus(status) : courseRepository.findAll();

        return courseList.stream()
                .map(SimpleCourseResponse::of)
                .toList();
    }

}
