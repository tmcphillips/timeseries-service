package org.openskope.timeseries.model;

import org.openskope.timeseries.controller.InvalidArgumentException;



public class TimeScale {

    private class YearMonth {
 
        public final int month;
        public final int year;
        
        public YearMonth(String yearMonthString) {
            String[] parts = yearMonthString.split("-");
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
        }
        
        public YearMonth(int months) {
            year = months / 12 + 1;
            month = months % 12 + 1;
        }
        
        public int getMonthDifference(YearMonth earlierYearMonth) {
            return ( (12 * this.year + this.month) - (12 * earlierYearMonth.year + earlierYearMonth.month) );
        }

        public YearMonth addMonths(int months) {
            return new YearMonth(12 * (this.year - 1) + (this.month - 1) + months);
        }

        
        public String toString() {
            return String.format("%04d-%02d", year, month);
        }
   }
    
	private TimeResolution timeResolution;
	private Integer integerTimeZero;
	private YearMonth yearMonthTimeZero;
	
	public TimeScale(String timeResolutionSetting, String timeZeroSetting) {

		timeResolution = TimeResolution.toTimeResolution(timeResolutionSetting);
		 
		switch (this.timeResolution) {
			case INDEX:
				integerTimeZero = 0;
				break;
			case BAND:
				integerTimeZero = 1;
				break;
			case YEAR:
				integerTimeZero = (timeZeroSetting == null) ? 1 : Integer.parseInt(timeZeroSetting);
				break;
			case MONTH:
			    yearMonthTimeZero = new YearMonth(timeZeroSetting);
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
            case MONTH:
                return getResponseIndexRangeForMonthTimeResolution(requestedStartTime, requestedEndTime, fullTimeseriesLength);
			default:
				throw new Exception();
		}
	}
	
	public IndexRange getResponseIndexRangeForIntegerTimeResolution(String start, String end, int valueCount) {
		
        int startIndex = (start == null) ? 0 : Integer.parseInt(start) - integerTimeZero;
        if (startIndex < 0 || startIndex > valueCount - 1) {
        	throw new InvalidArgumentException("Time range start is outside coverage of dataset");
        }

        int endIndex = (end == null) ? valueCount - 1: Integer.parseInt(end) - integerTimeZero;
        if (endIndex < 0) {
        	throw new InvalidArgumentException("Time range end is outside coverage of dataset");
        }
        if (endIndex > valueCount - 1) {
        	endIndex = valueCount - 1;
        }
        
        if (endIndex < startIndex) {
        	throw new InvalidArgumentException("Time range end is before time range start");
        }
        
		return new IndexRange(startIndex, endIndex);
	}
	
   public IndexRange getResponseIndexRangeForMonthTimeResolution(String start, String end, int valueCount) {

       YearMonth startYearMonth = (start == null) ? null : new YearMonth(start);
       YearMonth endYearMonth = (end == null) ? null : new YearMonth(end);
       
        int startIndex = (startYearMonth == null) ? 0 : startYearMonth.getMonthDifference(yearMonthTimeZero);
        if (startIndex < 0 || startIndex > valueCount - 1) {
            throw new InvalidArgumentException("Time range start is outside coverage of dataset");
        }

        int endIndex = (endYearMonth == null) ? valueCount - 1: endYearMonth.getMonthDifference(yearMonthTimeZero);
        if (endIndex < 0) {
            throw new InvalidArgumentException("Time range end is outside coverage of dataset");
        }
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
			case MONTH:
			    return getTimeForIndexForYearMonthTimeResolution(index);
			default:
				throw new Exception();
		}
	}

	public String getTimeForIndexForIntegerTimeResolution(int index) {
		return String.valueOf(index + integerTimeZero);
	}
	
   public String getTimeForIndexForYearMonthTimeResolution(int index) {
        return yearMonthTimeZero.addMonths(index).toString();
    }
	   
	public String toString() {
		return timeResolution.toString();
	}
}