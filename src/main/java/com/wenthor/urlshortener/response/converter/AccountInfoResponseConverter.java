package com.wenthor.urlshortener.response.converter;

import com.wenthor.urlshortener.enums.MessageCodes;
import com.wenthor.urlshortener.model.Role;
import com.wenthor.urlshortener.response.AccountInfoResponse;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Component;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Component
public final class AccountInfoResponseConverter  {
    public final AccountInfoResponse convertToResponse(Tuple tuple, Locale locale){
        return new AccountInfoResponse(tuple.get(0, String.class),findRoleName(tuple.get(1, String.class),locale), tuple.get(2, BigInteger.class));
    }
    public List<AccountInfoResponse> convertToResponse(List<Tuple> list, Locale locale){
        return list.stream()
                .map(v -> convertToResponse(v,locale))
                .collect(Collectors.toList());
    }
    private final String findRoleName(String role, Locale locale) {
        String response = MessageUtils.getMessage(locale, MessageCodes.USER);
        if (role.toLowerCase().contains("admın"))
            response = MessageUtils.getMessage(locale, MessageCodes.ADMIN);
        else if (role.toLowerCase().contains("premıum"))
            response = MessageUtils.getMessage(locale, MessageCodes.PREMIUM_USER);
        return response;
    }
}
