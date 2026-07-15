package sn.isepat.gestion_etudiants.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.isepat.gestion_etudiants.dto.ErrorResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                          org.springframework.security.core.AuthenticationException authException) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(),
                new ErrorResponse(401, "Token JWT manquant ou invalide."));
    }
}