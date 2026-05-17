package com.bamdoliro.homework.course.application;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.domain.CourseStatus;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.course.presentation.dto.response.CourseResponse;
import com.bamdoliro.homework.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChangedCourseStatusUseCase {

    private final CourseRepository courseRepository;

    @Transactional
    public CourseResponse changeCourseStatus(Long courseId, CourseStatus status, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("업승ㅁ"));

        validIsCreator(user);
        validIsOwner(user, course);

        switch (status){
            case OPEN -> course.open();
            case CLOSED -> course.close();
            default -> throw new IllegalArgumentException("변경할 수 없는 상태입니다.");
        }

        return CourseResponse.from(course);
    }

    private void validIsCreator(User user){
        if(!user.isCreator()){
            throw new IllegalArgumentException("크리에이터만 상태를 변경할 수 있습니다.");
        }
    }

    private void validIsOwner(User user, Course course) {
        if(!course.isOwner(user.getId())){
            throw new IllegalArgumentException("본인만 상태를 변경할 수 있습니다.");
        }
    }

}
