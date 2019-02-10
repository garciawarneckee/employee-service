package com.linke.employeeservice.exceptions;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;

/**
 * Global Exception interceptor and handler.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ AmazonServiceException.class, SdkClientException.class })
	ResponseEntity<Object> handleAmazonExceptions(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "There was an error while image upload, please try again later or contact administration";
		return new ResponseEntity<>(bodyOfResponse, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler({ IOException.class })
	ResponseEntity<Object> handleIOExceptions(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "There was an error processing the file you sent please try again later.";
		return new ResponseEntity<>(bodyOfResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ DataAccessException.class })
	ResponseEntity<Object> handleDataAccessExceptions(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "There was an error to retrieve or process the requested data, please try again later.";
		return new ResponseEntity<>(bodyOfResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
