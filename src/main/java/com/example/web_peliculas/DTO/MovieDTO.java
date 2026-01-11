package com.example.web_peliculas.DTO;

public record MovieDTO(
    Long id,
    String title,
    String overview,
    String poster_path,
    Double vote_average
) {}