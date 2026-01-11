package com.example.web_peliculas.service;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import com.example.web_peliculas.DTO.MovieDTO;
import com.example.web_peliculas.service.exception.MovieApiException;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {
    @Mock
    private RestClient restClient;
    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private MovieServiceImpl movieService;

   @Test
    void cuandoIdEsValido_debeRetornarPelicula() {
        // GIVEN: Preparamos la película falsa
        MovieDTO peliFalsa = new MovieDTO(100L, "Pelicula Test", "Resumen", "/path.jpg", 8.5);

        // Simulamos toda la cadena de llamadas del RestClient
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(Predicate.class), any())).thenReturn(responseSpec);
        when(responseSpec.body(MovieDTO.class)).thenReturn(peliFalsa);

        // WHEN: Ejecutamos el método
        MovieDTO resultado = movieService.fetchMovieDetails(100L);

        // THEN: Verificamos que no sea nulo y que el título coincida
        assertNotNull(resultado);
        assertEquals("Pelicula Test", resultado.title());
    }

    @Test
    void cuandoApiDaError_debeLanzarMovieApiException() {
        // GIVEN: Configuramos el mock para que la cadena llegue hasta retrieve()
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        // Aquí simulamos que onStatus detecta un error y lanza nuestra excepción
        // Usamos doThrow para simular el comportamiento de lanzar la excepción
        when(responseSpec.onStatus(any(Predicate.class), any())).thenThrow(new MovieApiException("Error de API simulado"));

        // WHEN & THEN: Verificamos que al llamar al servicio, se lance la excepción
        MovieApiException excepcion = assertThrows(MovieApiException.class, () -> {
            movieService.fetchMovieDetails(1L);
        });

        // Verificamos que el mensaje sea el que esperamos
        assertEquals("Error de API simulado", excepcion.getMessage());
    }
  
    
}
