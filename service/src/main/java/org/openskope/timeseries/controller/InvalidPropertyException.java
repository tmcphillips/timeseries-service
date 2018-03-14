package org.openskope.timeseries.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPropertyException extends RuntimeException {

	public InvalidPropertyException(String propertyName, String invalidValue) {
		super("'" + invalidValue + "' is not a supported value for property '" + propertyName + "'");
	}
}
