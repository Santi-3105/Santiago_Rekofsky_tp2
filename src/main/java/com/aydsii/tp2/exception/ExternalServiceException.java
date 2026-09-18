package com.aydsii.tp2.exception;

/*
 Se lanza cuando falla la comunicacion con un servicio externo
 (no responde, timeout, etc). 
 Es interceptada por GlobalExceptionHandler y traducida a HTTP 502.
 */
public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException(String message) {
        super(message);
    }
}