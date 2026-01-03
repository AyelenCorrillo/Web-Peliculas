package com.example.web_peliculas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Entity
@Data
public class PeliculaFavorita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_interno;

    private Long movie_id; // El ID que viene de la API externa
    private String title;
    private String poster_path;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // Relación con el usuario logueado
    
}
