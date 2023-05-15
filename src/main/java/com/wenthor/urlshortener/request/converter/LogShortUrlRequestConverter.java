package com.wenthor.urlshortener.request.converter;

import com.wenthor.urlshortener.model.LogShortUrl;
import com.wenthor.urlshortener.model.ShortUrl;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public final class LogShortUrlRequestConverter {
    public final LogShortUrl convertToEntity(ShortUrl shortUrl){
        return new LogShortUrl(
                null,
                shortUrl.getAccount(),
                shortUrl,
                shortUrl.getShortUrl(),
                shortUrl.getShortUrl(),
                shortUrl.getUpdatedDate());
    }
    public final LogShortUrl convertToEntity(ShortUrl shortUrl, String previous_url){
        return new LogShortUrl(
                null,
                shortUrl.getAccount(),
                shortUrl,
                previous_url,
                shortUrl.getShortUrl(),
                LocalDateTime.now());
    }
}
