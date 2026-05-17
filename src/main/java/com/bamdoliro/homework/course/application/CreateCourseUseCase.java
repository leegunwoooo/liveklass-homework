package com.bamdoliro.homework.course.application;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.course.presentation.dto.request.CreateCourseRequest;
import com.bamdoliro.homework.course.presentation.dto.response.CourseResponse;
import com.bamdoliro.homework.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateCourseUseCase {

    private final CourseRepository courseRepository;

    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request, User user) {
        validUserIsCreator(user);

        return CourseResponse.from(
                courseRepository.save(
                        Course.create(
                                request.title(),
                                request.description(),
                                user,
                                request.price(),
                                request.capacity(),
                                request.startDate(),
                                request.endDate()
                        )
                )
        );
    }

    private void validUserIsCreator(User user){
        if(!user.isCreator()){
            throw new IllegalArgumentException("크리에이터만 강의를 생성 할 수 있습니다.");
        }
    }
}
