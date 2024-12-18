package com.fbr.tech.BitBank.exception;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class BitBankException extends RuntimeException {

    public BitBankException(String message) {
        super(message);
    }

    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("BitBank Internal Error");
        pd.setDetail("Por favor, entre em contato com o suporte.");
        return pd;
    }
}
