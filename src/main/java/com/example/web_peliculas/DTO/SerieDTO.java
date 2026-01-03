package com.example.web_peliculas.DTO;

public record SerieDTO(
    Long id,
    String name,
    String overview,
    String poster_path,
    Double vote_average
) {}
