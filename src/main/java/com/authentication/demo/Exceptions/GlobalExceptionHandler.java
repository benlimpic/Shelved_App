package com.authentication.demo.Exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CollectionCreationException.class)
    public String handleCollectionCreationException(CollectionCreationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/create-collection";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
        // Log the error (optional)

        // Add an error message to the redirect attributes
        redirectAttributes.addFlashAttribute("error", "An unexpected error occurred: " + e.getMessage());

        // Redirect to the index page
        return "redirect:/index";
    }
}

