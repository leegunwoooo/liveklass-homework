package com.bamdoliro.homework.course.application;

import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.course.presentation.dto.response.CourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetCourseUseCase {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public CourseResponse getCourse(Long id){
        return CourseResponse.from(
                courseRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾지 못했습니다."))
        );
    }

}
