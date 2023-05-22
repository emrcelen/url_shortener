package com.wenthor.urlshortener.service;

import com.wenthor.urlshortener.model.ShortUrl;
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
        ShortUrl url = this.shortUrlService.getByShortUrl(shortUrl,locale);
        url.setVisitor(url.getVisitor()+1);
        shortUrlService.basicSave(url);
        return new RedirectView(url.getUrl());
    }

}
