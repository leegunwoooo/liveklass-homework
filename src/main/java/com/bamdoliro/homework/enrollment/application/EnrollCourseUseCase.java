package com.bamdoliro.homework.enrollment.application;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.domain.EnrollmentStatus;
import com.bamdoliro.homework.enrollment.infrastructure.EnrollmentRepository;
import com.bamdoliro.homework.enrollment.presentation.dto.response.EnrollmentResponse;
import com.bamdoliro.homework.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EnrollCourseUseCase {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public EnrollmentResponse enroll(Long courseId, User user){
        Course course = courseRepository.findByIdWithLock(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));

        validExpiredTime(course);
        validIsOpen(course);
        validDuplicateEnroll(course, user);
        validIsOwner(course, user);

        if(course.isMaxCapacity()){
            int waitlistCount = enrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.WAITED);
            return EnrollmentResponse.of(
                    enrollmentRepository.save(
                            Enrollment.createWaitedList(course, user, waitlistCount + 1)
                    )
            );
        }

        course.increaseCurrentCount();

        return EnrollmentResponse.of(
                enrollmentRepository.save(
                        Enrollment.create(course, user)
                )
        );
    }

    private void validExpiredTime(Course course){
        if(course.isExpired()){
            throw new IllegalStateException("수강일자이 지난 강의입니다.");
        }
    }

    private void validIsOpen(Course course){
        if(!course.isOpen()){
            throw new IllegalStateException("아직 오픈되지 않은 강의입니다.");
        }
    }

    private void validDuplicateEnroll(Course course, User user){
        if(enrollmentRepository.existsByCourseAndUser(course, user)){
            throw new IllegalStateException("이미 신청한 강의입니다.");
        }
    }

    private void validIsOwner(Course course, User user){
        if(course.isOwner(user.getId())){
            throw new IllegalArgumentException("자기자신의 강의에는 수강신청 할 수 없습니다.");
        }
    }

}

