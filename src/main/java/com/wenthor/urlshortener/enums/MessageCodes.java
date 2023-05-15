package com.wenthor.urlshortener.enums;

public enum MessageCodes implements MessageCode{
    // Response:
    USER(0001),
    PREMIUM_USER(0002),
    ADMIN(0003),
    NOT_UPDATED(0010),
    ACCESS_IS_DENIED(-9999),
    // Logger Message:
    LOG_ACCOUNT_SERVICE_SAVE(0100),
    LOG_ACCOUNT_SERVICE_LOGIN(0101),
    LOG_ACCOUNT_SERVICE_INFO(0102),
    LOG_ACCOUNT_SERVICE_INFO_LIST(0103),
    LOG_ACCOUNT_SERVICE_INFO_WARN_LIST(0104),
    LOG_ACCOUNT_SERVICE_INFO_ROLE_LIST(0105),
    LOG_SHORT_URL_USER_INFO_LIST(0200),
    LOG_SHORT_URL_PREMIUM_INFO_LIST(0201),
    LOG_SHORT_URL_USER_SAVE(0203),
    LOG_SHORT_URL_PREMIUM_SAVE(0204),
    LOG_SHORT_URL_PREMIUM_AUTO_SAVE(0205),
    LOG_SHORT_URL_UPDATE(0206),
    LOG_SHORT_URL_DELETE(0207),
    LOG_SHORT_URL_LOG_USER_ALL_INFO(0300),
    LOG_SHORT_URL_LOG_USER_SEARCH_INFO(0301),
    LOG_SHORT_URL_LOG_ADMIN_ALL_INFO(0302),
    LOG_SHORT_URL_LOG_ADMIN_USER_SEARCH_INFO(0303),
    LOG_SHORT_URL_LOG_ADMIN_URL_SEARCH_INFO(0304),
    // Exception:
    GENERAL_ILLEGAL_ARGUMENT_EXCEPTION(-3333),
    BAD_CREDENTIAL_EXCEPTION(-4444),
    METHOD_NOT_VALID_EXCEPTION(-5555),
    EMAIL_NOT_VALID_EXCEPTION(-5556),
    ACCOUNT_NOT_FOUND_EXCEPTION(-1000),
    ACCOUNT_ILLEGAL_ARGUMENT_EXCEPTION(-1001),
    ACCOUNT_ALREADY_CREATED_THIS_EMAIL_EXCEPTION(-1010),
    ACCOUNT_INFO_NOT_FOUND_EXCEPTION(-1020),
    ACCOUNT_INFO_ROLE_NOT_FOUND_EXCEPTION(-1030),
    ROLE_NOT_FOUND_EXCEPTION(-1100),
    ROLE_ILLEGAL_ARGUMENT_EXCEPTION(-1101),
    SHORT_URL_NOT_FOUND_EXCEPTION(-1500),
    SHORT_URL_EXPIRATION_DATE_EXCEPTION(-1501),
    SHORT_URL_DELETE_NOT_FOUND_EXCEPTION(-1510),
    SHORT_URL_NOT_CREATED_EXCEPTION(-1555),
    SHORT_URL_CREATED_ALREADY_EXISTS_EXCEPTION(-1559),
    SHORT_URL_UPDATE_ALREADY_EXISTS_EXCEPTION(-1599),
    LOG_NOT_FOUND_EXCEPTION(-1600),
    LOG_NOT_FOUND_ADMIN_EXCEPTION(-1601),
    LOG_NOT_FOUND_ADMIN_ALL_EXCEPTION(-1602),
    LOG_SHORT_URL_NOT_FOUND_EXCEPTION(-1610),
    // Developer Message:
    DEVELOPER_ACCOUNT_NOT_FOUND_MESSAGE(1000),
    DEVELOPER_ACCOUNT_ILLEGAL_ARGUMENT_MESSAGE(1001),
    DEVELOPER_ACCOUNT_ALREADY_CREATED_THIS_EMAIL_MESSAGE(1010),
    DEVELOPER_ACCOUNT_INFO_NOT_FOUND_MESSAGE(1020),
    DEVELOPER_ACCOUNT_INFO_ROLE_NOT_FOUND_EXCEPTION_MESSAGE(1030),
    DEVELOPER_ROLE_NOT_FOUND_MESSAGE(1100),
    DEVELOPER_ROLE_ILLEGAL_ARGUMENT_MESSAGE(1101),
    DEVELOPER_SHORT_URL_NOT_FOUND_MESSAGE(1500),
    DEVELOPER_SHORT_URL_EXPIRATION_DATE_MESSAGE(1501),
    DEVELOPER_SHORT_URL_RE_DIRECT_NOT_FOUND_MESSAGE(1505),
    DEVELOPER_SHORT_URL_DELETE_NOT_FOUND_MESSAGE(1510),
    DEVELOPER_SHORT_URL_NOT_CREATED_MESSAGE(1555),
    DEVELOPER_SHORT_URL_CREATE_ALREADY_EXISTS_MESSAGE(1559),
    DEVELOPER_SHORT_URL_UPDATE_ALREADY_EXISTS_MESSAGE(1599),
    DEVELOPER_LOG_URL_NOT_FOUND_MESSAGE(1600),
    DEVELOPER_LOG_URL_NOT_FOUND_ADMIN_MESSAGE(1601),
    DEVELOPER_LOG_URL_NOT_FOUND_ADMIN_ALL_MESSAGE(1602),
    DEVELOPER_LOG_SHORT_URL_NOT_FOUND_MESSAGE(1610),
    DEVELOPER_USER_SHORT_URL_ILLEGAL_ARGUMENT_MESSAGE(3333),
    DEVELOPER_PREMIUM_USER_SHORT_URL_ILLEGAL_ARGUMENT_MESSAGE(3333),
    DEVELOPER_LOG_SHORT_URL_ILLEGAL_ARGUMENT_MESSAGE(3444);

    private final int value;

    MessageCodes(int value) {
        this.value = value;
    }
    @Override
    public int getMessageCode() {
        return value;
    }
}