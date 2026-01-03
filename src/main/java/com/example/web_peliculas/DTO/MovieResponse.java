package com.example.web_peliculas.DTO;

import java.util.List;

public record MovieResponse(
    
    List<MovieDTO> results
    
) {}
