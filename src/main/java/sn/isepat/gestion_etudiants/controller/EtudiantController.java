package sn.isepat.gestion_etudiants.controller;

import sn.isepat.gestion_etudiants.dto.ErrorResponse;
import sn.isepat.gestion_etudiants.entity.Etudiant;
import sn.isepat.gestion_etudiants.service.EtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;

    @PostMapping
    public ResponseEntity<?> ajouter(@RequestBody Etudiant etudiant) {
        ErrorResponse erreur = controlerChampsObligatoires(etudiant);
        if (erreur != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);

        if (etudiantService.existeMatricule(etudiant.getMatricule()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, "Le matricule existe déjà."));

        if (etudiantService.existeEmail(etudiant.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, "L'email existe déjà."));

        return ResponseEntity.status(HttpStatus.CREATED).body(etudiantService.ajouter(etudiant));
    }

    @GetMapping
    public ResponseEntity<List<Etudiant>> lister(@RequestParam(required = false, defaultValue = "false") boolean trierParNom) {
        return ResponseEntity.ok(trierParNom ? etudiantService.listerTrieParNom() : etudiantService.lister());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> rechercher(@PathVariable Long id) {
        Optional<Etudiant> etudiant = etudiantService.rechercher(id);
        if (etudiant.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Étudiant introuvable."));
        return ResponseEntity.ok(etudiant.get());
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<?> rechercherParMatricule(@PathVariable String matricule) {
        Optional<Etudiant> etudiant = etudiantService.rechercherParMatricule(matricule);
        if (etudiant.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Étudiant introuvable."));
        return ResponseEntity.ok(etudiant.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifier(@PathVariable Long id, @RequestBody Etudiant etudiant) {
        if (!etudiantService.existeParId(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Étudiant introuvable."));

        ErrorResponse erreur = controlerChampsObligatoires(etudiant);
        if (erreur != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);

        if (etudiantService.matriculeUtiliseParAutre(etudiant.getMatricule(), id))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, "Le matricule existe déjà."));

        if (etudiantService.emailUtiliseParAutre(etudiant.getEmail(), id))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, "L'email existe déjà."));

        return ResponseEntity.ok(etudiantService.modifier(id, etudiant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimer(@PathVariable Long id) {
        if (!etudiantService.existeParId(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Étudiant introuvable."));
        etudiantService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    private ErrorResponse controlerChampsObligatoires(Etudiant e) {
        if (e.getMatricule() == null || e.getMatricule().isBlank()) return new ErrorResponse(400, "Le matricule est obligatoire.");
        if (e.getPrenom() == null || e.getPrenom().isBlank()) return new ErrorResponse(400, "Le prénom est obligatoire.");
        if (e.getNom() == null || e.getNom().isBlank()) return new ErrorResponse(400, "Le nom est obligatoire.");
        if (e.getEmail() == null || e.getEmail().isBlank()) return new ErrorResponse(400, "L'email est obligatoire.");
        if (e.getDateNaissance() == null) return new ErrorResponse(400, "La date de naissance est obligatoire.");
        if (e.getLieuNaissance() == null || e.getLieuNaissance().isBlank()) return new ErrorResponse(400, "Le lieu de naissance est obligatoire.");
        if (e.getNationalite() == null || e.getNationalite().isBlank()) return new ErrorResponse(400, "La nationalité est obligatoire.");
        return null;
    }
}