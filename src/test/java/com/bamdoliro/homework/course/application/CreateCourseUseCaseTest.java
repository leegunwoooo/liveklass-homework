package com.bamdoliro.homework.course.application;

import com.bamdoliro.homework.course.domain.Course;
import com.bamdoliro.homework.course.infrastructure.CourseRepository;
import com.bamdoliro.homework.course.presentation.dto.request.CreateCourseRequest;
import com.bamdoliro.homework.course.presentation.dto.response.CourseResponse;
import com.bamdoliro.homework.global.fixture.CourseFixture;
import com.bamdoliro.homework.global.fixture.UserFixture;
import com.bamdoliro.homework.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCourseUseCaseTest {

    @InjectMocks
    private CreateCourseUseCase createCourseUseCase;

    @Mock
    private CourseRepository courseRepository;

    @Test
    void 강의를_만든다() {
        // given
        User user = UserFixture.createCreator();
        CreateCourseRequest request = CourseFixture.createCreateCourseRequest();
        Course course = CourseFixture.createDraftCourse();

        given(courseRepository.save(any(Course.class)))
                .willReturn(course);

        // when
        CourseResponse response = createCourseUseCase.createCourse(request, user);

        // then
        assertEquals(course.getTitle(), response.title());
        assertEquals(course.getDescription(), response.description());
        assertEquals(course.getPrice(), response.price());
        assertEquals(course.getCapacity(), response.capacity());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void 강의를_생성할_때_크리에이터가_아니면_예외가_발생한다() {
        // given
        User user = UserFixture.createClassmate();
        CreateCourseRequest request = CourseFixture.createCreateCourseRequest();

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> createCourseUseCase.createCourse(request, user));

        verify(courseRepository, never()).save(any());
    }

}
