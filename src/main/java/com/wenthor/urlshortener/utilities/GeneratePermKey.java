package com.wenthor.urlshortener.utilities;

import java.util.UUID;

public final class GeneratePermKey {

    public static final String premium(){
        final String key = "PRE-"+UUID.randomUUID().toString().substring(0,6)+"-"+UUID.randomUUID().toString().substring(6,14)+UUID.randomUUID().toString().substring(0,6);
        return key;
    }
    public static final String admin(){
        final String key = "ADM-"+UUID.randomUUID().toString().substring(0,6)+"-"+UUID.randomUUID().toString().substring(6,14)+UUID.randomUUID().toString().substring(0,6);
        return key;
    }
}
