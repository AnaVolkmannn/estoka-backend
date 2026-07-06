// src/main/java/com/estoka/diorana/exception/GlobalExceptionHandler.java
package com.estoka.diorana.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─── 409 · Mês fechado ──────────────────────────────────────────────────

    @ExceptionHandler(MesFechadoException.class)
    public ResponseEntity<Map<String, Object>> handleMesFechado(MesFechadoException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ─── 400 · Erros de validação (@Valid) ─────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                erros.put(err.getField(), err.getDefaultMessage())
        );

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Dados inválidos");
        body.put("campos", erros);
        return ResponseEntity.badRequest().body(body);
    }

    // ─── 404 · "Não encontrado" (RuntimeException genérica usada nos services) ──

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "Erro inesperado";

        // Convenção adotada nos services: mensagens terminando em
        // "não encontrado(a)" viram 404; o resto vira 400.
        if (msg.toLowerCase().contains("não encontrado")) {
            return buildResponse(HttpStatus.NOT_FOUND, msg);
        }

        log.warn("RuntimeException não mapeada especificamente: {}", msg);
        return buildResponse(HttpStatus.BAD_REQUEST, msg);
    }

    // ─── 500 · Qualquer outra coisa não prevista ────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenerico(Exception ex) {
        log.error("Erro não tratado", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor");
    }

    // ─── Helper ─────────────────────────────────────────────────────────────

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String mensagem) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", mensagem);
        return ResponseEntity.status(status).body(body);
    }
}