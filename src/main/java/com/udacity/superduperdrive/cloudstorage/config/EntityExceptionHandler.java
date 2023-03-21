package com.udacity.superduperdrive.cloudstorage.config;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class EntityExceptionHandler
        extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { SizeLimitExceededException.class, MaxUploadSizeExceededException.class, MultipartException.class })
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    protected ResponseEntity handleConflict(
            RuntimeException ex, WebRequest request) {
        Map<String, String> result = new HashMap<>();
        result.put("message", "The uploaded file is too large.");
        return  ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(result);
    }
}