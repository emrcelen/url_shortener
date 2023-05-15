package com.wenthor.urlshortener.utilities;

import java.util.UUID;

public final class GenerateShortUrl {
    public static final String code(){
        final UUID random = UUID.randomUUID();
        final String code = random.toString().substring(0,7);
        return code;
    }
}
