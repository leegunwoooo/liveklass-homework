package com.bamdoliro.homework.course.application;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.infrastructure.EnrollmentRepository;
import com.bamdoliro.homework.course.presentation.dto.response.ClassmateResponse;
import com.bamdoliro.homework.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetClassmatesByCourseUseCase {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<ClassmateResponse> classmateByCourseId(Long courseId, User user) {
        validIsCreator(user);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾지 못함"));

        List<Enrollment> enrollments = enrollmentRepository.findByCourse(course);

        return enrollments.stream()
                .map(ClassmateResponse::of)
                .toList();
    }

    private void validIsCreator(User user){
        if(!user.isCreator()){
            throw new IllegalArgumentException("크리에이터만 조회할 수 있습니다.");
        }
    }

}
