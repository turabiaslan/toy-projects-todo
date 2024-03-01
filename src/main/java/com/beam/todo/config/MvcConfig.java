package com.beam.todo.config;


import com.beam.todo.repository.UserRepository;
import com.beam.todo.security.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(userRepository))
                .addPathPatterns("/today", "/profile", "/month", "/planned", "/starred", "/completed");

    }
}
