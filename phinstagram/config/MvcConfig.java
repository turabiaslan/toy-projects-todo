package com.beam.sample.phinstagram.config;

import com.beam.sample.phinstagram.repository.AccountRepository;
import com.beam.sample.phinstagram.security.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final AccountRepository accountRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new AuthenticationInterceptor(accountRepository));
    }
}
