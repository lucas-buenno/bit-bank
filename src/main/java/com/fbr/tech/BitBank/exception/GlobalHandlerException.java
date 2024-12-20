package com.fbr.tech.BitBank.exception;

import com.fbr.tech.BitBank.controllers.dto.InvalidParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(BitBankException.class)
    public ProblemDetail handlerBitBankException(BitBankException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        List<InvalidParam> invalidParams = e.getFieldErrors().stream()
                .map(fieldError ->
                        new InvalidParam(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        pd.setTitle("Não foi possível realizar a operação.");
        pd.setDetail("Por favor, verifique os campos com erros.");
        pd.setProperty("Campos com erro", invalidParams);

        return pd;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setTitle("Não foi possível realizar a ação");
        //pd.setDetail(e.getMessage());

        var invalidParams = new InvalidParam(e.getPropertyName(), e.getCause().toString());
        pd.setProperty("Parâmetro com erro", invalidParams);

        return pd;
    }
    
}
