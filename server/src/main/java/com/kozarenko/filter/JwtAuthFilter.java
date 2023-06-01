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

import static com.kozarenko.util.Constants.Path.H2_PAGE;
import static com.kozarenko.util.Constants.Path.MAIN_PAGE;
import static com.kozarenko.util.Constants.Auth.AUTHORIZATION_HEADER;
import static com.kozarenko.util.Constants.Auth.BEARER;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
          throws ServletException, IOException {

    if (req.getRequestURI().startsWith(H2_PAGE) || req.getRequestURI().equals(MAIN_PAGE)) {
      chain.doFilter(req, resp);
      return;
    }

    String authHeader = req.getHeader(AUTHORIZATION_HEADER);

    if (authHeader != null && !authHeader.contains("null")) {
      authHeader = authHeader.substring(BEARER.length());
      try {
        jwtTokenUtil.checkTokenValidAndReturnUsername(authHeader);
      } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
               | SignatureException | IllegalArgumentException e) {
        resp.setStatus(401);
        return;
      }
      chain.doFilter(req, resp);
    } else {
      resp.setStatus(401);
    }
  }
}
