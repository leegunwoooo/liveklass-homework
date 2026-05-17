package com.bamdoliro.homework.enrollment.infrastructure;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.enrollment.domain.Enrollment;
import com.bamdoliro.homework.enrollment.domain.EnrollmentStatus;
import com.bamdoliro.homework.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByCourseAndUser(Course course, User user);
    Page<Enrollment> findByUser(User user, Pageable pageable);
    int countByCourseAndStatus(Course course, EnrollmentStatus status);
    Optional<Enrollment> findFirstByCourseAndStatusOrderByWaitlistCountAsc(Course course, EnrollmentStatus status);
    List<Enrollment> findByCourse(Course course);
}
