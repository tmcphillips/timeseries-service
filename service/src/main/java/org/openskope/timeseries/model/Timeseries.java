package org.openskope.timeseries.model;

public class Timeseries {
    
    private int[] values;

	public Timeseries(int[] values) {
        this.values = values.clone();
    }

    public int[] getValues() {
        return this.values;
    }
}
