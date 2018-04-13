package org.openskope.timeseries.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openskope.timeseries.controller.InvalidArgumentException;
import org.openskope.timeseries.controller.MissingPropertyException;

public class TimeseriesRequest {
	
	private String datasetId;
	private String variableName;
	private Number latitude;
	private Number longitude;
	private String timeResolution;
	private String timeZero;
	private String start;
	private String end;
	private String boundaryGeometryType = "Point";
	private Boolean invalidBoundaryGeometryType = false;
	private Boolean returnCsv;
	private Boolean returnArray;
	private String nodata;
	private Map<String, Object> boundaryGeometry = new HashMap<String,Object>();
	
	public TimeseriesRequest() {}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public void setLatitude(String latitude) {
		if (latitude != null) this.latitude = Double.parseDouble(latitude);
	}

	public void setLongitude(String longitude) {
		if (longitude != null) this.longitude = Double.parseDouble(longitude);
	}

	public void setTimeResolution(String timeResolution) {
		this.timeResolution = timeResolution;
	}
	
	public void setTimeZero(String timeZero) {
		this.timeZero = timeZero;
	}
	
	public void setStart(String start) {
		this.start = start;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setArray(Boolean returnArray) {
		this.returnArray = returnArray;
	}

	public void setCsv(Boolean returnCsv) {
		this.returnCsv = returnCsv;
	}
	
	public void setNodata(String nodata) {
		this.nodata = nodata;
	}

	@SuppressWarnings("unchecked")
	public void setBoundaryGeometry(Map<String,Object> boundaryGeometry) {
		
		this.boundaryGeometry.put("geometry", boundaryGeometry);
		
		boundaryGeometryType = (String) boundaryGeometry.get("type");
		if (boundaryGeometryType != null && !boundaryGeometryType.equalsIgnoreCase("Point") && 
											! boundaryGeometryType.equalsIgnoreCase("Polygon")) {
			invalidBoundaryGeometryType = true;
			return;
		}
		
		if (boundaryGeometryType.equalsIgnoreCase("Point")) {
			ArrayList<Number> coordinates = (ArrayList<Number>) boundaryGeometry.get("coordinates");
			if (coordinates != null) {
				this.longitude = (Number) coordinates.get(0);
				this.latitude  = (Number) coordinates.get(1);
			}
		}
	}
		
	public void validate() throws Exception {
		if (datasetId == null) throw new MissingPropertyException("datasetId");
		if (variableName == null) throw new MissingPropertyException("variableName");
		if (invalidBoundaryGeometryType) throw new InvalidArgumentException("boundaryGeometry.type", boundaryGeometryType);
		if (boundaryGeometryType.equalsIgnoreCase("Point")) {
			if (this.latitude == null) throw new MissingPropertyException("latitude");
			if (this.longitude == null) throw new MissingPropertyException("longitude");
		}
	}

	public String getBoundaryGeometryType() { return boundaryGeometryType; }
	public String getDatasetId() { return datasetId; }
	public String getVariableName() { return variableName; }
	public Double getLatitude() { return latitude != null ? latitude.doubleValue() : null; }
	public Double getLongitude() { return longitude != null ? longitude.doubleValue() : null; }
	public String getTimeResolution() { return timeResolution; }
	public String getTimeZero() { return timeZero; }
	public String getStart() { return start; }
	public String getEnd() { return end; }
	public Boolean getArray() { return returnArray; }
	public Boolean getCsv() { return returnCsv; }
	public String getNodata() { return nodata; }

	public Map<String,Object>  getBoundaryGeometry() {
		if (boundaryGeometry.get("geometry") == null) {
	        Map<String,Object> geometry = new HashMap<String,Object>();
			Number[] coordinates = new Double[2];
	        coordinates[0] = longitude;
	        coordinates[1] = latitude;
	        geometry.put("type", "Point");
	        geometry.put("coordinates", coordinates);
			this.boundaryGeometry.put("geometry", geometry);
		}
		return boundaryGeometry;	
	}
}
