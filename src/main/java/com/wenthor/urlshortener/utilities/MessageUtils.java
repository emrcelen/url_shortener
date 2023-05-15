package com.wenthor.urlshortener.utilities;

import com.wenthor.urlshortener.enums.MessageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class MessageUtils {
    private static final String RESOURCE_BUNDLE_NAME = "i18Message";
    private static final String SPECIAL_CHARACTER = "__";
    private static final Logger logger = LoggerFactory.getLogger("MessageUtils");

    public static final String getMessage(Locale locale, MessageCode messageCode){
        String messageKey = null;
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME,locale);
            messageKey = messageCode.getClass().getSimpleName() + SPECIAL_CHARACTER + messageCode;
            return resourceBundle.getString(messageKey);
        }catch (MissingResourceException missingResourceException){
            logger.error("Message not found for key: {}", messageKey);
            return null;
        }
    }
}
