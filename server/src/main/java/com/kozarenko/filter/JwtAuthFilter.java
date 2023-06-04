package com.kozarenko.filter;

import com.kozarenko.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.kozarenko.util.Constants.Path.H2_PATH;
import static com.kozarenko.util.Constants.Request.OPTIONS_METHOD;
import static com.kozarenko.util.Constants.Auth.AUTHORIZATION_HEADER;
import static com.kozarenko.util.Constants.Auth.USERNAME_ATTRIBUTE;
import static com.kozarenko.util.Constants.Auth.BEARER;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;

  private final List<String> allowedPaths = List.of(H2_PATH, "/api/authentication/login", "/api/authentication/registration");

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
          throws ServletException, IOException {

    if (allowedPaths.stream().anyMatch(req.getRequestURI()::startsWith) || req.getMethod().equals(OPTIONS_METHOD)) {
      chain.doFilter(req, resp);
      return;
    }

    String authHeader = req.getHeader(AUTHORIZATION_HEADER);

    if (authHeader != null && !authHeader.contains("null")) {
      authHeader = authHeader.substring(BEARER.length());
      try {
        req.setAttribute(USERNAME_ATTRIBUTE, jwtTokenUtil.checkTokenValidAndReturnUsername(authHeader));
      } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
               | SignatureException | IllegalArgumentException e) {
        resp.setStatus(401);
        return;
      }
      chain.doFilter(req, resp);
    } else {
      req.setAttribute(USERNAME_ATTRIBUTE, null);
      resp.setStatus(401);
    }
  }
}
