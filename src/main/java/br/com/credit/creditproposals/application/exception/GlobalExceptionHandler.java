package br.com.credit.creditproposals.application.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Obtém uma string formatada indicando a origem da exceção
     * (classe, método e linha onde ocorreu).
     */
    private String formatarOrigem(Throwable e) {
        if (e.getStackTrace().length > 0) {
            StackTraceElement origem = e.getStackTrace()[0];
            return String.format("%s.%s(linha %d)",
                    origem.getClassName(),
                    origem.getMethodName(),
                    origem.getLineNumber());
        }
        return "Origem desconhecida";
    }

    /**
     * Trata exceções do tipo IllegalArgumentException,
     * retornando HTTP 400 com a mensagem da exceção.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        logger.warn("Requisição inválida: {} | Origem: {}", e.getMessage(), formatarOrigem(e));
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Trata exceções de recurso não encontrado (NoSuchElementException),
     * retornando HTTP 404 com a mensagem da exceção.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElement(NoSuchElementException e) {
        logger.info("Recurso não encontrado: {} | Origem: {}", e.getMessage(), formatarOrigem(e));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Trata exceções genéricas não previstas,
     * retornando HTTP 500 com mensagem genérica.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        logger.error("Erro interno no servidor: {} | Origem: {}", e.getMessage(), formatarOrigem(e), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocorreu um erro interno no servidor.");
    }

    /**
     * Trata violações de restrições de validação (@Valid, @Validated),
     * retornando HTTP 400 com mensagem detalhada dos erros.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException e) {
        String mensagem = e.getConstraintViolations()
                .stream()
                .map(cv -> {
                    String propriedade = cv.getPropertyPath().toString();
                    String msg = cv.getMessage();
                    return String.format("Erro de validação no campo '%s': %s", propriedade, msg);
                })
                .collect(Collectors.joining("; "));
        logger.warn("Erro de validação: {} | Origem: {}", mensagem, formatarOrigem(e));
        return ResponseEntity.badRequest().body(mensagem);
    }

    /**
     * Trata requisições para endpoints inexistentes,
     * retornando HTTP 404 com mensagem para indicar que o endpoint não existe.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFound(NoHandlerFoundException e) {
        logger.info("Endpoint não encontrado: {} | Origem: {}", e.getMessage(), formatarOrigem(e));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Desculpe, o recurso que você tentou acessar não existe.");
    }

    /**
     * Trata erros de validação de argumentos em métodos (@Valid em DTOs),
     * retornando HTTP 400 com a primeira mensagem de erro.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String firstError = ex.getBindingResult()
                            .getFieldErrors()
                            .stream()
                            .findFirst()
                            .map(FieldError::getDefaultMessage)
                            .orElse("Erro desconhecido");

        logger.warn("Erro de validação: {} | Origem: {}", firstError, formatarOrigem(ex));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(firstError);
    }

    /**
     * Trata erros de incompatibilidade de tipo nos parâmetros dos métodos,
     * como quando uma String não pode ser convertida para Long,
     * retornando HTTP 400 com uma mensagem explicativa.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String msg = String.format("Parâmetro inválido: '%s' não pôde ser convertido para %s",
                e.getValue(), e.getRequiredType().getSimpleName());
        logger.warn(msg + " | Origem: " + formatarOrigem(e));
        return ResponseEntity.badRequest().body(msg);
    }

    /**
     * Trata exceções de estado inválido (IllegalStateException),
     * retornando HTTP 400 com a mensagem da exceção.
     * Usado, por exemplo, quando uma parcela já está paga
     * e uma operação inválida é tentada.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        logger.warn("Estado inválido: {} | Origem: {}", e.getMessage(), formatarOrigem(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}