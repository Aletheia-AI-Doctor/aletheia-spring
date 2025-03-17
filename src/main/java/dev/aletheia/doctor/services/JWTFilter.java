package dev.aletheia.doctor.services;

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
        String token = null;
        Long userId = null;
        boolean isAdmin = false;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userId = jwtService.extractUserId(token);
            isAdmin = jwtService.isAdmin(token);
        }

//        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            AuthenticatableService authService = isAdmin
//                    ? context.getBean(AdminService.class)
//                    : context.getBean(UserService.class);
//
//            Authenticatable auth = authService.getAuthenticatableById(userId);
//
//            Details userDetail = isAdmin
//                    ? new AdminDetail((Admin) auth)
//                    : new UserDetail((User) auth);
//
//            if (jwtService.isTokenValid(token, userDetail)) {
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            } else{
//                throw new UnauthorizedAccessException("Token is invalid, unauthorized access");
//            }
//        }
        filterChain.doFilter(request, response);
    }
}
