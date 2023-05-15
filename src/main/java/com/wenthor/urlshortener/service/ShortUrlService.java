package com.wenthor.urlshortener.service;

import com.wenthor.urlshortener.enums.MessageCodes;
import com.wenthor.urlshortener.exception.exceptions.IllegalArgumentException;
import com.wenthor.urlshortener.exception.exceptions.ShortUrlAlreadyExistsException;
import com.wenthor.urlshortener.exception.exceptions.ShortUrlNotCreatedException;
import com.wenthor.urlshortener.exception.exceptions.ShortUrlNotFoundException;
import com.wenthor.urlshortener.model.Account;
import com.wenthor.urlshortener.model.LogShortUrl;
import com.wenthor.urlshortener.model.ShortUrl;
import com.wenthor.urlshortener.repository.ShortUrlRepository;
import com.wenthor.urlshortener.request.PremiumShortUrlRequest;
import com.wenthor.urlshortener.request.UpdateShortUrlRequest;
import com.wenthor.urlshortener.request.UserShortUrlRequest;
import com.wenthor.urlshortener.request.converter.LogShortUrlRequestConverter;
import com.wenthor.urlshortener.request.converter.PremiumShortUrlRequestConverter;
import com.wenthor.urlshortener.request.converter.UserShortUrlRequestConverter;
import com.wenthor.urlshortener.response.PremiumShortUrlResponse;
import com.wenthor.urlshortener.response.ShortUrlDeleteResponse;
import com.wenthor.urlshortener.response.UserShortUrlResponse;
import com.wenthor.urlshortener.response.converter.PremiumShortUrlResponseConverter;
import com.wenthor.urlshortener.response.converter.ShortUrlDeleteResponseConverter;
import com.wenthor.urlshortener.response.converter.UserShortUrlResponseConverter;
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
public class ShortUrlService {
    // Logger:
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // Repository:
    private final ShortUrlRepository shortUrlRepository;
    // Service:
    private final AccountService accountService;
    private final LogShortUrlService logShortUrlService;
    // Converter:
    private final UserShortUrlRequestConverter userShortUrlRequestConverter;
    private final UserShortUrlResponseConverter userShortUrlResponseConverter;
    private final PremiumShortUrlRequestConverter premiumShortUrlRequestConverter;
    private final PremiumShortUrlResponseConverter premiumShortUrlResponseConverter;
    private final ShortUrlDeleteResponseConverter shortUrlDeleteResponseConverter;
    private final LogShortUrlRequestConverter logShortUrlRequestConverter;

    public ShortUrlService(ShortUrlRepository shortUrlRepository,
                           AccountService accountService,
                           LogShortUrlService logShortUrlService, UserShortUrlRequestConverter userShortUrlRequestConverter,
                           UserShortUrlResponseConverter userShortUrlResponseConverter,
                           PremiumShortUrlRequestConverter premiumShortUrlRequestConverter,
                           PremiumShortUrlResponseConverter premiumShortUrlResponseConverter,
                           ShortUrlDeleteResponseConverter shortUrlDeleteResponseConverter,
                           LogShortUrlRequestConverter logShortUrlRequestConverter) {
        this.shortUrlRepository = shortUrlRepository;
        this.accountService = accountService;
        this.logShortUrlService = logShortUrlService;
        this.userShortUrlRequestConverter = userShortUrlRequestConverter;
        this.userShortUrlResponseConverter = userShortUrlResponseConverter;
        this.premiumShortUrlRequestConverter = premiumShortUrlRequestConverter;
        this.premiumShortUrlResponseConverter = premiumShortUrlResponseConverter;
        this.shortUrlDeleteResponseConverter = shortUrlDeleteResponseConverter;
        this.logShortUrlRequestConverter = logShortUrlRequestConverter;
    }

    public List<UserShortUrlResponse> findByUserAccountEmail(HttpHeaders headers, Locale locale) {
        List<ShortUrl> list;
        final String email = AccountUtils.findByAccountEmail(headers);
        if (email != null && !email.isEmpty()) {
            list = shortUrlRepository.findByAccountEmailAndDeletedFalseOrderByUrlIdAsc(email);
            if (!list.isEmpty()){
                logger.info(String.format(
                        MessageUtils.getMessage(locale,MessageCodes.LOG_SHORT_URL_USER_INFO_LIST),
                        AccountUtils.findByAccountEmail(headers)
                ));
                return userShortUrlResponseConverter.convertToResponse(list);
            }
            throw new ShortUrlNotFoundException(
                    String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_NOT_FOUND_MESSAGE),
                            this.getClass().getSimpleName(), "findByUserAccountEmail"), email, locale, MessageCodes.SHORT_URL_NOT_FOUND_EXCEPTION);
        }
        throw new IllegalArgumentException(String.format(
                MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_USER_SHORT_URL_ILLEGAL_ARGUMENT_MESSAGE),
                this.getClass().getSimpleName()),
                locale, MessageCodes.GENERAL_ILLEGAL_ARGUMENT_EXCEPTION);
    }

    public List<PremiumShortUrlResponse> findByPremiumAccountEmail(HttpHeaders headers, Locale locale) {
        List<ShortUrl> list;
        final String email = AccountUtils.findByAccountEmail(headers);
        if (email != null && !email.isEmpty()) {
            list = shortUrlRepository.findByAccountEmailAndDeletedFalseOrderByUrlIdAsc(email);
            if (!list.isEmpty()){
                logger.info(String.format(
                        MessageUtils.getMessage(locale,MessageCodes.LOG_SHORT_URL_PREMIUM_INFO_LIST),
                        AccountUtils.findByAccountEmail(headers)
                ));
                return premiumShortUrlResponseConverter.convertToResponse(list, locale);
            }
            throw new ShortUrlNotFoundException(
                    String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_NOT_FOUND_MESSAGE),
                            this.getClass().getSimpleName(), "findByPremiumAccountEmail"), email, locale, MessageCodes.SHORT_URL_NOT_FOUND_EXCEPTION);
        }
        throw new IllegalArgumentException(String.format(
                MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_PREMIUM_USER_SHORT_URL_ILLEGAL_ARGUMENT_MESSAGE),
                this.getClass().getSimpleName()),
                locale, MessageCodes.GENERAL_ILLEGAL_ARGUMENT_EXCEPTION);
    }

    public UserShortUrlResponse saveUserShortUrl(UserShortUrlRequest request, HttpHeaders headers, Locale locale) {
        final Account account = accountService.findByAccountEmail(AccountUtils.findByAccountEmail(headers), locale);
        if (accountService.accountRemainingUrls(account.getEmail()) > 0) {
            ShortUrl shortUrl = userShortUrlRequestConverter.convertToEntity(request, account);
            if (isShortUrlAlreadyCreated(shortUrl.getShortUrl()))
                throw new ShortUrlAlreadyExistsException(
                        String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_CREATE_ALREADY_EXISTS_MESSAGE),
                                this.getClass().getSimpleName(), account.getEmail()),
                        shortUrl.getShortUrl(), locale, MessageCodes.SHORT_URL_CREATED_ALREADY_EXISTS_EXCEPTION);
            logger.info(String.format(
                    MessageUtils.getMessage(locale,MessageCodes.LOG_SHORT_URL_USER_SAVE),
                    AccountUtils.findByAccountEmail(headers),
                    request.getUrl(),
                    shortUrl.getShortUrl()
            ));
            return this.userShortUrlResponseConverter.convertToResponse(shortUrlRepository.save(shortUrl));
        }
        throw new ShortUrlNotCreatedException(String.format(
                MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_NOT_CREATED_MESSAGE),
                this.getClass().getSimpleName(),
                account.getRole().getName(),
                account.getEmail()), locale, MessageCodes.SHORT_URL_NOT_CREATED_EXCEPTION);
    }

    public PremiumShortUrlResponse savePremiumUserShortUrl(PremiumShortUrlRequest request, HttpHeaders headers, Locale locale) {
        final Account account = accountService.findByAccountEmail(AccountUtils.findByAccountEmail(headers), locale);
        if (accountService.accountRemainingUrls(account.getEmail()) > 0) {
            if (isShortUrlAlreadyCreated(request.getShortUrl()))
                throw new ShortUrlAlreadyExistsException(
                        String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_CREATE_ALREADY_EXISTS_MESSAGE),
                                this.getClass().getSimpleName(), account.getEmail()),
                        request.getShortUrl(), locale, MessageCodes.SHORT_URL_CREATED_ALREADY_EXISTS_EXCEPTION);
            ShortUrl shortUrl = premiumShortUrlRequestConverter.convertToEntity(request, account);
            logger.info(String.format(
                    MessageUtils.getMessage(locale, MessageCodes.LOG_SHORT_URL_PREMIUM_SAVE),
                    AccountUtils.findByAccountEmail(headers),
                    shortUrl.getUrl(),
                    shortUrl.getShortUrl()
            ));
            return this.premiumShortUrlResponseConverter.convertToResponse(shortUrlRepository.save(shortUrl), locale);
        }
        throw new ShortUrlNotCreatedException(String.format(
                MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_NOT_CREATED_MESSAGE),
                this.getClass().getSimpleName(),
                account.getRole().getName(),
                account.getEmail()), locale, MessageCodes.SHORT_URL_NOT_CREATED_EXCEPTION);
    }

    public PremiumShortUrlResponse savePremiumUserAutoShortUrl(String url, Boolean protocol, HttpHeaders headers, Locale locale) {
        final Account account = accountService.findByAccountEmail(AccountUtils.findByAccountEmail(headers), locale);
        if (accountService.accountRemainingUrls(account.getEmail()) > 0) {
            ShortUrl shortUrl = premiumShortUrlRequestConverter.convertToEntity(insertUrlProtocol(protocol, url), account);
            if (isShortUrlAlreadyCreated(shortUrl.getShortUrl()))
                throw new ShortUrlAlreadyExistsException(
                        String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_CREATE_ALREADY_EXISTS_MESSAGE),
                                this.getClass().getSimpleName(), shortUrl.getAccount().getEmail()),
                        shortUrl.getShortUrl(), locale, MessageCodes.SHORT_URL_CREATED_ALREADY_EXISTS_EXCEPTION);
            else {
                logger.info(String.format(
                        MessageUtils.getMessage(locale, MessageCodes.LOG_SHORT_URL_PREMIUM_AUTO_SAVE),
                        AccountUtils.findByAccountEmail(headers),
                        shortUrl.getUrl(),
                        shortUrl.getShortUrl()
                ));
                return this.premiumShortUrlResponseConverter.convertToResponse(shortUrlRepository.save(shortUrl), locale);
            }
        }
        throw new ShortUrlNotCreatedException(String.format(
                MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_NOT_CREATED_MESSAGE),
                this.getClass().getSimpleName(),
                account.getRole().getName(),
                account.getEmail()), locale, MessageCodes.SHORT_URL_NOT_CREATED_EXCEPTION);
    }

    @Transactional
    public PremiumShortUrlResponse updateUserShortUrl(UpdateShortUrlRequest request, HttpHeaders headers, Locale locale) {
        final String email = AccountUtils.findByAccountEmail(headers);
        ShortUrl shortUrl = this.shortUrlRepository.findByAccountEmailAndShortUrlAndDeletedFalse(email, request.getPrevious_url())
                .orElseThrow(
                        () -> new ShortUrlNotFoundException(String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_NOT_FOUND_MESSAGE),
                                this.getClass().getSimpleName(), "updateUserShortUrl"), email, locale, MessageCodes.SHORT_URL_NOT_FOUND_EXCEPTION)
                );
        final String oldShortUrl = shortUrl.getShortUrl();
        if (!request.getPrevious_url().equals(request.getUpdate_url()) && isShortUrlAlreadyCreated(request.getUpdate_url()))
            throw new ShortUrlAlreadyExistsException(
                    String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_UPDATE_ALREADY_EXISTS_MESSAGE),
                            this.getClass().getSimpleName(), shortUrl.getAccount().getEmail()),
                    request.getUpdate_url(), locale, MessageCodes.SHORT_URL_UPDATE_ALREADY_EXISTS_EXCEPTION);
        shortUrl = this.premiumShortUrlRequestConverter.convertToEntity(shortUrl, request.getUpdate_url());
        LogShortUrl logShortUrl = this.logShortUrlRequestConverter.convertToEntity(shortUrl, request.getPrevious_url());
        logShortUrlService.basicSave(logShortUrl);
        shortUrlRepository.save(shortUrl);
        logger.info(String.format(
                MessageUtils.getMessage(locale, MessageCodes.LOG_SHORT_URL_UPDATE),
                AccountUtils.findByAccountEmail(headers),
                oldShortUrl,
                shortUrl.getShortUrl()
        ));
        return premiumShortUrlResponseConverter.convertToResponse(shortUrl, locale);
    }

    @Transactional
    public ShortUrlDeleteResponse deleteUserShortUrl(String short_url, HttpHeaders headers, Locale locale) {
        ShortUrl shortUrl = this.shortUrlRepository.findByAccountEmailAndShortUrlAndDeletedFalse
                (AccountUtils.findByAccountEmail(headers), short_url).orElseThrow(
                () -> new ShortUrlNotFoundException(
                        String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_SHORT_URL_DELETE_NOT_FOUND_MESSAGE),
                                this.getClass().getSimpleName(), short_url, AccountUtils.findByAccountEmail(headers)), short_url,
                        locale, MessageCodes.SHORT_URL_DELETE_NOT_FOUND_EXCEPTION));
        shortUrl.setDeleted(true);
        shortUrl.setUpdatedDate(LocalDateTime.now());
        this.shortUrlRepository.save(shortUrl);
        this.logShortUrlService.basicSave(this.logShortUrlRequestConverter.convertToEntity(shortUrl));
        logger.info(String.format(
                MessageUtils.getMessage(locale, MessageCodes.LOG_SHORT_URL_DELETE),
                AccountUtils.findByAccountEmail(headers),
                short_url
        ));
        return this.shortUrlDeleteResponseConverter.convertToResponse(shortUrl, accountService.accountRemainingUrls(AccountUtils.findByAccountEmail(headers)));
    }


    public String getByShortUrl(String short_url, Locale locale){
        ShortUrl shortUrl = this.shortUrlRepository.findByShortUrlAndDeletedFalse(short_url).
                orElseThrow(() -> new ShortUrlNotFoundException(String.format(
                        MessageUtils.getMessage(locale,MessageCodes.DEVELOPER_SHORT_URL_RE_DIRECT_NOT_FOUND_MESSAGE),
                        this.getClass().getSimpleName(), short_url),short_url,locale,MessageCodes.SHORT_URL_DELETE_NOT_FOUND_EXCEPTION));
        if(shortUrl.getExpirationDate().isAfter(LocalDateTime.now())){
            return shortUrl.getUrl();
        }
        shortUrl.setDeleted(true);
        shortUrl.setUpdatedDate(LocalDateTime.now());
        this.shortUrlRepository.save(shortUrl);
        throw new ShortUrlNotFoundException(String.format(
                MessageUtils.getMessage(locale,MessageCodes.DEVELOPER_SHORT_URL_EXPIRATION_DATE_MESSAGE),
                this.getClass().getSimpleName(),
                short_url
        ),short_url,locale,MessageCodes.SHORT_URL_EXPIRATION_DATE_EXCEPTION);
    }
    private boolean isShortUrlAlreadyCreated(String url) {
        return shortUrlRepository.existsByShortUrlAndDeletedFalse(url);
    }
    private String insertUrlProtocol(Boolean protocol,String url){
        if(protocol != null && protocol)
            return "https://"+url;
        return "http://"+url;
    }

}
