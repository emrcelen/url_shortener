package com.wenthor.urlshortener.exception.exceptions;


import com.wenthor.urlshortener.enums.MessageCode;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class IllegalArgumentException extends java.lang.IllegalArgumentException {
    private final Locale locale;
    private final MessageCode messageCode;
    private final Integer exceptionCode;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public IllegalArgumentException(String message,Locale locale, MessageCode messageCode) {
        super(MessageUtils.getMessage(locale,messageCode));
        this.locale = locale;
        this.messageCode = messageCode;
        this.exceptionCode = messageCode.getMessageCode();
        logger.error("Exception Message: {} | Developer Message: {}",MessageUtils.getMessage(locale,messageCode),message);
    }

    public IllegalArgumentException(String message,String param,Locale locale, MessageCode messageCode) {
        super(String.format(MessageUtils.getMessage(locale,messageCode), param));
        this.locale = locale;
        this.messageCode = messageCode;
        this.exceptionCode = messageCode.getMessageCode();
        logger.error("Exception Message: {} | Developer Message: {}",String.format(MessageUtils.getMessage(locale,messageCode), param),message);
    }

    public Integer getExceptionCode(){
        if(this.exceptionCode != null)
            return this.exceptionCode;
        return -9999;
    }
}
