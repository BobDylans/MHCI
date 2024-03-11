package com.example.mhcidemo.Interceptor;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Objects;

@Component
@CrossOrigin
public class JwtInterceptor implements HandlerInterceptor {

    private static final String SECRET_KEY = "yourSecretKeyYourSecretKeyYourSecretKeyYourSecretKeyYourSecretKey";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if ("OPTIONS".equals(method)) {
            return true;
        }

        String token = request.getHeader("token");
        if (Objects.nonNull(token)) {
            try {
                Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (ExpiredJwtException e) {
                return handleInvalidToken(response, "令牌已过期，请重新登录。");
            } catch (JwtException e) {
                return handleInvalidToken(response, "无效的令牌，请重试。");
            }
        }

        return handleInvalidToken(response, "缺少令牌，请提供令牌。");
    }

    private boolean handleInvalidToken(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
        return false;
    }
}
