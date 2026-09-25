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


//Esta anotación es la que hace que la clase sea un interceptor global de excepciones para toda la aplicación, va a identificar que hace la clase
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Usado para detectar errores de validacion en cascada sobre listas (Ej. 1)
    // Los errores se forman del tipo [N].cantidad 
    private static final Pattern INDEXED_FIELD_PATTERN = Pattern.compile("^\\[(\\d+)]\\.(.+)$");

    //Cada método de la clase tiene esta anotación con un tipo de excepción como parámetro, basicamente que excepcion agarrara segun el ejercicio
    @ExceptionHandler(MethodArgumentNotValidException.class)
    // Response: representa toda la respuesta HTTP, incluyendo el cuerpo, los encabezados y el código de estado
    // handleValidation:
    // Los ejercicios pidem dos formatos de error de validación distintos según el endpoint, por lo que esto lo resuelve y decirle que error de validacion es segun el ej.
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        //Stream.anyMatch: recorre todos los errores que detecto Spring, si es del tipo [N].algo significa que es del Ej1, sino es de otro
        //getBindingResult: se crea automatico con una lista con todos los errores, representado por fielError
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
                    //matcher.group: get del indice y del nombre del campo
                    posicion = Integer.parseInt(matcher.group(1));
                    campo = matcher.group(2);
                }
                //Aca arma el formato de como luego tirara el error, el DefaultMessage viene de los DTO que hice
                lista.add(new ErrorValidacionItem(posicion, campo, fieldError.getDefaultMessage()));
            }
            errores = lista;
        } else {
            //los errores aparecen en la respuesta en el mismo orden en que Spring los detectó, tmb los arma
            Map<String, String> mapa = new LinkedHashMap<>();
            for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                mapa.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            errores = mapa;
        }

        ApiResponse<Object> body =
                ApiResponse.of(HttpStatus.BAD_REQUEST.value(), "Error de validacion en los datos recibidos", errores);

                //códigos de estado con nombre, no numéricos. Es lo mismo que poner 400, etc
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
    //Este handler cubre un caso distinto: 
    //cuando la validación falla sobre un @RequestParam directo (no un objeto del body), por ejemplo el porcentaje del Ejercicio 1
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String mensaje = ex.getConstraintViolations().stream()
                //convierte cada violación en su mensaje de texto
                .map(ConstraintViolation::getMessage)
                //trae una colección de violaciones
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
    //este handler captura cualquier error que no haya sido anticipado por los handlers específicos de arriba
    //por ejemplo errores no controlados como problemas de conexion, etc
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        ApiResponse<Object> body = ApiResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrio un error interno inesperado: " + ex.getMessage(),
                null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}