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
	private TimeResolution timeResolution = TimeResolution.INDEX;
	private String timeZero;
	private String start;
	private String end;
	private String boundaryGeometryType = "Point";
	private Boolean invalidBoundaryGeometryType = false;
	private Boolean returnCsv;
	private Boolean returnArray;
	
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
		this.timeResolution = TimeResolution.toTimeResolution(timeResolution);
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
		if (timeResolution == TimeResolution.INVALID) throw new InvalidArgumentException("Unrecognized TimeResolution");
		if (datasetId == null) throw new MissingPropertyException("datasetId");
		if (variableName == null) throw new MissingPropertyException("variableName");
		if (invalidBoundaryGeometryType) throw new InvalidArgumentException("boundaryGeometry.type", boundaryGeometryType);
		if (this.latitude == null) throw new MissingPropertyException("latitude");
		if (this.longitude == null) throw new MissingPropertyException("longitude");
	}

	public String getDatasetId() { return datasetId; }
	public String getVariableName() { return variableName; }
	public Double getLatitude() { return latitude != null ? latitude.doubleValue() : null; }
	public Double getLongitude() { return longitude != null ? longitude.doubleValue() : null; }
	public TimeResolution getTimeResolution() { return timeResolution; }
	public String getTimeZero() { return timeZero; }
	public String getStart() { return start; }
	public String getEnd() { return end; }
	public Boolean getArray() { return returnArray; }
	public Boolean getCsv() { return returnCsv; }

	public BandRange getBandRange(int valueCount) {
		
        int startBand = (start == null) ? 0 : Integer.parseInt(start);
        if (startBand > valueCount - 1) {
        	throw new InvalidArgumentException("Time range start is outside coverage of dataset");
        }

        int endBand = (end == null) ? valueCount - 1: Integer.parseInt(end);
        if (endBand > valueCount - 1) {
        	endBand = valueCount - 1;
        }
        
        if (endBand < startBand) {
        	throw new InvalidArgumentException("Time range end is before time range start");
        }
        
		return new BandRange(startBand, endBand);
	}

}
