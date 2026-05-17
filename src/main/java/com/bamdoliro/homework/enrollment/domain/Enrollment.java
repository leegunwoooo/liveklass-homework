package com.bamdoliro.homework.enrollment.domain;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer waitlistCount;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    private LocalDateTime confirmedTime;

    @Builder
    private Enrollment(Course course, User user, Integer waitlistCount, EnrollmentStatus status) {
        this.course = course;
        this.user = user;
        this.waitlistCount = waitlistCount;
        this.status = status;
    }

    public static Enrollment create(Course course, User user) {
        return Enrollment.builder()
                .course(course)
                .user(user)
                .status(EnrollmentStatus.PENDING)
                .build();
    }

    public static Enrollment createWaitedList(Course course, User user, Integer waitlistCount) {
        return Enrollment.builder()
                .course(course)
                .user(user)
                .status(EnrollmentStatus.WAITED)
                .waitlistCount(waitlistCount)
                .build();
    }

    public void confirm() {
        if (status != EnrollmentStatus.PENDING){
            throw new IllegalStateException("대기 상태에서만 확정 가능합니다");
        }
        this.status = EnrollmentStatus.CONFIRMED;
        this.confirmedTime = LocalDateTime.now();
    }

    public void cancel() {
        if (status == EnrollmentStatus.CANCELLED){
            throw new IllegalStateException("이미 취소된 수강신청입니다");
        }else if (status == EnrollmentStatus.CONFIRMED){
            if(isExpiredFromConfirmedTime()){
                throw new IllegalStateException("취소는 결제 후 7일까지만 가능합니다.");
            }
        }
        this.status = EnrollmentStatus.CANCELLED;
        course.decreaseCurrentCount();
    }

    public boolean isApplicant(Long userId) {
        return this.user.getId().equals(userId);
    }

    public Long getCourseId() {
        return course.getId();
    }

    public String getCourseTitle() {
        return course.getTitle();
    }

    public boolean isExpiredFromConfirmedTime(){
        return LocalDateTime.now().isAfter(confirmedTime.plusDays(7L));
    }

    public void promote() {
        this.status = EnrollmentStatus.PENDING;
        course.increaseCurrentCount();
        this.waitlistCount = null;
    }

}
