package com.example.web_peliculas.DTO;

/**
 * Record que representa los datos de una película provenientes de la API.
 * Los nombres de los campos deben coincidir con el JSON de la API externa
 * para que Spring pueda mapearlos automáticamente.
 */
public record MovieDTO(
    Long id,
    String title,
    String overview,
    String poster_path,
    Double vote_average
) {}