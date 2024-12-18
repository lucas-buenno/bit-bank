package com.fbr.tech.BitBank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class DataAlreadyExistsException extends BitBankException {

    private final String detail;

    public DataAlreadyExistsException(String detail) {
        super(detail);
        this.detail = detail;
    }


    @Override
    public ProblemDetail toProblemDetail() {

        var pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setTitle("Não foi possível cadastrar a carteira");
        pd.setDetail(detail);
        return pd;

    }
}
