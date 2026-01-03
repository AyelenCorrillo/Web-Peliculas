package com.example.web_peliculas.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.web_peliculas.model.PeliculaFavorita;
import com.example.web_peliculas.model.Usuario;
import com.example.web_peliculas.repository.PeliculaFavoritaRepository;
import com.example.web_peliculas.repository.UsuarioRepository;
import com.example.web_peliculas.service.MovieService;


@Controller
public class MovieController {

    private final MovieService movieService;

    @Autowired
    private PeliculaFavoritaRepository favoritaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        // Enviamos la lista de películas a la vista de Thymeleaf
        model.addAttribute("movies", movieService.fetchPopularMovies());
        model.addAttribute("username", principal.getName()); //nombre del usuario
        return "index"; 
    }

    @GetMapping("/pelicula/{id}")
    public String movieDetails(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.fetchMovieDetails(id));
        model.addAttribute("trailerKey", movieService.fetchTrailerKey("movie", id));
        return "detalles";
    }

    @GetMapping("/favoritas")
    public String listarFavoritas(Model model, Principal principal) {
        // Obtenemos el nombre del usuario logueado
        String username = principal.getName();
        
        // Buscamos sus películas en la base de datos
        List<PeliculaFavorita> favoritas = favoritaRepository.findByUsuarioUsername(username);
        
        model.addAttribute("favoritas", favoritas);
        model.addAttribute("username", username);
        
        return "mis-favoritas"; // Nombre del nuevo HTML
    }

    @GetMapping("/genero/{id}")
    public String filtrarPorGenero(@PathVariable String id, Model model, Principal principal) {
        // Usamos el método que ya tienes en MovieService
        model.addAttribute("movies", movieService.fetchByGenre("movie", id));
        model.addAttribute("username", principal.getName());
        return "index"; // Recargamos la misma página pero con la lista filtrada
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam String q, Model model, Principal principal) {
        model.addAttribute("movies", movieService.searchMovies(q));
        model.addAttribute("username", principal.getName());
        model.addAttribute("queryActual", q); // Para mostrar "Resultados para: ..."
        return "index"; // Reutilizamos el index para mostrar los resultados
    }

    @PostMapping("/favoritas/agregar")
    public String agregarFavorita(@RequestParam Long movie_id, 
                                @RequestParam String title, 
                                @RequestParam String poster_path, 
                                Principal principal) {
        
        // Obtiene el nombre del usuario desde la sesion actual
        String username = principal.getName();
        Usuario usuario = usuarioRepository.findByUsername(principal.getName()).orElseThrow(); //busca al usuario logueado
         
        // Evita duplicados: Verifica si el usuario ya guardó esta película
        boolean yaEsFavorita = favoritaRepository.findByUsuarioUsername(username)
            .stream()
            .anyMatch(f -> f.getMovie_id().equals(movie_id));
        if (!yaEsFavorita) {    
            // Si no existe, se crea el registro
            PeliculaFavorita fav = new PeliculaFavorita();
            fav.setMovie_id(movie_id);
            fav.setTitle(title);
            fav.setPoster_path(poster_path);
            fav.setUsuario(usuario);

            favoritaRepository.save(fav);
        }
        return "redirect:/favoritas";
    }

    @PostMapping("/favoritas/eliminar")
    public String eliminarFavorita(@RequestParam Long id_interno) {
        // Borramos directamente por el ID de nuestra tabla en MySQL
        favoritaRepository.deleteById(id_interno);       
        // Redirigimos de vuelta a la lista de favoritas para ver el cambio
        return "redirect:/favoritas";
    }

    @GetMapping("/series")
    public String listarSeries(Model model, Principal principal) {
        model.addAttribute("series", movieService.fetchPopularSeries());
        model.addAttribute("username", principal.getName());
        return "series";
    }

    @GetMapping("/serie/{id}")
    public String serieDetails(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("serie", movieService.fetchSerieDetails(id));
        model.addAttribute("trailerKey", movieService.fetchTrailerKey("tv", id)); 
        model.addAttribute("username", principal.getName());
        return "detalles-serie"; 
    }

    @GetMapping("/buscar-serie")
    public String buscarSerie(@RequestParam String q, Model model, Principal principal) {
        model.addAttribute("series", movieService.searchSeries(q));
        model.addAttribute("username", principal.getName());
        model.addAttribute("queryActual", q);
        return "series";
    }
 
    
}
