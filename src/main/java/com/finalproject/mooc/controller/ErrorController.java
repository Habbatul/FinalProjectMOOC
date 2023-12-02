package com.finalproject.mooc.controller;

import com.finalproject.mooc.model.responses.WebResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    //bila nantinya ada exception ResponseStatusException arahkan kesini
    //saya tambah annotation @ResponseStatus biar muncul di doc, karena rata2 juga custom nya di bad_request
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> adaException(ResponseStatusException error) {
        return ResponseEntity.status(
                        error.getRawStatusCode())
                .body(WebResponse.
                        <String>builder()
                        .error(error.getReason())
                        .build()
                );
    }
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<WebResponse<String>> handleDataIntegrityViolation(DataIntegrityViolationException error) {
//        return ResponseEntity.badRequest()
//                .body(WebResponse.<String>builder()
//                        .error("Ada kesalahan integritas data, cek field unik")
//                        .build());
//    }
}
