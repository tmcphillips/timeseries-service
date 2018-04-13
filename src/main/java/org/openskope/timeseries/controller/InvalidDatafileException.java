package org.openskope.timeseries.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDatafileException extends RuntimeException {

	public InvalidDatafileException(String message) {
		super(message);
	}
}
