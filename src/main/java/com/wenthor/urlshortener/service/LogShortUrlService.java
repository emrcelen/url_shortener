package com.wenthor.urlshortener.service;

import com.wenthor.urlshortener.enums.MessageCodes;
import com.wenthor.urlshortener.exception.exceptions.IllegalArgumentException;
import com.wenthor.urlshortener.exception.exceptions.LogShortUrlNotFoundException;
import com.wenthor.urlshortener.model.LogShortUrl;
import com.wenthor.urlshortener.repository.LogShortUrlRepository;
import com.wenthor.urlshortener.response.LogShortUrlInfoResponse;
import com.wenthor.urlshortener.response.LogShortUrlUserInfoResponse;
import com.wenthor.urlshortener.response.converter.LogShortUrlResponseConverter;
import com.wenthor.urlshortener.utilities.AccountUtils;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class LogShortUrlService {
    // Logger:
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // Repository:
    private final LogShortUrlRepository logShortUrlRepository;
    // Converter:
    private final LogShortUrlResponseConverter logShortUrlResponseConverter;

    public LogShortUrlService(LogShortUrlRepository logShortUrlRepository, LogShortUrlResponseConverter logShortUrlResponseConverter) {
        this.logShortUrlRepository = logShortUrlRepository;
        this.logShortUrlResponseConverter = logShortUrlResponseConverter;
    }

    public final void basicSave(LogShortUrl logShortUrl){
        this.logShortUrlRepository.save(logShortUrl);
    }
    //Premium User:
    public final List<LogShortUrlUserInfoResponse> getLogShortUrl(String shortUrl, HttpHeaders headers, Locale locale){
        if(shortUrl != null && !shortUrl.isEmpty())
            return this.getByLogShortUrlUserInfo(shortUrl,headers,locale);
        return this.getByLogShortUrlUserInfo(headers,locale);
    }
    private List<LogShortUrlUserInfoResponse> getByLogShortUrlUserInfo(HttpHeaders headers, Locale locale){
        List<LogShortUrlUserInfoResponse>  list = this.logShortUrlResponseConverter.convertToUserLog(
                this.logShortUrlRepository.findByAccountEmailAndShortUrlDeletedFalseOrderByUpdatedDateDesc(
                        AccountUtils.findByAccountEmail(headers)));
        if(!list.isEmpty()){
            logger.info(String.format(MessageUtils.getMessage(locale,MessageCodes.LOG_SHORT_URL_LOG_USER_ALL_INFO),
                    AccountUtils.findByAccountEmail(headers)));
            return list;
        }
        throw new LogShortUrlNotFoundException(
                String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_LOG_URL_NOT_FOUND_MESSAGE),
                        this.getClass().getSimpleName(),
                        AccountUtils.findByAccountEmail(headers)),locale,MessageCodes.LOG_NOT_FOUND_EXCEPTION);
    }
    private List<LogShortUrlUserInfoResponse> getByLogShortUrlUserInfo(String shortUrl, HttpHeaders headers, Locale locale){
       List<LogShortUrlUserInfoResponse>  list = this.logShortUrlResponseConverter.convertToUserLog(
               this.logShortUrlRepository.findByAccountEmailAndShortUrlShortUrlAndShortUrlDeletedFalseOrderByUpdatedDateDesc(
                       AccountUtils.findByAccountEmail(headers),shortUrl));
       if(!list.isEmpty()){
           logger.info(String.format(MessageUtils.getMessage(locale,MessageCodes.LOG_SHORT_URL_LOG_USER_SEARCH_INFO),
                   AccountUtils.findByAccountEmail(headers),shortUrl));
           return list;
       }
       throw new LogShortUrlNotFoundException(
               String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_LOG_SHORT_URL_NOT_FOUND_MESSAGE),
                       this.getClass().getSimpleName(),
                       AccountUtils.findByAccountEmail(headers),
                       shortUrl),shortUrl,locale,MessageCodes.LOG_SHORT_URL_NOT_FOUND_EXCEPTION);
    }
    // Admin:
    public final List<LogShortUrlInfoResponse> getLogShortUrlAdmin(String shortUrl,
                                                                   String accountName,
                                                                   HttpHeaders headers,
                                                                   Locale locale){
        if(shortUrl != null && !shortUrl.isEmpty() && accountName == null)
            return this.getByLogShortUrlInfo(shortUrl,headers,locale);
        else if(accountName != null && !accountName.isEmpty() && shortUrl == null)
            return this.getByLogShortUrlAccountName(accountName,headers,locale);
        else if(accountName == null && shortUrl== null)
            return this.getLogShortUrlAdmin(headers,locale);
        else
            throw new IllegalArgumentException(String.format(
                    MessageUtils.getMessage(locale,MessageCodes.DEVELOPER_LOG_SHORT_URL_ILLEGAL_ARGUMENT_MESSAGE),
                    this.getClass().getSimpleName(),
                    AccountUtils.findByAccountEmail(headers)),locale,MessageCodes.GENERAL_ILLEGAL_ARGUMENT_EXCEPTION);
    }
    private List<LogShortUrlInfoResponse> getLogShortUrlAdmin(HttpHeaders headers, Locale locale){
        List<LogShortUrlInfoResponse> list = this.logShortUrlResponseConverter.convertToLog(
                this.logShortUrlRepository.findByShortUrlDeletedFalseOrderByUpdatedDateDesc());
        if(!list.isEmpty()){
            logger.info(String.format(
                    MessageUtils.getMessage(locale,MessageCodes.LOG_SHORT_URL_LOG_ADMIN_ALL_INFO),
                    AccountUtils.findByAccountEmail(headers)));
            return list;
        }
        throw new LogShortUrlNotFoundException(String.format(MessageUtils.getMessage(locale,MessageCodes.DEVELOPER_LOG_URL_NOT_FOUND_ADMIN_ALL_MESSAGE),
                this.getClass().getSimpleName(),AccountUtils.findByAccountEmail(headers)),locale,MessageCodes.LOG_NOT_FOUND_ADMIN_ALL_EXCEPTION);
    }
    private List<LogShortUrlInfoResponse> getByLogShortUrlInfo(String shortUrl,HttpHeaders headers,Locale locale){
        List<LogShortUrlInfoResponse> list = this.logShortUrlResponseConverter.convertToLog(
                this.logShortUrlRepository.findByShortUrlShortUrlAndShortUrlDeletedFalseOrderByUpdatedDateDesc(shortUrl));
        if(!list.isEmpty()){
            logger.info(String.format(
                    MessageUtils.getMessage(locale,MessageCodes.LOG_SHORT_URL_LOG_ADMIN_URL_SEARCH_INFO),
                    AccountUtils.findByAccountEmail(headers),
                    shortUrl));
            return list;
        }
        throw new LogShortUrlNotFoundException(
                String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_LOG_SHORT_URL_NOT_FOUND_MESSAGE),
                        this.getClass().getSimpleName(), AccountUtils.findByAccountEmail(headers),shortUrl)
                ,shortUrl,locale,MessageCodes.LOG_SHORT_URL_NOT_FOUND_EXCEPTION);
    }
    private List<LogShortUrlInfoResponse> getByLogShortUrlAccountName(String accountName,HttpHeaders headers, Locale locale){
        List<LogShortUrlInfoResponse> list = this.logShortUrlResponseConverter.convertToLog(
                this.logShortUrlRepository.findByAccountEmailAndShortUrlDeletedFalseOrderByUpdatedDateDesc(accountName));
        if(!list.isEmpty()){
            logger.info(String.format(
                    MessageUtils.getMessage(locale,MessageCodes.LOG_SHORT_URL_LOG_ADMIN_USER_SEARCH_INFO),
                    AccountUtils.findByAccountEmail(headers), accountName));
            return list;
        }
        throw new LogShortUrlNotFoundException(
                String.format(MessageUtils.getMessage(locale, MessageCodes.DEVELOPER_LOG_URL_NOT_FOUND_ADMIN_MESSAGE),
                        this.getClass().getSimpleName(), accountName),accountName,locale,MessageCodes.LOG_NOT_FOUND_ADMIN_EXCEPTION);
    }

}
