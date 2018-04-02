package org.openskope.timeseries.model;

import org.openskope.timeseries.controller.InvalidArgumentException;

public class TimeScale {
    
	private TimeResolution timeResolution;
	private Integer integerTimeZero;
	
	public TimeScale(String timeResolutionSetting, String timeZeroSetting) {

		timeResolution = TimeResolution.toTimeResolution(timeResolutionSetting);
		 
		switch (this.timeResolution) {
			case INDEX:
				integerTimeZero = (timeZeroSetting == null) ? 0 : Integer.parseInt(timeZeroSetting);
				break;
			case BAND:
			case YEAR:
				integerTimeZero = (timeZeroSetting == null) ? 1 : Integer.parseInt(timeZeroSetting);
				break;
			default:
				break;
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
		
        int startIndex = (start == null) ? integerTimeZero : Integer.parseInt(start) - integerTimeZero;
        if (startIndex > valueCount - 1) {
        	throw new InvalidArgumentException("Time range start is outside coverage of dataset");
        }

        int endIndex = (end == null) ? valueCount - 1: Integer.parseInt(end) - integerTimeZero;
        if (endIndex > valueCount - 1) {
        	endIndex = valueCount - 1;
        }
        
        if (endIndex < startIndex) {
        	throw new InvalidArgumentException("Time range end is before time range start");
        }
        
		return new IndexRange(startIndex, endIndex);
	}
	
	public String getTimeForIndex(int index) throws Exception {
		switch (timeResolution) {
			case INDEX:
			case BAND:
			case YEAR:
				return getTimeForIndexForIntegerTimeResolution(index);
			default:
				throw new Exception();
		}
	}

	public String getTimeForIndexForIntegerTimeResolution(int index) {
		return String.valueOf(index + integerTimeZero);
	}
}