package com.ne.autotest.tencent.fca.boot.service.exception;

import com.ne.autotest.tencent.fca.boot.common.entity.Response;
import com.ne.autotest.tencent.fca.boot.common.exception.IError;
import com.ne.autotest.tencent.fca.boot.common.exception.NEError;
import com.ne.autotest.tencent.fca.boot.common.exception.NEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({Throwable.class})
    @ResponseBody
    public ResponseEntity<Response> handleError(Throwable e) {
        if (logger.isErrorEnabled()) {
            logger.error("ne error", e);
        }

        return this.build(e);
    }

    protected ResponseEntity build(Throwable ex) {
        String extMessage = null;
        Object error;
        if (ex instanceof NEException) {
            error = ((NEException)ex).getError();
            extMessage = ((NEException)ex).getExtMessage();
        } else {
            StringBuilder msg;
            Iterator var6;
            if (ex instanceof BindException) {
                error = NEError.INVALID_PARAMETER;
                List<ObjectError> errors = ((BindException)ex).getAllErrors();
                if (errors != null && errors.size() != 0) {
                    msg = new StringBuilder();

                    ObjectError objectError;
                    for(var6 = errors.iterator(); var6.hasNext(); msg.append(objectError.getDefaultMessage() + " ")) {
                        objectError = (ObjectError)var6.next();
                        msg.append("Field error in object '" + objectError.getObjectName() + " ");
                        if (objectError instanceof FieldError) {
                            msg.append("on field " + ((FieldError)objectError).getField() + " ");
                        }
                    }

                    extMessage = msg.toString();
                }
            } else if (ex instanceof MissingServletRequestParameterException) {
                error = NEError.INVALID_PARAMETER;
                extMessage = ex.getMessage();
            } else if (ex instanceof ConstraintViolationException) {
                Set<ConstraintViolation<?>> violations = ((ConstraintViolationException)ex).getConstraintViolations();
                msg = new StringBuilder();
                var6 = violations.iterator();

                while(var6.hasNext()) {
                    ConstraintViolation<?> constraintViolation = (ConstraintViolation)var6.next();
                    msg.append(constraintViolation.getPropertyPath()).append(":").append(constraintViolation.getMessage() + "\n");
                }

                error = NEError.INVALID_PARAMETER;
                extMessage = msg.toString();
            } else if (ex instanceof MissingServletRequestParameterException) {
                error = NEError.INVALID_PARAMETER;
                extMessage = ex.getMessage();
            } else if (ex instanceof HttpMediaTypeNotSupportedException) {
                error = NEError.CONTENT_TYPE_NOT_SUPPORT;
                extMessage = ex.getMessage();
            } else if (ex instanceof HttpMessageNotReadableException) {
                error = NEError.INVALID_PARAMETER;
                extMessage = ex.getMessage();
            } else if (ex instanceof MethodArgumentNotValidException) {
                error = NEError.INVALID_PARAMETER;
                extMessage = ex.getMessage();
            } else if (ex instanceof HttpRequestMethodNotSupportedException) {
                error = NEError.METHOD_NOT_SUPPORTED;
                extMessage = ex.getMessage();
            } else if (ex instanceof UnexpectedTypeException) {
                error = NEError.INVALID_PARAMETER;
                extMessage = ex.getMessage();
            } else if (ex instanceof NoHandlerFoundException) {
                error = NEError.SERVICE_NOT_FOUND;
                extMessage = ex.getMessage();
            } else {
                error = NEError.SYSTEM_INTERNAL_ERROR;
                extMessage = ex.getMessage();
            }
        }

        Response response = Response.error((IError)error);
        response.setExtMessage(extMessage);
        HttpStatus status;
        if (error == NEError.SYSTEM_INTERNAL_ERROR) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (error == NEError.INVALID_PARAMETER) {
            status = HttpStatus.BAD_REQUEST;
        } else if (error == NEError.METHOD_NOT_SUPPORTED) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (error == NEError.SERVICE_NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        } else if (error == NEError.CONTENT_TYPE_NOT_SUPPORT) {
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        } else {
            status = HttpStatus.OK;
        }

        ResponseEntity<Response> entity = new ResponseEntity(response, status);
        return entity;
    }
}
