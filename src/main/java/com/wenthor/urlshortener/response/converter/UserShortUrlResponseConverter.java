package com.wenthor.urlshortener.response.converter;

import com.wenthor.urlshortener.model.ShortUrl;
import com.wenthor.urlshortener.response.UserShortUrlResponse;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class UserShortUrlResponseConverter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public UserShortUrlResponse convertToResponse(ShortUrl url){
        return new UserShortUrlResponse(
                url.getUrl(),
                url.getShortUrl(),
                formatter.format(url.getCreatedDate()),
                formatter.format(url.getExpirationDate()),
                url.getVisitor());
    }

    public List<UserShortUrlResponse> convertToResponse(List<ShortUrl> listUrl){
        return listUrl.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}
