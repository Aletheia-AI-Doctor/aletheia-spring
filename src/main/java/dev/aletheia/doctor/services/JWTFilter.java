package dev.aletheia.doctor.services;

import dev.aletheia.doctor.exceptions.UnauthorizedException;
import dev.aletheia.doctor.models.Doctor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@AllArgsConstructor
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private ApplicationContext context;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        Long id = jwtService.extractUserId(token);

        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            DoctorService authService = context.getBean(DoctorService.class);

            Doctor doctor = authService.find(id);

            if (jwtService.isTokenValid(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(doctor, null, doctor.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
