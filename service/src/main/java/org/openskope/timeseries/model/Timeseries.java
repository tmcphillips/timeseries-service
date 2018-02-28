package org.openskope.timeseries.model;

public class Timeseries {
    
    private String[] values;

	public Timeseries(int size) {
        values = new String[size];
    }

    public String[] getValues() {
        return this.values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

}
