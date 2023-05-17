package com.wenthor.urlshortener.security.filter;


import com.wenthor.urlshortener.security.service.CustomUserDetailService;
import com.wenthor.urlshortener.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService service;
    private final UserDetailsService userService;

    public JwtAuthenticationFilter(JwtService service, CustomUserDetailService userService) {
        this.service = service;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        final String token;
        final String username;
        if(header == null || !header.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        try{
            token = header.substring(7); // Bearer kısmını kırpıyoruz.
            username = service.findUsername(token);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userService.loadUserByUsername(username);
                if(service.tokenControl(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken
                            (userDetails,null,userDetails.getAuthorities());
                    // token tekrar oluşturulduğunda mevcut context holder içerisindekinin güncellenemsi için set işlemi yapacağız.
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }catch (ExpiredJwtException ex){

        }

        filterChain.doFilter(request,response);
    }
}
