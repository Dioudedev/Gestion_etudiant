package sn.isepat.gestion_etudiants.repository;

import sn.isepat.gestion_etudiants.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    boolean existsByMatricule(String matricule);
    boolean existsByEmail(String email);

    Optional<Etudiant> findByMatricule(String matricule);       // bonus
    List<Etudiant> findAllByOrderByNomAsc();                     // bonus
}