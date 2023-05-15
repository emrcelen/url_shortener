package com.wenthor.urlshortener.request.converter;

import com.wenthor.urlshortener.model.Account;
import com.wenthor.urlshortener.model.ShortUrl;
import com.wenthor.urlshortener.request.UserShortUrlRequest;
import com.wenthor.urlshortener.utilities.GenerateShortUrl;
import org.springframework.stereotype.Component;

@Component
public final class UserShortUrlRequestConverter {

    public final ShortUrl convertToEntity(UserShortUrlRequest request, Account account){
        return new ShortUrl(request.getUrl(), GenerateShortUrl.code(),account);
    }

}
