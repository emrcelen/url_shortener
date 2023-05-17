package com.wenthor.urlshortener.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;

import java.security.Key;
import java.util.function.Function;


public final class AccountUtils {

    private static <T> T exportToken(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getKey()) // parse işlemi ederken secret_key değerin atamasını yapıyoruz.
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsTFunction.apply(claims);
    }
    private static Key getKey() {
        byte [] key = Decoders.BASE64.decode("SecretShortenerURL003AppKeySecretShortenerURL002AppKeySecretShortenerURL001AppKey");
        return Keys.hmacShaKeyFor(key);
    }
    private static String getHeadersToken(HttpHeaders headers){
       return headers.get(HttpHeaders.AUTHORIZATION).get(0).substring(7);
    }
    public static String findByAccountEmail(HttpHeaders headers){
       return exportToken(getHeadersToken(headers), Claims::getSubject);
    }
}
