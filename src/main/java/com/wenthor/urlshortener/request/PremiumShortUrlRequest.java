package com.wenthor.urlshortener.request;

import org.hibernate.validator.constraints.URL;

public class PremiumShortUrlRequest {
    @URL(regexp = "^(http|https)://[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*?)?$")
    private String url;
    private String shortUrl;

    public String getUrl() {
        return url;
    }
    public String getShortUrl() {
        return shortUrl;
    }
}
