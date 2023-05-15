package com.wenthor.urlshortener.controller;

import com.wenthor.urlshortener.service.RedirectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

@RestController
public class RedirectController {
    private final RedirectService redirectService;

    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping(path = "{url}")
    public RedirectView redirect(@PathVariable(name = "url") String url,
                                     Locale locale){
        return this.redirectService.redirect(url,locale);
    }
}
