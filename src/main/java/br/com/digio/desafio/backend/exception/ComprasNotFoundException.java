package br.com.digio.desafio.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ComprasNotFoundException extends RuntimeException{
    public ComprasNotFoundException(String message){
        super(message);
    }
}
