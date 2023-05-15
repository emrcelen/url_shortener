package com.wenthor.urlshortener.exception.handler;

import java.util.ArrayList;
import java.util.List;
import com.wenthor.todoapp.response.RestErrorResponse;
import com.wenthor.urlshortener.enums.MessageCodes;
import com.wenthor.urlshortener.exception.exceptions.AccountAlreadyCreatedException;
import com.wenthor.urlshortener.exception.exceptions.AccountInfoNotFoundException;
import com.wenthor.urlshortener.exception.exceptions.AccountNotFoundException;
import com.wenthor.urlshortener.exception.exceptions.IllegalArgumentException;
import com.wenthor.urlshortener.exception.exceptions.LogShortUrlNotFoundException;
import com.wenthor.urlshortener.exception.exceptions.RoleNotFoundException;
import com.wenthor.urlshortener.exception.exceptions.ShortUrlAlreadyExistsException;
import com.wenthor.urlshortener.exception.exceptions.ShortUrlNotCreatedException;
import com.wenthor.urlshortener.exception.exceptions.ShortUrlNotFoundException;
import com.wenthor.urlshortener.response.rest.RestResponse;
import com.wenthor.urlshortener.response.rest.error.RestNotvalidMethodResponse;
import com.wenthor.urlshortener.utilities.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public final ResponseEntity handleAccountNotFoundException(AccountNotFoundException ex, WebRequest webRequest) {
        String message = ex.getMessage();
        Integer errorCode = ex.getExceptionCode();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, errorCode, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccountAlreadyCreatedException.class)
    public final ResponseEntity handleAccountAlreadyCreatedException(AccountAlreadyCreatedException ex, WebRequest webRequest){
        String message = ex.getMessage();
        Integer errorCode = ex.getExceptionCode();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, errorCode, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccountInfoNotFoundException.class)
    public final ResponseEntity handleAccountInfoNotFoundException(AccountInfoNotFoundException ex, WebRequest webRequest){
        String message = ex.getMessage();
        Integer errorCode = ex.getExceptionCode();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, errorCode, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity handleBadCredentialsException(BadCredentialsException ex, WebRequest webRequest, Locale locale){
        String message = MessageUtils.getMessage(locale,MessageCodes.BAD_CREDENTIAL_EXCEPTION);
        Integer errorCode = MessageCodes.BAD_CREDENTIAL_EXCEPTION.getMessageCode();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, errorCode, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RoleNotFoundException.class)
    public final ResponseEntity handlRoleNotFoundException(RoleNotFoundException ex, WebRequest webRequest) {
        String message = ex.getMessage();
        Integer errorCode = ex.getExceptionCode();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, errorCode, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ShortUrlNotFoundException.class)
    public final ResponseEntity handlShortUrlFoundException(ShortUrlNotFoundException ex, WebRequest webRequest) {
        String message = ex.getMessage();
        Integer errorCode = ex.getExceptionCode();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, errorCode, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ShortUrlNotCreatedException.class)
    public final ResponseEntity handlShortUrlNotCreatedException(ShortUrlNotCreatedException ex, WebRequest webRequest) {
        String message = ex.getMessage();
        Integer errorCode = ex.getExceptionCode();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, errorCode, HttpStatus.I_AM_A_TEAPOT);
    }
    @ExceptionHandler(ShortUrlAlreadyExistsException.class)
    public final ResponseEntity handleShortUrlAlreadyExistsException(ShortUrlAlreadyExistsException ex, WebRequest webRequest){
        String message = ex.getMessage();
        Integer errorCode = ex.getExceptionCode();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, errorCode, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(LogShortUrlNotFoundException.class)
    public final ResponseEntity handleLogShortUrlNotFoundException(LogShortUrlNotFoundException ex, WebRequest webRequest){
        String message = ex.getMessage();
        Integer errorCode = ex.getExceptionCode();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, errorCode, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity handleAccessDeniedException(AccessDeniedException ex, WebRequest webRequest, Locale locale) {
        String message;
        if (ex.getMessage().equalsIgnoreCase("Acces is denied"))
            message = "You are not authorized to perform this operation, please contact your administrator.";
        message = MessageUtils.getMessage(locale, MessageCodes.ACCESS_IS_DENIED);
        Integer errorCode = MessageCodes.ACCESS_IS_DENIED.getMessageCode();
        String detail = webRequest.getDescription(false);
        
        return getResponseEntity(message, detail,errorCode,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity handleIllegalArgumentException(IllegalArgumentException ex, WebRequest webRequest) {
        String message = ex.getMessage();
        Integer errorCode = ex.getExceptionCode();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, errorCode, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity handleConstraintViolationException(ConstraintViolationException ex, WebRequest webRequest, Locale locale){
        String message = MessageUtils.getMessage(locale,MessageCodes.GENERAL_ILLEGAL_ARGUMENT_EXCEPTION);
        Integer errorCode = MessageCodes.GENERAL_ILLEGAL_ARGUMENT_EXCEPTION.getMessageCode();
        String detail = webRequest.getDescription(false);
        return getResponseConstraintViolationEntity(message,detail,errorCode,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest, Locale locale){
        String message = String.format(MessageUtils.getMessage(locale,MessageCodes.METHOD_NOT_VALID_EXCEPTION),
                ex.getFieldError().getRejectedValue());
        Integer errorCode = MessageCodes.METHOD_NOT_VALID_EXCEPTION.getMessageCode();
        String detail = webRequest.getDescription(false);
        if(ex.getParameter().getMethod().getName().equalsIgnoreCase("accountRegister")){
            List<String> list = new ArrayList<>();
            list.add("example@example.com");
            list.add("example@example.net");
            message = String.format(MessageUtils.getMessage(locale, MessageCodes.EMAIL_NOT_VALID_EXCEPTION),
                    ex.getFieldError().getRejectedValue());
            errorCode =MessageCodes.EMAIL_NOT_VALID_EXCEPTION.getMessageCode();
            return getResponseInvalidMethodEntity(message,list,detail,errorCode, HttpStatus.BAD_REQUEST);
        }
        return getResponseInvalidMethodEntity(message,detail,errorCode,HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntity(String message, String detail, Integer errorCode, HttpStatus httpStatus) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        RestErrorResponse restErrorResponse = new RestErrorResponse(
                formatter.format(LocalDateTime.now()),
                message,
                errorCode,
                detail);
        RestResponse<RestErrorResponse> restResponse = RestResponse.Companion.error(restErrorResponse);
        return new ResponseEntity<>(restResponse, httpStatus);
    }
    private ResponseEntity<Object> getResponseConstraintViolationEntity(String message, String detail, Integer errorCode, HttpStatus httpStatus){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        List<String> list = new ArrayList<>();
        list.add("example.com");
        list.add("www.example.com");
        RestNotvalidMethodResponse restInvalidMethodResponse = new RestNotvalidMethodResponse(
                formatter.format(LocalDateTime.now()),
                message,
                list,
                errorCode,
                detail
        );
        RestResponse<RestNotvalidMethodResponse> restResponse = RestResponse.Companion.error(restInvalidMethodResponse);
        return new ResponseEntity<>(restResponse, httpStatus);
    }
    private ResponseEntity<Object> getResponseInvalidMethodEntity(String message,List<String> list, String detail, Integer errorCode, HttpStatus httpStatus){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        RestNotvalidMethodResponse restInvalidMethodResponse = new RestNotvalidMethodResponse(
                formatter.format(LocalDateTime.now()),
                message,
                list,
                errorCode,
                detail
        );
        RestResponse<RestNotvalidMethodResponse> restResponse = RestResponse.Companion.error(restInvalidMethodResponse);
        return new ResponseEntity<>(restResponse, httpStatus);
    }

    private ResponseEntity<Object> getResponseInvalidMethodEntity(String message, String detail, Integer errorCode, HttpStatus httpStatus){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        RestNotvalidMethodResponse restInvalidMethodResponse = new RestNotvalidMethodResponse(
                formatter.format(LocalDateTime.now()),
                message,
                errorCode,
                detail
        );
        RestResponse<RestNotvalidMethodResponse> restResponse = RestResponse.Companion.error(restInvalidMethodResponse);
        return new ResponseEntity<>(restResponse, httpStatus);
    }
}
