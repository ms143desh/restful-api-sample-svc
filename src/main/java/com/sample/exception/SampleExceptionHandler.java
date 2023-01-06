package com.sample.exception;

import com.sample.config.ApplicationPropertiesConfig;
import com.sample.constants.Constants;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class SampleExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(SampleExceptionHandler.class);

    private final MessageSource messageSource;

    @Autowired private ApplicationPropertiesConfig applicationPropertiesConfig;

    @Autowired
    public SampleExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        String[] messageStr = getMessage("SampleError", null, Locale.US).split("::");
        ApiError error =
                new ApiError(
                        applicationPropertiesConfig.getSvcErrorPrefix() + messageStr[0],
                        messageStr[1]);
        ApiErrors errors = new ApiErrors(new ApiError[] {error});
        return new ResponseEntity<>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleInvalidScopes(Exception e) {
        String[] messageStr = getMessage("AccessDenied", null, Locale.US).split("::");
        final HttpStatus status = HttpStatus.FORBIDDEN;
        final ApiError error =
                new ApiError(
                        applicationPropertiesConfig.getSvcErrorPrefix() + messageStr[0],
                        messageStr[1]);
        ApiErrors errors = new ApiErrors(new ApiError[] {error});
        return new ResponseEntity<>(errors, new HttpHeaders(), status);
    }

    @ExceptionHandler(SampleException.class)
    public ResponseEntity<Object> lcSampleException(
            SampleException lcSampleException, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String[] messageStr = {};
        switch (lcSampleException.getMessage()) {
            case "accessDenied":
                messageStr = getMessage("AccessDenied", null, Locale.US).split("::");
                status = HttpStatus.FORBIDDEN;
                break;
            case "invalidName":
                messageStr =
                        getMessage(
                                        "invalidName",
                                        new Object[] {
                                            lcSampleException.getErrorMessageParameter(),
                                            applicationPropertiesConfig.getNameRegexPattern()
                                        },
                                        Locale.US)
                                .split("::");
                break;
            case "invalidAge":
                messageStr =
                        getMessage(
                                        "invalidAge",
                                        new Object[] {applicationPropertiesConfig.getMinAge()},
                                        Locale.US)
                                .split("::");
                break;
            case "invalidCompany":
                messageStr =
                        getMessage(
                                        "invalidCompany",
                                        new Object[] {
                                            lcSampleException.getErrorMessageParameter(),
                                            applicationPropertiesConfig.getCompanyRegexPattern()
                                        },
                                        Locale.US)
                                .split("::");
                break;
            case "unsupportedEncoding":
                messageStr = getMessage("UnsupportedEncodingError", null, Locale.US).split("::");
                break;

            default:
        }
        ApiError error =
                new ApiError(
                        applicationPropertiesConfig.getSvcErrorPrefix() + messageStr[0],
                        messageStr[1]);
        ApiErrors errors = new ApiErrors(new ApiError[] {error});
        return new ResponseEntity<>(errors, new HttpHeaders(), status);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeServiceException(
            Exception excep, WebRequest request) {

        String[] messageStr = getMessage(Constants.SERVICE_ERROR, null, Locale.US).split("::");
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError error =
                new ApiError(
                        applicationPropertiesConfig.getSvcErrorPrefix() + messageStr[0],
                        messageStr[1]);
        ApiErrors errors = new ApiErrors(new ApiError[] {error});
        return new ResponseEntity<>(errors, new HttpHeaders(), status);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleInternalError(Exception excep) {
        String[] messageStr = getMessage(Constants.SERVICE_ERROR, null, Locale.US).split("::");
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError error =
                new ApiError(
                        applicationPropertiesConfig.getSvcErrorPrefix() + messageStr[0],
                        messageStr[1]);
        ApiErrors errors = new ApiErrors(new ApiError[] {error});
        return new ResponseEntity<>(errors, new HttpHeaders(), status);
    }

    public String getMessage(String code, Object[] args, Locale locale) {
        return this.messageSource.getMessage(code, args, locale);
    }
}
