package org.openskope.timeseries.model;

import java.util.ArrayList;
import java.util.Map;

import org.openskope.timeseries.controller.InvalidArgumentException;
import org.openskope.timeseries.controller.MissingPropertyException;

public class TimeseriesRequest {
	
	private String datasetId;
	private String variableName;
	private Number latitude;
	private Number longitude;
	private String start;
	private String end;
	private String boundaryGeometryType = "Point";
	private boolean invalidBoundaryGeometryType = false;
	
	public TimeseriesRequest() {}

	public void setDatasetId(String datasetId) {
		if (this.datasetId == null) {
			this.datasetId = datasetId;
		}
	}

	public void setVariableName(String variableName) {
		if (this.variableName == null) {
			this.variableName = variableName;
		}
	}

	public void setLatitude(String latitude) {
		if (this.latitude == null && latitude != null) {
			this.latitude = Double.parseDouble(latitude);
		}
	}

	public void setLongitude(String longitude) {
		if (this.longitude == null && longitude != null) {
			this.longitude = Double.parseDouble(longitude);
		}
	}

	public void setStart(String start) {
		if (this.start == null) {
			this.start = start;
		}
	}

	public void setEnd(String end) {
		if (this.end == null) {
			this.end = end;
		}
	}

	@SuppressWarnings("unchecked")
	public void setBoundaryGeometry(Map<String,Object> boundaryGeometry) {
		
		boundaryGeometryType = (String) boundaryGeometry.get("type");
		if (boundaryGeometryType != null && ! boundaryGeometryType.equals("Point")) {
			invalidBoundaryGeometryType = true;
			return;
		}

		ArrayList<Number> coordinates = (ArrayList<Number>) boundaryGeometry.get("coordinates");
		if (coordinates != null) {
			this.longitude = (Number) coordinates.get(0);
			this.latitude  = (Number) coordinates.get(1);
		}
	}
		
	public void validate() throws Exception {
		if (datasetId == null) throw new MissingPropertyException("datasetId");
		if (variableName == null) throw new MissingPropertyException("variableName");
		if (invalidBoundaryGeometryType) throw new InvalidArgumentException("boundaryGeometry.type", boundaryGeometryType);
		if (this.latitude == null) throw new MissingPropertyException("latitude");
		if (this.longitude == null) throw new MissingPropertyException("longitude");
	}

	public String getDatasetId() { return datasetId; }
	public String getVariableName() { return variableName; }
	public double getLatitude() { return latitude.doubleValue(); }
	public double getLongitude() { return longitude.doubleValue(); }
	public String getStart() { return start; }
	public String getEnd() { return end; }
}
