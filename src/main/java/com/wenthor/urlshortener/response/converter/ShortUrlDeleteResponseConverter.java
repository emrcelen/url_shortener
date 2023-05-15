package com.wenthor.urlshortener.response.converter;

import com.wenthor.urlshortener.model.ShortUrl;
import com.wenthor.urlshortener.response.ShortUrlDeleteResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public final class ShortUrlDeleteResponseConverter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public final ShortUrlDeleteResponse convertToResponse (ShortUrl shortUrl, int remaining_urls){
        return new ShortUrlDeleteResponse(
                shortUrl.getAccount().getEmail(),
                remaining_urls,
                shortUrl.getUrl(),
                shortUrl.getShortUrl(),
                formatter.format(shortUrl.getCreatedDate()),
                formatter.format(LocalDateTime.now()));
    }

}
