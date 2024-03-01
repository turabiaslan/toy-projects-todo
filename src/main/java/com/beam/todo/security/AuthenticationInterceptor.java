package com.beam.todo.security;


import com.beam.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.beam.todo.controller.UserController.SESSION_USER;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        Object user = request.getSession().getAttribute(SESSION_USER);
        if (user != null) {
            return true;
        } else {
            if (request.getRequestURI().equals("/") ||
                request.getRequestURI().startsWith("/login") ||
                request.getRequestURI().startsWith("/logout") ||
                request.getRequestURI().startsWith("/3rd") ||
                request.getRequestURI().startsWith("/app")) {
                return true;
            } else {
                response.setStatus(401);
                return false;
            }
        }
    }

}
