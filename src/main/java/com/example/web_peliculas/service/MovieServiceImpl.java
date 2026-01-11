package com.example.web_peliculas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.web_peliculas.DTO.MovieDTO;
import com.example.web_peliculas.DTO.MovieResponse;
import com.example.web_peliculas.DTO.SerieDTO;
import com.example.web_peliculas.DTO.SerieResponse;
import com.example.web_peliculas.DTO.VideoDTO;
import com.example.web_peliculas.DTO.VideoResponse;
import com.example.web_peliculas.service.exception.MovieApiException;


@Service
public class MovieServiceImpl implements IMovieService {
    
    private final RestClient restClient;

    @Value("${api.external.key}")
    private String apiKey;

    @Value("${api.external.url}")
    private String baseUrl;

    public MovieServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<MovieDTO> fetchPopularMovies() {
        MovieResponse response = restClient.get()
            .uri(baseUrl + "/movie/popular?api_key=" + apiKey + "&language=es-ES")
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, response1) -> {
                throw new RuntimeException("Error al obtener películas populares");
            })
            .body(MovieResponse.class);

        return response != null ? response.results() : List.of();
    }

    @Override
    public List<MovieDTO> fetchByGenre(String type, String genreId) {
        MovieResponse response = restClient.get()
            .uri(baseUrl + "/discover/" + type + "?api_key=" + apiKey + "&with_genres=" + genreId + "&language=es-ES")
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, response1) -> {
                throw new RuntimeException("Error al filtrar por género");
            })
            .body(MovieResponse.class);
        return response != null ? response.results() : List.of();
    }

    @Override
    public String fetchTrailerKey(String type, Long id) {
        VideoResponse res = restClient.get()
            .uri(baseUrl + "/" + type + "/" + id + "/videos?api_key=" + apiKey)
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, response1) -> {
                throw new RuntimeException("Error al buscar el trailer");
            })
            .body(VideoResponse.class);

        if (res == null || res.results() == null) return null; 

        return res.results().stream()
            .filter(v -> "YouTube".equalsIgnoreCase(v.site()) && "Trailer".equalsIgnoreCase(v.type()))
            .map(VideoDTO::key)
            .findFirst()
            .orElse(null);
    }

    @Override
    public MovieDTO fetchMovieDetails(Long id) {
        return restClient.get()
            .uri(baseUrl + "/movie/" + id + "?api_key=" + apiKey + "&language=es-ES")
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new MovieApiException("No pudimos encontrar la película con ID: " + id);
            })
            .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                throw new MovieApiException("El servidor de películas está teniendo problemas técnicos.");
            })
            .body(MovieDTO.class);
    }

    @Override
    public List<MovieDTO> searchMovies(String query) {
        MovieResponse response = restClient.get()
            .uri(baseUrl + "/search/movie?api_key=" + apiKey + "&query=" + query + "&language=es-ES")
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, response1) -> {
                throw new RuntimeException("Error en la búsqueda de películas");
            })
            .body(MovieResponse.class);
        return response != null ? response.results() : List.of();
    }

    @Override
    public List<SerieDTO> fetchPopularSeries() {        
        SerieResponse response = restClient.get()
            .uri(baseUrl + "/tv/popular?api_key=" + apiKey + "&language=es-ES")
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, response1) -> {
                throw new RuntimeException("Error al obtener series populares");
            })
            .body(SerieResponse.class);

        return response != null ? response.results() : List.of();
    }

    @Override
    public SerieDTO fetchSerieDetails(Long id) {
        return restClient.get()
            .uri(baseUrl + "/tv/" + id + "?api_key=" + apiKey + "&language=es-ES")
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new RuntimeException("La serie solicitada no existe.");
            })
            .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                throw new RuntimeException("Error de servidor en TMDB.");
            })
            .body(SerieDTO.class);
    }

    @Override
    public List<SerieDTO> searchSeries(String query) {
        SerieResponse response = restClient.get()
            .uri(baseUrl + "/search/tv?api_key=" + apiKey + "&query=" + query + "&language=es-ES")
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, response1) -> {
                throw new RuntimeException("Error en la búsqueda de series");
            })
            .body(SerieResponse.class);
        return response != null ? response.results() : List.of();
    }

}
