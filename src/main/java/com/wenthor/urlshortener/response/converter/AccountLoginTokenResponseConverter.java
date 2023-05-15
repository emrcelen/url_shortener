package com.wenthor.urlshortener.response.converter;

import com.wenthor.urlshortener.request.AccountLoginRequest;
import com.wenthor.urlshortener.response.AccountLoginTokenResponse;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public final class AccountLoginTokenResponseConverter {
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public final AccountLoginTokenResponse convertToResponse(AccountLoginRequest request, String token, Date tokenIssuedAt, Date tokenExpirationTime) {
        return new AccountLoginTokenResponse(
                request.getEmail(),
                token,
                formatter.format(tokenIssuedAt),
                formatter.format(tokenExpirationTime)
                );
    }
}
