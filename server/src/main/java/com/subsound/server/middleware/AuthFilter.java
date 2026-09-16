package com.subsound.server.middleware;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String username = request.getParameter("u");
        String version = request.getParameter("v");
        String client = request.getParameter("c");

        String salt = request.getParameter("s");
        String token = request.getParameter("t");
        String password = request.getParameter("p");


        if (username == null || version == null || client == null) {
            inviaErrore(response, "Parametri obbligatori mancanti (u, v, c)");
            return;
        }

        boolean isAuthenticated = false;
        if(token != null && salt != null) {
            // TODO: Recupera password da DB ed effettua MD5(password + salt) == token
            isAuthenticated = true; //TEMP
        }
        else if (password != null) {
            // TODO: Verifica password in chiaro o 'enc:' con il DB
            isAuthenticated = true;
        }

        //-----ESITO AUTENTICAZIONE-----
        if(isAuthenticated) {
            filterChain.doFilter(request, response);
        } else {
            inviaErrore(response, "Autenticazione fallita: credenziali non valide");
        }
    }

    private void inviaErrore(HttpServletResponse response, String messaggio) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        // TODO: inserire la struttura JSON ufficiale di SubSonic
        response.getWriter().write("{\"subsonic-response\":{\"status\":\"failed\",\"error\":{\"code\":40,\"message\":\"" + messaggio + "\"}}}");
    }
}
