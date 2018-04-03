package org.openskope.timeseries.service;

import org.openskope.timeseries.controller.InvalidArgumentException;
import org.openskope.timeseries.model.IndexRange;
import org.openskope.timeseries.model.TimeScale;
import org.openskope.timeseries.model.TimeseriesRequest;
import org.openskope.timeseries.model.TimeseriesResponse;
import org.yesworkflow.util.exec.ProcessRunner;
import org.yesworkflow.util.exec.StreamSink;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class TimeseriesService implements InitializingBean {
    
	@Value("${TIMESERIES_DATA_PATH_TEMPLATE}") public String timeseriesDataPath;
	@Value("${TIMESERIES_DATA_FILE_EXTENSIONS}") public String timeseriesDatafileExtensions;

    private UriTemplate dataPathTemplate;
    private Map<String, String> uriVariables = new HashMap<String,String>();
    private String[] extensions;

    public void afterPropertiesSet() {
    	
    	dataPathTemplate = new UriTemplate(timeseriesDataPath);
    	
    	String[] customExtensionArray = {};
    	if (timeseriesDatafileExtensions.trim().length() > 0) {
    		customExtensionArray = timeseriesDatafileExtensions.split("\\s+");
    	}
    	extensions = new String[1 + customExtensionArray.length];
    	extensions[0] = "";
    	int i = 1;
    	for (String customExtension : customExtensionArray) {
    		extensions[i++] = customExtension;
    	}
    }

	public TimeseriesResponse getTimeseries(TimeseriesRequest request) throws Exception {

        File dataFile = getDataFile(request.getDatasetId(), request.getVariableName());
        if (dataFile == null) {
        	throw new InvalidArgumentException(
    			"Data file for dataset '" + request.getDatasetId() +
    			"', variable '" + request.getVariableName() + 
    			"' does not exist on timeseries server."
			);
        }
        
        Number nodataValue = getNodataValue(request.getNodata(), dataFile);
        
        String[] fullTimeSeries = runGdalLocationInfo(dataFile, request.getLongitude(), request.getLatitude());
        if (fullTimeSeries.length == 0) {
        	throw new InvalidArgumentException("Coordinates are outside region covered by the dataset");
        }

        TimeScale timeScale = new TimeScale(request.getTimeResolution(), request.getTimeZero());
        
        IndexRange responseRange = timeScale.getResponseIndexRange(request.getStart(), request.getEnd(), fullTimeSeries.length);
        
        int[] valuesInRequestedRange = getRangeOfStringValuesAsInts(fullTimeSeries, responseRange.startIndex, responseRange.endIndex);
        
        int[] values =  (request.getArray() == null || request.getArray()) ? valuesInRequestedRange : null;
        String csv = (request.getCsv() == null || request.getCsv()) ? getTable(responseRange, timeScale, request.getVariableName(), valuesInRequestedRange) : null;
		        
        boolean containsNodata = valueContainNodataValue(nodataValue, valuesInRequestedRange);
        
        return new TimeseriesResponse(
        		request.getDatasetId(),
        		request.getVariableName(),
        		request.getLatitude(),
        		request.getLongitude(),
        		timeScale.getTimeForIndex(responseRange.startIndex),
        		timeScale.getTimeForIndex(responseRange.endIndex),
        		responseRange.startIndex,
        		responseRange.endIndex,
        		values,
        		csv,
        		nodataValue,
        		containsNodata
    		);
	}
	
	private File getDataFile(String datasetId, String variableName) {
        uriVariables.put("datasetId", datasetId);
        uriVariables.put("variableName", variableName);
		URI datafileBaseUri = dataPathTemplate.expand(uriVariables);
		for (String extension : extensions) {
			File file = new File(datafileBaseUri.getPath() + extension);
			if (file.exists()) {
				return file;
			}
		}
		return null;
	}
	
	private Number getNodataValue(String requestNodata, File dataFile) {
		Number nodata;
		if (requestNodata == null || requestNodata.equalsIgnoreCase("detect")) {
			nodata = detectNodataMetadata(dataFile);
		} else if (requestNodata.equalsIgnoreCase("ignore")) {
			nodata = null;
		} else {
			try {
				nodata = Double.parseDouble(requestNodata);
			} catch (Exception e) {
	        	throw new InvalidArgumentException("nodata", requestNodata);
			}
		}
		return nodata;
	}

	private Number detectNodataMetadata(File dataFile) {
		return null;
	}
	
	private boolean valueContainNodataValue(Number nodataValue, int[] values) {
		if (nodataValue == null) return false;
		for (Number n : values) {
			if (n.intValue() == nodataValue.intValue()) return true;
		}
		return false;
	}
	
	private String[] runGdalLocationInfo(File dataFile, double longitude, double latitude) throws Exception {
        String commandLine = String.format(
                "gdallocationinfo -valonly -geoloc %s %f %f", dataFile.getAbsolutePath(), longitude, latitude);
        System.out.println(commandLine);
        StreamSink streams[] = ProcessRunner.run(commandLine, "", new String[0], null);
        return streams[0].toString().split("\\s+");
	}
	
	private int[] getRangeOfStringValuesAsInts(String[] sa, int start, int end) {
		int[] ia = new int[end - start + 1];
		for (int si = 0, ii = 0; si < sa.length; ++si) {
			if (si >= start && si <= end) {
				ia[ii++] = Integer.valueOf(sa[si]);
			}
		}
		return ia;
	}
	
	public String getTable(IndexRange responseRange, TimeScale timeScale, String variableName, int[] values) throws Exception {
		StringBuffer buffer = new StringBuffer();
        buffer.append(timeScale + ", " + variableName + "\n");
        for (int i = 0; i < values.length; ++i) {
            buffer.append(String.format("%s, %s\n", timeScale.getTimeForIndex(i + responseRange.startIndex), values[i]));
        }
        return buffer.toString();
	}
}