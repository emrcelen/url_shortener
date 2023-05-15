package com.wenthor.urlshortener.exception.exceptions;

import com.wenthor.urlshortener.enums.MessageCode;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class AccountAlreadyCreatedException extends RuntimeException {
    private final Locale locale;
    private final MessageCode messageCode;
    private final Integer exceptionCode;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AccountAlreadyCreatedException(String message, String username, Locale locale, MessageCode messageCode) {
        super(String.format(MessageUtils.getMessage(locale,messageCode),username));
        this.locale = locale;
        this.messageCode = messageCode;
        this.exceptionCode = messageCode.getMessageCode();
        logger.error("Exception Message: {} | Developer Message: {}",String.format(MessageUtils.getMessage(locale,messageCode),username),message);
    }

    public Integer getExceptionCode(){
        if(this.exceptionCode != null)
            return this.exceptionCode;
        return -9999;
    }
}
