package sn.isepat.gestion_etudiants.service;

import sn.isepat.gestion_etudiants.entity.Etudiant;
import sn.isepat.gestion_etudiants.repository.EtudiantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public Etudiant ajouter(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    public Etudiant modifier(Long id, Etudiant donnees) {
        Etudiant etudiant = etudiantRepository.findById(id).orElseThrow();
        etudiant.setMatricule(donnees.getMatricule());
        etudiant.setPrenom(donnees.getPrenom());
        etudiant.setNom(donnees.getNom());
        etudiant.setEmail(donnees.getEmail());
        etudiant.setDateNaissance(donnees.getDateNaissance());
        etudiant.setLieuNaissance(donnees.getLieuNaissance());
        etudiant.setNationalite(donnees.getNationalite());
        return etudiantRepository.save(etudiant);
    }

    public void supprimer(Long id) {
        etudiantRepository.deleteById(id);
    }

    public Optional<Etudiant> rechercher(Long id) {
        return etudiantRepository.findById(id);
    }

    public List<Etudiant> lister() {
        return etudiantRepository.findAll();
    }

    public boolean existeParId(Long id) { return etudiantRepository.existsById(id); }
    public boolean existeMatricule(String matricule) { return etudiantRepository.existsByMatricule(matricule); }
    public boolean existeEmail(String email) { return etudiantRepository.existsByEmail(email); }

    public boolean matriculeUtiliseParAutre(String matricule, Long id) {
        return etudiantRepository.findByMatricule(matricule).map(e -> !e.getId().equals(id)).orElse(false);
    }

    public boolean emailUtiliseParAutre(String email, Long id) {
        return etudiantRepository.findAll().stream()
                .anyMatch(e -> e.getEmail().equalsIgnoreCase(email) && !e.getId().equals(id));
    }

    public Optional<Etudiant> rechercherParMatricule(String matricule) {
        return etudiantRepository.findByMatricule(matricule);
    }

    public List<Etudiant> listerTrieParNom() {
        return etudiantRepository.findAllByOrderByNomAsc();
    }
}