package sn.isepat.gestion_etudiants.controller;

import sn.isepat.gestion_etudiants.dto.AuthRequest;
import sn.isepat.gestion_etudiants.dto.ErrorResponse;
import sn.isepat.gestion_etudiants.dto.TokenResponse;
import sn.isepat.gestion_etudiants.entity.Utilisateur;
import sn.isepat.gestion_etudiants.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Inscription et connexion des utilisateurs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Inscrire un nouvel utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Compte créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Champ obligatoire manquant"),
            @ApiResponse(responseCode = "409", description = "Email déjà existant")
    })
    public ResponseEntity<?> register(@RequestBody Utilisateur utilisateur) {
        if (utilisateur.getEmail() == null || utilisateur.getEmail().isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "L'email est obligatoire."));
        if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Le mot de passe est obligatoire."));
        if (authService.emailExiste(utilisateur.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, "Cet email existe déjà."));

        Utilisateur cree = authService.inscrire(utilisateur);
        cree.setMotDePasse(null); // on ne renvoie jamais le mot de passe encodé
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @PostMapping("/login")
    @Operation(summary = "Authentifier un utilisateur et obtenir un token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentification réussie, token retourné"),
            @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect")
    })
    public ResponseEntity<?> login(@RequestBody AuthRequest requete) {
        return authService.authentifier(requete.getEmail(), requete.getMotDePasse())
                .<ResponseEntity<?>>map(token -> ResponseEntity.ok(new TokenResponse(token)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse(401, "Email ou mot de passe incorrect.")));
    }
}