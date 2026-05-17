package com.bamdoliro.homework.global;

import com.bamdoliro.homework.user.domain.User;
import com.bamdoliro.homework.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String userId = webRequest.getHeader("X-User-Id");
        if (userId == null) throw new IllegalArgumentException("유저 아이디가 없습니다");
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다"));
    }
}
