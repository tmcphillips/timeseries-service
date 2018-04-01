package org.openskope.timeseries.model;

public enum TimeResolution {
	INVALID,
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

        return TimeResolution.INVALID;
    }
}
