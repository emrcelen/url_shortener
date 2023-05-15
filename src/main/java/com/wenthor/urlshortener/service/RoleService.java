package com.wenthor.urlshortener.service;

import com.wenthor.urlshortener.enums.MessageCodes;
import com.wenthor.urlshortener.exception.exceptions.IllegalArgumentException;
import com.wenthor.urlshortener.exception.exceptions.RoleNotFoundException;
import com.wenthor.urlshortener.model.Role;
import com.wenthor.urlshortener.repository.RoleRepository;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Role findByRoleName(String roleName, Locale locale){
        if(roleName != null){
            return roleRepository.findByName(roleName)
                    .orElseThrow(
                            () -> new RoleNotFoundException(String.format(MessageUtils.getMessage(locale,MessageCodes.DEVELOPER_ROLE_NOT_FOUND_MESSAGE),
                                    this.getClass().getSimpleName()),roleName,locale, MessageCodes.ROLE_NOT_FOUND_EXCEPTION)
                    );
        }
        throw new IllegalArgumentException(MessageUtils.getMessage(locale,MessageCodes.DEVELOPER_ROLE_ILLEGAL_ARGUMENT_MESSAGE),locale,MessageCodes.ROLE_ILLEGAL_ARGUMENT_EXCEPTION);
    }
}
