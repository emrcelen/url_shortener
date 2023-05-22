package com.wenthor.urlshortener.response.converter;

import com.wenthor.urlshortener.enums.MessageCodes;
import com.wenthor.urlshortener.model.ShortUrl;
import com.wenthor.urlshortener.response.PremiumShortUrlResponse;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public final class PremiumShortUrlResponseConverter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public PremiumShortUrlResponse convertToResponse (ShortUrl url, Locale locale){
        final String updateDate =  (url.getUpdatedDate()==null) ? MessageUtils.getMessage(locale, MessageCodes.NOT_UPDATED) :
                formatter.format(url.getUpdatedDate());
        return new PremiumShortUrlResponse(
                url.getUrl(),
                url.getShortUrl(),
                formatter.format(url.getCreatedDate()),
                updateDate,
                formatter.format(url.getExpirationDate()),
                url.getVisitor());
    }

    public List<PremiumShortUrlResponse> convertToResponse (List<ShortUrl> listUrl, Locale locale){
        return listUrl.stream()
                .map(a -> convertToResponse(a,locale))
                .collect(Collectors.toList());
    }
}
