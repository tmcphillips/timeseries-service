package org.openskope.timeseries.model;

import java.util.HashMap;
import java.util.Map;

public class TimeseriesResponse {
	
	private String datasetId;
	private String variableName;
	private double latitude;
	private double longitude;
	private int start;
	private int end;
    private int[] values;

	public TimeseriesResponse(
			String datasetId, 
			String variableName, 
			double latitude, 
			double longitude, 
			int start,
			int end,
			int[] values
		) {
		this.datasetId = datasetId;
		this.variableName = variableName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.start = start;
        this.end = end;
        this.values = values.clone();
    }

	public String getDatasetId() { return datasetId; }

	public String getVariableName() { return variableName; }
    
	public int[] getValues() { return this.values; }

	public Map<String,Object> getBoundaryGeometry() { 
        
		double[] coordinates = new double[2];
        coordinates[0] = longitude;
        coordinates[1] = latitude;

        Map<String,Object> boundaryGeometry = new HashMap<String,Object>();
        boundaryGeometry.put("type", "Point");
        boundaryGeometry.put("coordinates", coordinates);
		
        return boundaryGeometry; 
	}
	
	public Integer getStart() { return start; }
	public Integer getEnd() { return end; }

}
