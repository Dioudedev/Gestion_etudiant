package sn.isepat.gestion_etudiants.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey cle = Keys.hmacShaKeyFor(
            "isepat-secret-key-tres-longue-pour-hs256-2026".getBytes());

    private final long dureeValiditeMs = 3600_000;

    public String genererToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + dureeValiditeMs))
                .signWith(cle)
                .compact();
    }

    public String extraireEmail(String token) {
        return Jwts.parser().verifyWith(cle).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean estValide(String token) {
        try {
            Jwts.parser().verifyWith(cle).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}