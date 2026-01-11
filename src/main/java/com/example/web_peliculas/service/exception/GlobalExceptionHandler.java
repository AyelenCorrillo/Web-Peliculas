package com.example.web_peliculas.service.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MovieApiException.class)
    public String handleMovieException(MovieApiException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error"; 
    }
    
}
