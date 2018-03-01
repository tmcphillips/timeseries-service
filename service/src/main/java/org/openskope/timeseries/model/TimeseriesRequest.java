package org.openskope.timeseries.model;

import java.util.ArrayList;
import java.util.Map;

public class TimeseriesRequest {
	
	private String datasetId;
	private String variableName;
	private Number latitude;
	private Number longitude;
	private int start;
	private int end;

	public void setDatasetId(String datasetId) { this.datasetId = datasetId; }
	public void setVariableName(String variableName) { this.variableName = variableName; }

	public void setBoundaryGeometry(Map<String,Object> boundaryGeometry) {
		@SuppressWarnings("rawtypes")
		ArrayList coordinates = (ArrayList)boundaryGeometry.get("coordinates");
		this.longitude = (Number) coordinates.get(0);
		this.latitude  = (Number) coordinates.get(1);
	}
	
	public void setRange(Map<String,Integer> range) {
		this.start = range.get("start");
		this.end   = range.get("end");
	}
	
	public String getDatasetId() { return datasetId; }
	public String getVariableName() { return variableName; }
	public double getLatitude() { return latitude.doubleValue(); }
	public double getLongitude() { return longitude.doubleValue(); }
	public int getStart() { return start; }
	public int getEnd() { return end; }
    
}
