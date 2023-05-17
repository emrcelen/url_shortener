package com.wenthor.urlshortener.response.converter;

import com.wenthor.urlshortener.enums.MessageCodes;
import com.wenthor.urlshortener.model.PermKey;
import com.wenthor.urlshortener.response.PermKeyInfoResponse;
import com.wenthor.urlshortener.response.PermKeyResponse;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public final class PermKeyResponseConverter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public final PermKeyResponse convertToResponse (PermKey key, Locale locale){
        return new PermKeyResponse(
                key.getKey(),
                findByRoleName(key,locale),
                formatter.format(key.getCreatedDate()),
                formatter.format(key.getExpirationDate())
        );
    }
    public final List<PermKeyResponse> convertToResponses(List<PermKey> permKeys, Locale locale){
        return permKeys.stream()
                .map(key -> this.convertToResponse(key,locale))
                .collect(Collectors.toList());
    }

    public final PermKeyInfoResponse convertToInfoResponse(PermKey key, Locale locale){
        final String activated =  (key.getAccount() == null) ? MessageUtils.getMessage(locale, MessageCodes.NOT_ACTIVATED) :
                MessageUtils.getMessage(locale, MessageCodes.ACTIVATED);
        final String accountName = (activated.equalsIgnoreCase(MessageUtils.getMessage(locale,MessageCodes.ACTIVATED))) ? key.getAccount().getEmail() :
                "null";
        return new PermKeyInfoResponse(
                key.getKey(),
                findByRoleName(key,locale),
                formatter.format(key.getCreatedDate()),
                formatter.format(key.getExpirationDate()),
                activated,
                accountName
        );
    }

    public final List<PermKeyInfoResponse> convertToInfoResponses(List<PermKey> permKeys, Locale locale){
        return permKeys.stream()
                .map(key -> this.convertToInfoResponse(key,locale))
                .collect(Collectors.toList());
    }

    private String findByRoleName(PermKey key, Locale locale){
        String roleName = (key.getRole().getName().equalsIgnoreCase("ROLE_PREMIUM_USER")) ? MessageUtils.getMessage(locale,MessageCodes.PREMIUM_USER) :
                MessageUtils.getMessage(locale,MessageCodes.ADMIN);
        return roleName;
    }
}
