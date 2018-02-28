package org.openskope.timeseries.model;

import java.util.Hashtable;
import java.util.Map;

public class Timeseries {
    
	private String datasetId;
	private String variableName;
	private double latitude;
	private double longitude;
    private int[] values;

	public Timeseries(String datasetId, String variableName, double latitude, double longitude, int[] values) {
		this.datasetId = datasetId;
		this.variableName = variableName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.values = values.clone();
    }

	public String getDatasetId() { return datasetId; }

	public String getVariableName() { return variableName; }
    
	public int[] getValues() { return this.values; }

	public Map<String,Object> getBoundaryGeometry() { 
        
		double[] coordinates = new double[2];
        coordinates[0] = longitude;
        coordinates[1] = latitude;

        Map<String,Object> boundaryGeometry = new Hashtable<String,Object>();
        boundaryGeometry.put("type", "Point");
        boundaryGeometry.put("coordinates", coordinates);
		
        return boundaryGeometry; 
	}
}
