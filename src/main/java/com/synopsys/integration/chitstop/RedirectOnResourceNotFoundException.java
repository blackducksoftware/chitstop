package com.synopsys.integration.chitstop;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class RedirectOnResourceNotFoundException {
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Object handleStaticResourceNotFound(NoHandlerFoundException ex, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        if (req.getRequestURI().startsWith("/api"))
            return getApiResourceNotFoundBody(ex, req);
        else {
            return "redirect:/index.html";
        }
    }

    private ResponseEntity<String> getApiResourceNotFoundBody(NoHandlerFoundException ex, HttpServletRequest req) {
        return new ResponseEntity<>("api not found", HttpStatus.NOT_FOUND);
    }

}
