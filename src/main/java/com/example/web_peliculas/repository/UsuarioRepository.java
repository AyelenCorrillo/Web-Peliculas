package com.example.web_peliculas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web_peliculas.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    
}
