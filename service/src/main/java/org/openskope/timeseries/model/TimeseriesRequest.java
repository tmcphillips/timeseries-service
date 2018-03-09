package org.openskope.timeseries.model;

import java.util.ArrayList;
import java.util.Map;

public class TimeseriesRequest {
	
	private String datasetId;
	private String variableName;
	private Number latitude;
	private Number longitude;
	private String start;
	private String end;

	public TimeseriesRequest() {}
	
	public TimeseriesRequest(
			String datasetId, 
			String variableName, 
			String latitude, 
			String longitude, 
			String start,
			String end
		) {
		this.datasetId = datasetId;
		this.variableName = variableName;
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
        this.start = start;
        this.end = end;
    }

	public void setBoundaryGeometry(Map<String,Object> boundaryGeometry) {
		@SuppressWarnings("rawtypes")
		ArrayList coordinates = (ArrayList)boundaryGeometry.get("coordinates");
		this.longitude = (Number) coordinates.get(0);
		this.latitude  = (Number) coordinates.get(1);
	}
	
	public void setRange(Map<String,String> range) {
		this.start = range.get("start");
		this.end   = range.get("end");
	}
	
	public String getDatasetId() { return datasetId; }
	public String getVariableName() { return variableName; }
	public double getLatitude() { return latitude.doubleValue(); }
	public double getLongitude() { return longitude.doubleValue(); }
	public String getStart() { return start; }
	public String getEnd() { return end; }
}
