package com.wenthor.urlshortener.service;


import com.wenthor.urlshortener.enums.MessageCodes;
import com.wenthor.urlshortener.exception.exceptions.AccountAlreadyCreatedException;
import com.wenthor.urlshortener.exception.exceptions.AccountInfoNotFoundException;
import com.wenthor.urlshortener.exception.exceptions.AccountNotFoundException;
import com.wenthor.urlshortener.exception.exceptions.IllegalArgumentException;
import com.wenthor.urlshortener.model.Account;
import com.wenthor.urlshortener.model.Role;
import com.wenthor.urlshortener.repository.AccountRepository;
import com.wenthor.urlshortener.request.AccountLoginRequest;
import com.wenthor.urlshortener.request.AccountRegisterRequest;
import com.wenthor.urlshortener.response.AccountInfoResponse;
import com.wenthor.urlshortener.response.AccountLoginTokenResponse;
import com.wenthor.urlshortener.response.AccountRegisterResponse;
import com.wenthor.urlshortener.response.converter.AccountInfoResponseConverter;
import com.wenthor.urlshortener.response.converter.AccountLoginTokenResponseConverter;
import com.wenthor.urlshortener.response.converter.AccountRegisterResponseConverter;
import com.wenthor.urlshortener.security.model.CustomUserDetail;
import com.wenthor.urlshortener.security.service.JwtService;
import com.wenthor.urlshortener.utilities.AccountUtils;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class AccountService {
    // Logger:
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // Repository:
    private final AccountRepository accountRepository;
    // Converter:
    private final AccountRegisterResponseConverter registerConverter;
    private final AccountLoginTokenResponseConverter loginTokenConverter;
    private final AccountInfoResponseConverter infoConverter;
    // Servisler:
    private final RoleService roleService;
    private final JwtService jwtService;
    // Kayıt yaparken gerekli olan yardımcılar:
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    //@Value("${security.jwt.secret}")
    private String premiumAccountKey = "premium1";
    private String adminAccountKey = "admin1";

    public AccountService(AccountRepository accountRepository, AccountRegisterResponseConverter registerConverter,
                          AccountLoginTokenResponseConverter loginTokenConverter, AccountInfoResponseConverter infoConverter, RoleService roleService,
                          JwtService jwtService, AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.registerConverter = registerConverter;
        this.loginTokenConverter = loginTokenConverter;
        this.infoConverter = infoConverter;
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    public AccountRegisterResponse register(AccountRegisterRequest request, String key, Locale locale) {
        if (request.getEmail() != null &&
                request.getPassword() != null) {
            // Her oluşturulan kullancıyı default olarak User rolünde olarak kabul ediyoruz.
            Role role = roleService.findByRoleName("ROLE_USER", locale);
            if (key != null && key.equals(adminAccountKey))
                role = roleService.findByRoleName("ROLE_ADMIN", locale);
            if (key != null && key.equals(premiumAccountKey))
                role = roleService.findByRoleName("ROLE_PREMIUM_USER", locale);
            if (!findByAccountEmail(request.getEmail()))
                throw new AccountAlreadyCreatedException(String.format(
                        MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_ACCOUNT_ALREADY_CREATED_THIS_EMAIL_MESSAGE),
                        this.getClass().getSimpleName(), request.getEmail()), request.getEmail(), locale, MessageCodes.ACCOUNT_ALREADY_CREATED_THIS_EMAIL_EXCEPTION);
            Account account = new Account(null,
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword()),
                    null,
                    null,
                    role);
            logger.info(String.format(MessageUtils.getMessage(locale, MessageCodes.LOG_ACCOUNT_SERVICE_SAVE),
                    request.getEmail(), registerConverter.findRoleName(role, locale)));
            return registerConverter.convertToResponse(accountRepository.save(account), locale);
        }
        throw new IllegalArgumentException(String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_ACCOUNT_ILLEGAL_ARGUMENT_MESSAGE),
                this.getClass().getSimpleName(), "register"), "register", locale, MessageCodes.ACCOUNT_ILLEGAL_ARGUMENT_EXCEPTION);
    }
    public AccountLoginTokenResponse login(AccountLoginRequest request, HttpHeaders headers, Locale locale) {
        if (request.getEmail() != null && request.getPassword() != null) {
            Account account = accountRepository.findByEmail(request.getEmail()).orElseThrow(
                    () -> new AccountNotFoundException(String.format(MessageUtils.getMessage
                            (locale, MessageCodes.DEVELOPER_ACCOUNT_NOT_FOUND_MESSAGE), this.getClass().getSimpleName()),
                            request.getEmail(), locale, MessageCodes.ACCOUNT_NOT_FOUND_EXCEPTION)
            );
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            CustomUserDetail userDetail = new CustomUserDetail(account);
            String token = jwtService.generateToken(userDetail);
            logger.info(String.format(MessageUtils.getMessage(locale, MessageCodes.LOG_ACCOUNT_SERVICE_LOGIN), account.getEmail()));
            return loginTokenConverter.convertToResponse(request, token
                    , jwtService.tokenIssuedAt(token)
                    , jwtService.tokenExpirationTime(token));
        }
        throw new IllegalArgumentException(String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_ACCOUNT_ILLEGAL_ARGUMENT_MESSAGE),
                this.getClass().getSimpleName(), "login"), "login", locale, MessageCodes.ACCOUNT_ILLEGAL_ARGUMENT_EXCEPTION);
    }

    public AccountInfoResponse accountInfoResponse(HttpHeaders headers, Locale locale) {
        logger.info(String.format(MessageUtils.getMessage(locale, MessageCodes.LOG_ACCOUNT_SERVICE_INFO), AccountUtils.findByAccountEmail(headers)));
        return infoConverter.convertToResponse(accountRepository.findAccountInfo(AccountUtils.findByAccountEmail(headers)), locale);
    }

    public List<AccountInfoResponse> accountInfoResponseList(String roleName, HttpHeaders headers, Locale locale) {
        if (roleName != null && !roleName.isEmpty()) {
            if (findByRoleName(roleName) != null)
                return accountInfoResponseWithRoleNameList(findByRoleName(roleName), headers, locale);
            logger.warn(String.format(MessageUtils.getMessage(locale,MessageCodes.LOG_ACCOUNT_SERVICE_INFO_WARN_LIST),AccountUtils.findByAccountEmail(headers),roleName));
            return accountInfoResponseList(headers,locale);
        }
        return accountInfoResponseList(headers,locale);
    }

    private List<AccountInfoResponse> accountInfoResponseList(HttpHeaders headers, Locale locale) {
        List<AccountInfoResponse> list = infoConverter.convertToResponse(accountRepository.findAllAccountInfo(), locale);
        if (!list.isEmpty()){
            logger.info(String.format(MessageUtils.getMessage(locale,MessageCodes.LOG_ACCOUNT_SERVICE_INFO_LIST),AccountUtils.findByAccountEmail(headers)));
            return list;
        }
        throw new AccountInfoNotFoundException(String.format(MessageUtils.getMessage
                (locale, MessageCodes.DEVELOPER_ACCOUNT_INFO_NOT_FOUND_MESSAGE), this.getClass().getSimpleName())
                , locale, MessageCodes.ACCOUNT_INFO_NOT_FOUND_EXCEPTION);
    }
    private List<AccountInfoResponse> accountInfoResponseWithRoleNameList(String roleName, HttpHeaders headers, Locale locale) {
        List<AccountInfoResponse> list = infoConverter.convertToResponse(accountRepository.findAllAccountInfoWithRole(roleName), locale);
        if (!list.isEmpty()){
            logger.info(String.format(MessageUtils.getMessage(locale,MessageCodes.LOG_ACCOUNT_SERVICE_INFO_ROLE_LIST),AccountUtils.findByAccountEmail(headers),roleName));
            return list;
        }
        throw new AccountInfoNotFoundException(String.format(MessageUtils.getMessage
                        (locale, MessageCodes.DEVELOPER_ACCOUNT_INFO_ROLE_NOT_FOUND_EXCEPTION_MESSAGE)
                , this.getClass().getSimpleName(), AccountUtils.findByAccountEmail(headers)), roleName, locale,
                MessageCodes.ACCOUNT_INFO_ROLE_NOT_FOUND_EXCEPTION);
    }
    private String findByRoleName(String role) {
        if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("admın")
                || role.equalsIgnoreCase("yönetici") || role.equalsIgnoreCase("yonetici"))
            return "ROLE_ADMIN";
        else if (role.equalsIgnoreCase("premium") || role.equalsIgnoreCase("premium-kullanıcı"))
            return "ROLE_PREMIUM_USER";
        else if (role.equalsIgnoreCase("user") || role.equalsIgnoreCase("kullanıcı"))
            return "ROLE_USER";
        else
            return null;
    }

    public int accountRemainingUrls(String email) {
        return accountRepository.findRemaingUrls(email);
    }
    public Account findByAccountEmail(String email, Locale locale) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("[%s] - findByAccountEmail method threw an exception.", this.getClass().getSimpleName()),
                        email, locale, MessageCodes.ACCOUNT_NOT_FOUND_EXCEPTION));
    }

    private Boolean findByAccountEmail(String email) {
        Account account = accountRepository.findByEmail(email).orElse(null);
        if (account != null)
            return false;
        return true;
    }
}
