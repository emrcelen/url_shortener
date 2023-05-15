package com.wenthor.urlshortener.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

@Service
public class RedirectService {
    private final ShortUrlService shortUrlService;

    public RedirectService(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    public RedirectView redirect(String shortUrl, Locale locale){
        return new RedirectView(this.shortUrlService.getByShortUrl(shortUrl,locale));
    }

}
