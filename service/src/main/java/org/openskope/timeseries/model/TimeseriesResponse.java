package org.openskope.timeseries.model;

import java.util.HashMap;
import java.util.Map;

public class TimeseriesResponse {
	
	private String datasetId;
	private String variableName;
	private double latitude;
	private double longitude;
	private int startIndex;
	private int endIndex;
    private int[] values;
    private String csv;

	public TimeseriesResponse(
			String datasetId, 
			String variableName, 
			double latitude, 
			double longitude, 
			int startIndex,
			int endIndex,
			int[] values,
			String csv
		) {
		this.datasetId = datasetId;
		this.variableName = variableName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.values = values;
        this.csv = csv;
    }

	public String getDatasetId() { return datasetId; }
	public String getVariableName() { return variableName; }
	public int[] getValues() { return this.values; }
	public String getCsv() { return this.csv; }
	public String getStart() { return String.valueOf(startIndex); }
	public String getEnd() { return String.valueOf(endIndex); }
	public Integer getStartIndex() { return startIndex; }
	public Integer getEndIndex() { return endIndex; }

	public Map<String,Object> getBoundaryGeometry() { 
		double[] coordinates = new double[2];
        coordinates[0] = longitude;
        coordinates[1] = latitude;
        Map<String,Object> boundaryGeometry = new HashMap<String,Object>();
        boundaryGeometry.put("type", "Point");
        boundaryGeometry.put("coordinates", coordinates);
        return boundaryGeometry; 
	}	
}
