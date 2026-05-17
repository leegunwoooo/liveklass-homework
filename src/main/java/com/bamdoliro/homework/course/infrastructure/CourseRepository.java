package com.bamdoliro.homework.course.infrastructure;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.domain.CourseStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCourseStatus(CourseStatus courseStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select course from Course course where course.id = :id")
    Optional<Course> findByIdWithLock(@Param("id") Long id);

}
