package com.example.web_peliculas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web_peliculas.model.PeliculaFavorita;


public interface PeliculaFavoritaRepository extends JpaRepository<PeliculaFavorita, Long> {

    List<PeliculaFavorita> findByUsuarioUsername(String username);
    
}
