package com.fbr.tech.BitBank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class StatementOperationTypeException extends BitBankException {

    private final String detail;

    public StatementOperationTypeException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
         var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
         pd.setTitle("Não foi possível realizar a operação");
         pd.setDetail(detail);
         return pd;
    }
}
