package org.openskope.timeseries.model;

public enum TimeResolution {
	UNDEFINED,
	INDEX,
	BAND,
	YEAR,
	MONTH;
	
    public static TimeResolution toTimeResolution(String tr) {
        
        if (tr == null || tr.equalsIgnoreCase("index"))
            return TimeResolution.INDEX;

        if (tr.equalsIgnoreCase("band"))
            return TimeResolution.BAND;

        if (tr.equalsIgnoreCase("year") || tr.equalsIgnoreCase("years") || tr.equalsIgnoreCase("annual"))
            return TimeResolution.YEAR;

        if (tr.equalsIgnoreCase("month") || tr.equalsIgnoreCase("months") || tr.equalsIgnoreCase("monthly"))
            return TimeResolution.MONTH;

        return TimeResolution.UNDEFINED;
    }
    
    public String toString() {
    	switch(this) {
    	case INDEX: return "index";
    	case BAND: return "band";
    	case YEAR: return "year";
        case MONTH: return "month";
    	default: return "UNDEFINED";
    	}
    }
    
}
