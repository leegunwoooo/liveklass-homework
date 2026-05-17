package com.bamdoliro.homework.course.domain;

import com.bamdoliro.homework.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer price;

    private Integer capacity;

    private Integer currentApplicantCount = 0;

    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder
    private Course(String title, String description, User user, Integer price, Integer capacity, LocalDate startDate, LocalDate endDate) {
        validCapacity(capacity);
        validPrice(price);
        validEndDate(startDate, endDate);
        this.title = title;
        this.description = description;
        this.user = user;
        this.price = price;
        this.capacity = capacity;
        this.courseStatus = CourseStatus.DRAFT;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Course create(String title, String description, User user, Integer price, Integer capacity, LocalDate startDate, LocalDate endDate) {
        return Course.builder()
                .title(title)
                .description(description)
                .user(user)
                .price(price)
                .capacity(capacity)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    private void validPrice(Integer price){
        if (price == null || price < 0) {
            throw new IllegalArgumentException("가격은 0원 미만일 수 없습니다.");
        }
    }

    private void validCapacity(Integer capacity){
        if (capacity == null || capacity < 0){
            throw new IllegalArgumentException("정원은 0명 이하일 수 없습니다.");
        }
    }

    private void validEndDate(LocalDate startDate, LocalDate endDate){
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("종료일자는 시작일자보다 먼저이면 안됩니다.");
        }
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }

    public boolean isMaxCapacity() {
        return currentApplicantCount >= capacity;
    }

    public void increaseCurrentCount() {
        currentApplicantCount++;
    }

    public void decreaseCurrentCount() {
        if (currentApplicantCount <= 0){
            throw new IllegalArgumentException("마이너스로 감소 불가");
        }
        currentApplicantCount--;
    }

    public void open(){
        if(courseStatus != CourseStatus.DRAFT) {
            throw new IllegalArgumentException("초안 상태에서만 강의의 상태를 모집 중으로 변경 할 수 있습니다.");
        }
        this.courseStatus = CourseStatus.OPEN;
    }

    public void close(){
        if(courseStatus != CourseStatus.OPEN) {
            throw new IllegalArgumentException("모집 중 상태에서만 강의의 상태를 모집 마감으로 변경할 수 있습니다.");
        }
        this.courseStatus = CourseStatus.CLOSED;
    }

    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }

    public boolean isOpen(){
        return courseStatus == CourseStatus.OPEN;
    }

}
