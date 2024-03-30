package com.hatrongtan99.taskExam.exception;

import com.hatrongtan99.taskExam.dto.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerGlobal {
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ResponseBody> handleNotFoundException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBody(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        ));
    }

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<ResponseBody> handleConflictException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseBody(
                HttpStatus.CONFLICT,
                ex.getMessage()
        ));
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ResponseBody> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBody(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        ));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            error.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBody(
                HttpStatus.BAD_REQUEST,
                error.toString()
        ));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseBody> handleException(RuntimeException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseBody(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "internal server error"
        ));
    }
}
