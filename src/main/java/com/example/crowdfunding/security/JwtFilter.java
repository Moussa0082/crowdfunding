package com.example.crowdfunding.security;

// security/JwtFilter.java

import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.*;
import org.springframework.stereotype.Component;

import com.example.crowdfunding.repository.UtilisateurRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    @Autowired
    private  JwtUtil jwtUtil;

    @Autowired
    private  UtilisateurRepository userRepository;

    // @Override
    // public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    //         throws IOException, ServletException {

    //     HttpServletRequest request = (HttpServletRequest) req;
    //     String authHeader = request.getHeader("Authorization");

    //     if (authHeader != null && authHeader.startsWith("Bearer ")) {
    //         String token = authHeader.substring(7);
    //         try {
    //             if (jwtUtil.validateToken(token)) {
    //                 String email = jwtUtil.extractEmail(token);

    //                 userRepository.findByEmail(email).ifPresent(user -> {
    //                     UsernamePasswordAuthenticationToken auth =
    //                             new UsernamePasswordAuthenticationToken(email, null, null);
    //                     SecurityContextHolder.getContext().setAuthentication(auth);
    //                 });
    //             }
    //         } catch (JwtException ignored) {
    //         }
    //     }

    //     chain.doFilter(req, res);
    // }
    @Override
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        try {
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);

                userRepository.findByEmail(email).ifPresent(user -> {
                    // Ajouter l'utilisateur dans la requête pour y accéder plus tard
                    request.setAttribute("utilisateur", user);
                });
            }
        } catch (JwtException e) {
            // Token invalide ou expiré — tu peux retourner une erreur si tu veux
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalide ou expiré");
            return;
        }
    }

    chain.doFilter(request, response);
}

}
