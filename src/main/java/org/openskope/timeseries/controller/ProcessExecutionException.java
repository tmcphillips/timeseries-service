package org.openskope.timeseries.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProcessExecutionException extends RuntimeException {

	public ProcessExecutionException(String message) {
		super(message);
	}
}
