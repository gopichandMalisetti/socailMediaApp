package com.tasks.socialMediaApp.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasks.socialMediaApp.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

     JwtUtils jwtUtils;

    CustomUserDetailsService customUserDetailsService;

    @Autowired
    AuthTokenFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService){
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            System.out.println("try block entered : ");
            String jwt = parseJwt(request);
            if(jwt != null){

                try {
                    jwtUtils.validateJwtToken(jwt);
                }catch (Exception exception){
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                    final Map<String, Object> body = new HashMap<>();
                    body.put("Status", HttpServletResponse.SC_UNAUTHORIZED);
                    body.put("error", "unauthorized");
                    body.put("message", exception.getMessage());
                    body.put("path", request.getServletPath());

                    final ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(response.getOutputStream(), body);
                    return;
                }

                System.out.println("validation was successfull");
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                System.out.println("username:" + userDetails.getUsername());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
           }
       }catch (Exception e){
            System.out.println("Exception found in doFilter method:" + e.getMessage());
        }

        filterChain.doFilter(request,response);
    }

    private String parseJwt(HttpServletRequest request){
        String jwt = jwtUtils.getJwtFromHeader(request);
        System.out.println("jwt token : " + jwt);
        return jwt;
    }


}
