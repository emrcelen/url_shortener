package com.wenthor.urlshortener.request.converter;

import com.wenthor.urlshortener.model.PermKey;
import com.wenthor.urlshortener.model.Role;
import com.wenthor.urlshortener.utilities.GeneratePermKey;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public final class PermKeyRequestConverter {

    public final PermKey convertToEntity(Role role) {
        return new PermKey(getKey(role), role);
    }

    private final String getKey(Role role) {
        final String key = (role.getName().equalsIgnoreCase("ROLE_ADMIN")) ?
                GeneratePermKey.admin() : GeneratePermKey.premium();
        return key;
    }
}
