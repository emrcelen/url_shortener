package com.wenthor.urlshortener.security.service;



import com.wenthor.urlshortener.model.Account;
import com.wenthor.urlshortener.repository.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    /**
     * Token bilgilerini parse, genarete etmek için kullanacağım temel servis katmanı olacak.
     */

    private final AccountRepository repository;

    @Value("${security.jwt.secret}")
    private String SECRET_KEY;

    public JwtService(AccountRepository repository) {
        this.repository = repository;
    }

    public String findUsername(String token) {
        return exportToken(token, Claims::getSubject);
    }
    public String findDeviceName(String token){ return exportToken(token, claims -> claims.get("device", String.class));}
    // Token yapımızı istediğimiz formatta parse ederek verecek metot.
    private <T> T exportToken(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getKey()) // parse işlemi ederken secret_key değerin atamasını yapıyoruz.
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsTFunction.apply(claims);
    }

    private Key getKey() {
        byte [] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    // Token'ın geçerli olup olmadığını kontrol edeceğiz.
    public boolean tokenControl(String token, UserDetails userDetails) {
        final String email = findUsername(token);
        return (email.equals(userDetails.getUsername()) &&
                !exportToken(token, Claims::getExpiration).before(new Date()));
                // Token'ın geçerlilik süresinin mevzut zamanımızdan önce olup olmadığını kontrol ediyoruz.
    }

    public boolean tokenIsValid(String token){
        final String email = findUsername(token);
        Optional<Account> user = repository.findByEmail(email);
        if(user.isPresent()){
            if(!exportToken(token, Claims::getExpiration).before(new Date()) &&
                    exportToken(token, Claims::getSubject).equals(user.get().getEmail()))
                return true;
        }
        return false;
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setHeaderParam("typ","JWT")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Date tokenExpirationTime(String token){
        return exportToken(token,Claims::getExpiration);
    }
    public Date tokenIssuedAt(String token){
        return exportToken(token,Claims::getIssuedAt);
    }

}
