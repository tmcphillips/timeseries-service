package org.openskope.timeseries.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingPropertyException extends RuntimeException {

	public MissingPropertyException(String argumentName) {
		super("Required property '" + argumentName + "' is not present");
	}
}
