package com.wenthor.urlshortener.request;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;

public class UserShortUrlRequest {
    @URL(regexp = "^(http|https)://[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*?)?$")
    private String url;

    public String getUrl() {
        return url;
    }
}
