package com.beam.sample.phinstagram.security;


import com.beam.sample.phinstagram.repository.AccountRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.beam.sample.phinstagram.controller.AccountController.SESSION_ACCOUNT;


@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AccountRepository accountRepository;



}
