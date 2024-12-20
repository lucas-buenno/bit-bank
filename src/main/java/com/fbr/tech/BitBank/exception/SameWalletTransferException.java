package com.fbr.tech.BitBank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class SameWalletTransferException extends BitBankException {

    private final String detail;

    public SameWalletTransferException(String detail) {
        super(detail);
        this.detail = detail;
    }


    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setTitle("Não foi possível realizar a operação");
        pd.setDetail(detail);
        return pd;
    }
}
