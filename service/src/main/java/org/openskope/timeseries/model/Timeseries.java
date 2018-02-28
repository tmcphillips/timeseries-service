package org.openskope.timeseries.model;

public class Timeseries {
    
	private String datasetId;
	private String variableName;
	private double latitude;
	private double longitude;
    private int[] values;

	public Timeseries(String datasetId, String variableName, double latitude, double longitude, int[] values) {
		this.datasetId = datasetId;
		this.variableName = variableName;
        this.values = values.clone();
    }

	public String getDatasetId() { return datasetId; }
	public String getVariableName() { return variableName; }
    public int[] getValues() { return this.values; }
}
