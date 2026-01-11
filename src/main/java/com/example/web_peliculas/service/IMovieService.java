package com.example.web_peliculas.service;

import java.util.List;
import com.example.web_peliculas.DTO.MovieDTO;
import com.example.web_peliculas.DTO.SerieDTO;

public interface IMovieService {
    List<MovieDTO> fetchPopularMovies();
    List<MovieDTO> fetchByGenre(String type, String genreId);
    String fetchTrailerKey(String type, Long id);
    MovieDTO fetchMovieDetails(Long id);
    List<MovieDTO> searchMovies(String query);
    List<SerieDTO> fetchPopularSeries();
    SerieDTO fetchSerieDetails(Long id);
    List<SerieDTO> searchSeries(String query);
    
}
