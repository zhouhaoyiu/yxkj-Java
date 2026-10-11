package com.zhy.yxkj.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final Sessions sessions;
    public AuthConfig(Sessions sessions) { this.sessions = sessions; }
    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                if (request.getMethod().equals("OPTIONS")) return true;
                var user = sessions.find(request.getHeader("Authorization"));
                if (user == null) { response.sendError(401); return false; }
                if ((request.getRequestURI().contains("/admin") || request.getRequestURI().endsWith("/verifyWorkJob") || request.getRequestURI().endsWith("/getVerifyWorkJobByPage")) && !user.manages()) {
                    response.sendError(403); return false;
                }
                request.setAttribute("user", user);
                return true;
            }
        }).addPathPatterns("/**").excludePathPatterns("/auth/login", "/error");
    }
}
