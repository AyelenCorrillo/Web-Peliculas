package com.example.web_peliculas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.web_peliculas.DTO.MovieDTO;
import com.example.web_peliculas.DTO.MovieResponse;
import com.example.web_peliculas.DTO.SerieDTO;
import com.example.web_peliculas.DTO.SerieResponse;
import com.example.web_peliculas.DTO.VideoDTO;
import com.example.web_peliculas.DTO.VideoResponse;



@Service
public class MovieService {
    
    private final RestClient restClient;

    @Value("${api.external.key}")
    private String apiKey;

    @Value("${api.external.url}")
    private String baseUrl;

    public MovieService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<MovieDTO> fetchPopularMovies() {
        MovieResponse response = restClient.get()
            .uri(baseUrl + "/movie/popular?api_key=" + apiKey + "&language=es-ES")
            .retrieve()
            .body(MovieResponse.class);

        return response != null ? response.results() : List.of();
    }

    public List<MovieDTO> fetchByGenre(String type, String genreId) {
        MovieResponse response = restClient.get()
            .uri(baseUrl + "/discover/" + type + "?api_key=" + apiKey + "&with_genres=" + genreId + "&language=es-ES")
            .retrieve()
            .body(MovieResponse.class);
        return response != null ? response.results() : List.of();
    }

    public String fetchTrailerKey(String type, Long id) {
        VideoResponse res = restClient.get()
            .uri(baseUrl + "/" + type + "/" + id + "/videos?api_key=" + apiKey)
            .retrieve()
            .body(VideoResponse.class);

        if (res == null || res.results() == null) return null; {
            System.out.println("Videos encontrados por ID " + id + ": " + res.results());
        }
        System.out.println("Buscando trailer para ID: " + id);
        System.out.println("Respuesta de la API: " + res);

        return res.results().stream()
            .filter(v -> "YouTube".equalsIgnoreCase(v.site()) && "Trailer".equalsIgnoreCase(v.type()))
            .map(VideoDTO::key)
            .findFirst()
            .orElse(null);
    }

    public MovieDTO fetchMovieDetails(Long id) {
        return restClient.get()
            .uri(baseUrl + "/movie/" + id + "?api_key=" + apiKey + "&language=es-ES")
            .retrieve()
            .body(MovieDTO.class);
    }

    public List<MovieDTO> searchMovies(String query) {
        MovieResponse response = restClient.get()
            .uri(baseUrl + "/search/movie?api_key=" + apiKey + "&query=" + query + "&language=es-ES")
            .retrieve()
            .body(MovieResponse.class);
        return response != null ? response.results() : List.of();
    }


    public List<SerieDTO> fetchPopularSeries() {        
        SerieResponse response = restClient.get()
            .uri(baseUrl + "/tv/popular?api_key=" + apiKey + "&language=es-ES")
            .retrieve()
            .body(SerieResponse.class);

        return response != null ? response.results() : List.of();
    }

    public SerieDTO fetchSerieDetails(Long id) {
        return restClient.get()
            .uri(baseUrl + "/tv/" + id + "?api_key=" + apiKey + "&language=es-ES")
            .retrieve()
            .body(SerieDTO.class);
    }

    public List<SerieDTO> searchSeries(String query) {
        SerieResponse response = restClient.get()
            .uri(baseUrl + "/search/tv?api_key=" + apiKey + "&query=" + query + "&language=es-ES")
            .retrieve()
            .body(SerieResponse.class);
        return response != null ? response.results() : List.of();
    }

}
