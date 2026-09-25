package com.aydsii.tp2;

import com.aydsii.tp2.Ej1.exception.ErrorValidacionItem;
import com.aydsii.tp2.Ej3.exception.ExternalServiceException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Usado para detectar errores de validacion en cascada sobre listas (Ej. 1)
    private static final Pattern INDEXED_FIELD_PATTERN = Pattern.compile("^\\[(\\d+)]\\.(.+)$");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        boolean esListaIndexada = ex.getBindingResult().getFieldErrors().stream()
                .anyMatch(fe -> INDEXED_FIELD_PATTERN.matcher(fe.getField()).matches());

        Object errores;
        if (esListaIndexada) {
            List<ErrorValidacionItem> lista = new ArrayList<>();
            for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                String field = fieldError.getField();
                int posicion = -1;
                String campo = field;
                Matcher matcher = INDEXED_FIELD_PATTERN.matcher(field);
                if (matcher.matches()) {
                    posicion = Integer.parseInt(matcher.group(1));
                    campo = matcher.group(2);
                }
                lista.add(new ErrorValidacionItem(posicion, campo, fieldError.getDefaultMessage()));
            }
            errores = lista;
        } else {
            Map<String, String> mapa = new LinkedHashMap<>();
            for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                mapa.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            errores = mapa;
        }

        ApiResponse<Object> body =
                ApiResponse.of(HttpStatus.BAD_REQUEST.value(), "Error de validacion en los datos recibidos", errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(BadRequestException ex) {
        ApiResponse<Object> body = ApiResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException ex) {
        ApiResponse<Object> body = ApiResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiResponse<Object>> handleExternalServiceError(ExternalServiceException ex) {
        ApiResponse<Object> body = ApiResponse.of(HttpStatus.BAD_GATEWAY.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String mensaje = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        ApiResponse<Object> body = ApiResponse.of(HttpStatus.BAD_REQUEST.value(), mensaje, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        ApiResponse<Object> body = ApiResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Falta el parametro obligatorio: " + ex.getParameterName(),
                null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        ApiResponse<Object> body = ApiResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrio un error interno inesperado: " + ex.getMessage(),
                null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}