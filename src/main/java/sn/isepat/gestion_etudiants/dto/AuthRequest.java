package sn.isepat.gestion_etudiants.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String motDePasse;
}