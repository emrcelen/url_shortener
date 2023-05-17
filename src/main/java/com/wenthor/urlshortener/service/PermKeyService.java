package com.wenthor.urlshortener.service;

import com.wenthor.urlshortener.enums.MessageCodes;
import com.wenthor.urlshortener.exception.exceptions.PermKeyNotFoundException;
import com.wenthor.urlshortener.model.Account;
import com.wenthor.urlshortener.model.PermKey;
import com.wenthor.urlshortener.model.Role;
import com.wenthor.urlshortener.repository.PermKeyRepository;
import com.wenthor.urlshortener.request.converter.PermKeyRequestConverter;
import com.wenthor.urlshortener.response.PermKeyInfoResponse;
import com.wenthor.urlshortener.response.PermKeyResponse;
import com.wenthor.urlshortener.response.converter.PermKeyResponseConverter;
import com.wenthor.urlshortener.utilities.AccountUtils;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class PermKeyService {
    // Logger:
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // Service:
    private final RoleService roleService;
    // Repository:
    private final PermKeyRepository permKeyRepository;
    // Converter:
    private final PermKeyResponseConverter permKeyResponseConverter;
    private final PermKeyRequestConverter permKeyRequestConverter;


    public PermKeyService(RoleService roleService, PermKeyRepository permKeyRepository, PermKeyResponseConverter permKeyResponseConverter, PermKeyRequestConverter permKeyRequestConverter) {
        this.roleService = roleService;
        this.permKeyRepository = permKeyRepository;
        this.permKeyResponseConverter = permKeyResponseConverter;
        this.permKeyRequestConverter = permKeyRequestConverter;
    }

    @Transactional
    public PermKeyResponse save(String roleName,
                                HttpHeaders headers,
                                Locale locale) {
        Role role = this.roleService.findByRoleName(findByRoleName(roleName), locale);
        PermKey key = this.permKeyRequestConverter.convertToEntity(role);
        logger.info(String.format(
                MessageUtils.getMessage(locale,MessageCodes.LOG_PERM_KEY_SAVE),
                AccountUtils.findByAccountEmail(headers),roleName
        ));
        return this.permKeyResponseConverter.convertToResponse(this.permKeyRepository.save(key),locale);
    }
    public boolean updatePermKey(String key, Account account){
        PermKey permKey = this.permKeyRepository.findByKeyAndActivatedFalse(key).orElse(null);
        if(permKey == null)
            return false;
        permKey.setAccount(account);
        permKey.setActivationDate(LocalDateTime.now());
        permKey.setActivated(true);
        this.permKeyRepository.save(permKey);
        return true;
    }


    public List<?> getPermKeys(String param,
                               String roleName,
                               HttpHeaders headers,
                               Locale locale){
        if(param != null && param.equalsIgnoreCase("activated-false") && roleName != null)
            return this.getActivatedFalseList(roleName,headers,locale);
        else if(param != null && param.equalsIgnoreCase("activated-false") && roleName == null)
            return this.getActivatedFalseList(headers,locale);
        else if(param != null && param.equalsIgnoreCase("activated") && roleName != null)
            return this.getActivatedTrueList(roleName, headers,locale);
        else if(param != null && param.equalsIgnoreCase("activated") && roleName == null)
            return this.getActivatedTrueList(headers,locale);
        else if(param == null && roleName != null)
            return this.getPermKeyList(roleName,headers,locale);
        return this.getPermKeyList(headers,locale);
    }

    private List <PermKeyInfoResponse> getPermKeyList (HttpHeaders headers,
                                                       Locale locale){
        List<PermKey> keys = this.permKeyRepository.findAll();
        if(keys.isEmpty())
            throw new PermKeyNotFoundException(String.format(
                    MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_PERM_KEY_NOT_FOUND_MESSAGE),
                    this.getClass().getSimpleName()+" - getPermKeyList", AccountUtils.findByAccountEmail(headers)
            ),locale,MessageCodes.PERM_KEY_NOT_FOUND_EXCEPTION);
        logger.info(String.format(
                MessageUtils.getMessage(locale,MessageCodes.LOG_PERM_KEY_LIST_INFO),
                AccountUtils.findByAccountEmail(headers)
        ));
        return this.permKeyResponseConverter.convertToInfoResponses(keys,locale);
    }
    private List <PermKeyResponse> getActivatedFalseList(HttpHeaders headers,
                                                         Locale locale){
        List<PermKey> keys = this.permKeyRepository.findByActivatedFalse();
        if(keys.isEmpty())
            throw new PermKeyNotFoundException(String.format(
                MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_PERM_KEY_NOT_FOUND_MESSAGE),
                this.getClass().getSimpleName()+" - getActivatedFalseList", AccountUtils.findByAccountEmail(headers)
        ),locale,MessageCodes.PERM_KEY_NOT_FOUND_EXCEPTION);
        logger.info(String.format(
                MessageUtils.getMessage(locale,MessageCodes.LOG_PERM_KEY_DEACTIVATED_LIST_INFO),
                AccountUtils.findByAccountEmail(headers)
        ));
        return this.permKeyResponseConverter.convertToResponses(keys,locale);
    }
    private List <PermKeyResponse> getActivatedFalseList(String roleName,
                                                         HttpHeaders headers,
                                                         Locale locale){
        List<PermKey> keys = this.permKeyRepository.findByRoleNameAndActivatedFalse(findByRoleName(roleName));
        if(keys.isEmpty())
            throw new PermKeyNotFoundException(String.format(
                    MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_PERM_KEY_ROLE_NOT_FOUND_MESSAGE),
                    this.getClass().getSimpleName()+" - getActivatedFalseList(Role)",
                    findByRoleName(roleName),
                    AccountUtils.findByAccountEmail(headers)
            ),roleName,locale,MessageCodes.PERM_KEY_ROLE_NOT_FOUND_EXCEPTION);
        logger.info(String.format(
                MessageUtils.getMessage(locale,MessageCodes.LOG_PERM_KEY_DEACTIVATED_ROLE_LIST_INFO),
                AccountUtils.findByAccountEmail(headers),
                roleName
        ));
        return this.permKeyResponseConverter.convertToResponses(keys,locale);
    }
    private List <PermKeyInfoResponse>getActivatedTrueList(HttpHeaders headers,
                                                           Locale locale){
        List<PermKey> keys = this.permKeyRepository.findByActivatedTrue();
        if(keys.isEmpty())
            throw new PermKeyNotFoundException(String.format(
                    MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_PERM_KEY_NOT_FOUND_MESSAGE),
                    this.getClass().getSimpleName()+" - getActivatedTrueList", AccountUtils.findByAccountEmail(headers)
            ),locale,MessageCodes.PERM_KEY_NOT_FOUND_EXCEPTION);
        logger.info(String.format(
                MessageUtils.getMessage(locale,MessageCodes.LOG_PERM_KEY_ACTIVATED_LIST_INFO),
                AccountUtils.findByAccountEmail(headers)
        ));
        return this.permKeyResponseConverter.convertToInfoResponses(keys,locale);
    }
    private List <PermKeyInfoResponse>getActivatedTrueList(String roleName,
                                                           HttpHeaders headers,
                                                           Locale locale){
        List<PermKey> keys = this.permKeyRepository.findByRoleNameAndActivatedTrue(findByRoleName(roleName));
        if(keys.isEmpty())
            throw new PermKeyNotFoundException(String.format(
                    MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_PERM_KEY_ROLE_NOT_FOUND_MESSAGE),
                    this.getClass().getSimpleName()+" - getActivatedTrueList(Role)",
                    findByRoleName(roleName),
                    AccountUtils.findByAccountEmail(headers)
            ),roleName,locale,MessageCodes.PERM_KEY_ROLE_NOT_FOUND_EXCEPTION);
        logger.info(String.format(
                MessageUtils.getMessage(locale,MessageCodes.LOG_PERM_KEY_ACTIVATED_ROLE_LIST_INFO),
                AccountUtils.findByAccountEmail(headers),
                roleName
        ));
        return this.permKeyResponseConverter.convertToInfoResponses(keys,locale);
    }
    private List <PermKeyInfoResponse> getPermKeyList(String roleName,
                                                      HttpHeaders headers,
                                                      Locale locale){
        List<PermKey> keys = this.permKeyRepository.findByRoleName(findByRoleName(roleName));
        if(keys.isEmpty())
            throw new PermKeyNotFoundException(String.format(
                    MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_PERM_KEY_ROLE_NOT_FOUND_MESSAGE),
                    this.getClass().getSimpleName()+" - getPermKeyList(Role)",
                    findByRoleName(roleName),
                    AccountUtils.findByAccountEmail(headers)
            ),roleName,locale,MessageCodes.PERM_KEY_ROLE_NOT_FOUND_EXCEPTION);
        logger.info(String.format(
                MessageUtils.getMessage(locale,MessageCodes.LOG_PERM_KEY_ROLE_LIST_INFO),
                AccountUtils.findByAccountEmail(headers),roleName
        ));
        return this.permKeyResponseConverter.convertToInfoResponses(keys,locale);
    }

    public PermKey getPermKey(String key, String email, Locale locale){
        PermKey permKey = this.permKeyRepository.findByKeyAndActivatedFalse(key).orElseThrow(
                () -> new PermKeyNotFoundException(String.format(
                        MessageUtils.getMessage(locale,MessageCodes.DEVELOPER_PERM_KEY_REGISTER_NOT_FOUND_MESSAGE),
                        this.getClass().getSimpleName(), key, email), locale,MessageCodes.PERM_KEY_NOT_FOUND_EXCEPTION)
        );
        if(permKey.getExpirationDate().isAfter(LocalDateTime.now()))
            return permKey;
        this.permKeyRepository.delete(permKey);
        throw new PermKeyNotFoundException(String.format(
                MessageUtils.getMessage(locale,MessageCodes.DEVELOPER_PERM_KEY_EXPIRATION_DATE_MESSAGE),
                this.getClass().getSimpleName(),
                key, email),key,locale,MessageCodes.PERM_KEY_EXPIRATION_DATE_EXCEPTION);
    }
    private String findByRoleName(String roleName) {
        return (roleName.equalsIgnoreCase("Yönetici") || roleName.equalsIgnoreCase("Admin")) ? "ROLE_ADMIN" :
                (roleName.equalsIgnoreCase("Premium")) ? "ROLE_PREMIUM_USER" : roleName ;
    }
}
