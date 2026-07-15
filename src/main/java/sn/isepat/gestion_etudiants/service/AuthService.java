package sn.isepat.gestion_etudiants.service;

import sn.isepat.gestion_etudiants.entity.Utilisateur;
import sn.isepat.gestion_etudiants.repository.UtilisateurRepository;
import sn.isepat.gestion_etudiants.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public boolean emailExiste(String email) {
        return utilisateurRepository.findByEmail(email).isPresent();
    }

    public Utilisateur inscrire(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        if (utilisateur.getRole() == null) utilisateur.setRole("USER");
        return utilisateurRepository.save(utilisateur);
    }

    /** Retourne le token si les identifiants sont valides, sinon Optional.empty() */
    public Optional<String> authentifier(String email, String motDePasse) {
        return utilisateurRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(motDePasse, u.getMotDePasse()))
                .map(u -> jwtUtil.genererToken(u.getEmail()));
    }
}