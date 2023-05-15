package com.wenthor.urlshortener.response.converter;

import com.wenthor.urlshortener.enums.MessageCodes;
import com.wenthor.urlshortener.model.Account;
import com.wenthor.urlshortener.model.Role;
import com.wenthor.urlshortener.response.AccountRegisterResponse;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public final class AccountRegisterResponseConverter {

    public final AccountRegisterResponse convertToResponse(Account account, Locale locale) {
        return new AccountRegisterResponse(account.getEmail(), findRoleName(account.getRole(), locale),account.getRole().getMaxUrls());
    }

    public final String findRoleName(Role role, Locale locale) {
            String response = MessageUtils.getMessage(locale, MessageCodes.USER);
            if (role.getName().toLowerCase().contains("admın"))
                response = MessageUtils.getMessage(locale, MessageCodes.ADMIN);
            else if (role.getName().toLowerCase().contains("premıum"))
                response = MessageUtils.getMessage(locale, MessageCodes.PREMIUM_USER);
        return response;
    }
}
