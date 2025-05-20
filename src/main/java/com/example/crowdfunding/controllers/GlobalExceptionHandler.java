package com.example.crowdfunding.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.crowdfunding.exception.AlreadyExistsException;
import com.example.crowdfunding.exception.InvalidAttributeException;
import com.example.crowdfunding.exception.NoContentException;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExists(AlreadyExistsException ex) {
        return new ResponseEntity<>("erreur : " + ex.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<String> handleNoContentException(NoContentException ex) {
        return new ResponseEntity<>("erreur : " + ex.getMessage(), HttpStatus.NOT_FOUND); // 404 Not found 
    }

    @ExceptionHandler(InvalidAttributeException.class)
    public ResponseEntity<String> handleInvalidAttributeException(InvalidAttributeException ex) {
        return new ResponseEntity<>("erreur : " + ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad request 
    }

}