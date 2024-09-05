package com.bilgeadam.config.security;

import com.bilgeadam.exception.AuthServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.utilty.JwtTokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final JwtUserDetails jwtUserDetails;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null){
            String token = bearerToken.replace("Bearer ", "");

            Long authId = jwtTokenManager.getIdFromToken(token).orElseThrow(() -> new AuthServiceException(ErrorType.INVALID_TOKEN));

            UserDetails userDetails = jwtUserDetails.loadUserByAuthid(authId);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                            null,
                            userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            System.out.println(userDetails.getAuthorities());
        }
        filterChain.doFilter(request,
                response);

    }
}
