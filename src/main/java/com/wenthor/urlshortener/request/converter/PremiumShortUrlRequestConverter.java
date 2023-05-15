package com.wenthor.urlshortener.request.converter;

import com.wenthor.urlshortener.model.Account;
import com.wenthor.urlshortener.model.Role;
import com.wenthor.urlshortener.model.ShortUrl;
import com.wenthor.urlshortener.request.PremiumShortUrlRequest;
import com.wenthor.urlshortener.request.UserShortUrlRequest;
import com.wenthor.urlshortener.utilities.GenerateShortUrl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public final class PremiumShortUrlRequestConverter {
    public final ShortUrl convertToEntity(String url, Account account){
        ShortUrl shortUrl = new ShortUrl(url, GenerateShortUrl.code(),account);
        shortUrl.setExpirationDate(LocalDateTime.now().plusDays(LocalDateTime.now().getMonth().maxLength()));
        return shortUrl;
    }
    public final ShortUrl convertToEntity(PremiumShortUrlRequest request,Account account){
        ShortUrl shortUrl =  new ShortUrl(request.getUrl(),request.getShortUrl(),account);
        shortUrl.setExpirationDate(LocalDateTime.now().plusDays(LocalDateTime.now().getMonth().maxLength()));
        return shortUrl;
    }
    public final ShortUrl convertToEntity(ShortUrl shortUrl, String updateUrl){
        shortUrl.setShortUrl(updateUrl);
        shortUrl.setUpdatedDate(LocalDateTime.now());
        shortUrl.setExpirationDate(LocalDateTime.now().plusDays(LocalDateTime.now().getMonth().maxLength()));
        return shortUrl;
    }
}
