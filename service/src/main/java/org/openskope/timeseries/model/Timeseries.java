package org.openskope.timeseries.model;

import java.util.Hashtable;
import java.util.Map;

public class Timeseries {
    
	private String datasetId;
	private String variableName;
	private double latitude;
	private double longitude;
    private int[] values;
    private Map<String,Object> boundaryGeometry;

	public Timeseries(String datasetId, String variableName, double latitude, double longitude, int[] values) {
		this.datasetId = datasetId;
		this.variableName = variableName;
        this.values = values.clone();
        this.boundaryGeometry = new Hashtable<String,Object>();
        this.boundaryGeometry.put("type", "Point");
        double[] coordinates = new double[2];
        coordinates[0] = longitude;
        coordinates[1] = latitude;
        this.boundaryGeometry.put("coordinates", coordinates);
        
    }

	public String getDatasetId() { return datasetId; }
	public String getVariableName() { return variableName; }
	public Map<String,Object> getBoundaryGeometry() { return boundaryGeometry; }
    public int[] getValues() { return this.values; }
}
