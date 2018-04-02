package org.openskope.timeseries.model;

public enum TimeResolution {
	UNDEFINED,
	INDEX,
	BAND,
	YEAR;
	
    public static TimeResolution toTimeResolution(String tr) {
        
        if (tr == null || tr.equalsIgnoreCase("index"))
            return TimeResolution.INDEX;

        if (tr.equalsIgnoreCase("band"))
            return TimeResolution.BAND;

        if (tr.equalsIgnoreCase("year") || tr.equalsIgnoreCase("annual"))
            return TimeResolution.YEAR;

        return TimeResolution.UNDEFINED;
    }
    
    public String toString() {
    	switch(this) {
    	case INDEX: return "index";
    	case BAND: return "band";
    	case YEAR: return "year";
    	default: return "UNDEFINED";
    	}
    }
    
}
