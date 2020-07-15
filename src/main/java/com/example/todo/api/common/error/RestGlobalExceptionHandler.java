package com.example.todo.api.common.error;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;
import org.terasoluna.gfw.common.message.ResultMessage;

@ControllerAdvice
public class RestGlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(RestGlobalExceptionHandler.class);

	@Inject
	MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		Object responseBody = body;
		if (body == null) {
			responseBody = createApiError(request, "E999", ex.getMessage());
		}
		return ResponseEntity.status(status).headers(headers).body(responseBody);
	}
	
	

	private ApiError createApiError(WebRequest request, String errorCode, Object... args) {
		return new ApiError(errorCode, messageSource.getMessage(errorCode, args, request.getLocale()));
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
		ApiError apiError = createApiError(request, "E402");
		if (logger.isErrorEnabled()) {
			logger.error("ConstraintViolations[\n{}\n]", ex.getConstraintViolations());
		}
		for(ConstraintViolation<?> error: ex.getConstraintViolations()) {
		apiError.addDetail(createApiError(request, error.getMessage(), null));
		}
		return handleExceptionInternal(ex, apiError, null, null, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError apiError = createApiError(request, "E400");
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			apiError.addDetail(createApiError(request, fieldError, fieldError.getField()));
		}
		for (ObjectError objectError : ex.getBindingResult().getGlobalErrors()) {
			apiError.addDetail(createApiError(request, objectError, objectError.getObjectName()));
		}
		return handleExceptionInternal(ex, apiError, headers, status, request);
	}

	//　日付かどうかのチェックはなぜかビーンバリデーションのメッセージが設定できなかったため、とりあえずエラーコードで対応
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError apiError = createApiError(request, "E401");
		return handleExceptionInternal(ex, apiError, headers, status, request);
	}
	

	private ApiError createApiError(WebRequest request, DefaultMessageSourceResolvable messageSourceResolvable,
			String target) {
		return new ApiError(messageSourceResolvable.getCode(),
				messageSource.getMessage(messageSourceResolvable, request.getLocale()), target);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
		return handleResultMessagesNotificationException(ex, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	private ResponseEntity<Object> handleResultMessagesNotificationException(ResultMessagesNotificationException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ResultMessage message = ex.getResultMessages().iterator().next();
		ApiError apiError = createApiError(request, message.getCode(), message.getArgs());
		return handleExceptionInternal(ex, apiError, headers, status, request);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		return handleResultMessagesNotificationException(ex, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleSystemError(Exception ex, WebRequest request) {
		ApiError apiError = createApiError(request, "E500");
		return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

}