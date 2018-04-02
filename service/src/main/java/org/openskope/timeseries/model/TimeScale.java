package org.openskope.timeseries.model;

import org.openskope.timeseries.controller.InvalidArgumentException;

public class TimeScale {
    
	private TimeResolution timeResolution;
	private String timeZero;
	
	public TimeScale(String timeResolutionSetting, String timeZeroSetting) {

		timeResolution = TimeResolution.toTimeResolution(timeResolutionSetting);
		 
		if (timeZeroSetting != null) {
			 timeZero = timeZeroSetting;
		} else {
			switch (this.timeResolution) {
				case INDEX:
					timeZero = "0";
					break;
				case BAND:
				case YEAR:
					timeZero = "1";
					break;
				default:
					break;
			}
		}
	}
	
	public IndexRange getResponseIndexRange(String requestedStartTime, String requestedEndTime, int fullTimeseriesLength) throws Exception {
		switch (timeResolution) {
			case INDEX:
			case BAND:
			case YEAR:
				return getResponseIndexRangeForIntegerTimeResolution(requestedStartTime, requestedEndTime, fullTimeseriesLength);
			default:
				throw new Exception();
		}
	}
	
	public IndexRange getResponseIndexRangeForIntegerTimeResolution(String start, String end, int valueCount) {
		
		int tzero = Integer.parseInt(timeZero);
		
        int startIndex = (start == null) ? tzero : Integer.parseInt(start) - tzero;
        if (startIndex > valueCount - 1) {
        	throw new InvalidArgumentException("Time range start is outside coverage of dataset");
        }

        int endIndex = (end == null) ? valueCount - 1: Integer.parseInt(end) - tzero;
        if (endIndex > valueCount - 1) {
        	endIndex = valueCount - 1;
        }
        
        if (endIndex < startIndex) {
        	throw new InvalidArgumentException("Time range end is before time range start");
        }
        
		return new IndexRange(startIndex, endIndex);
	}
}