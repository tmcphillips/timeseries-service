package org.openskope.timeseries.service;

import org.openskope.timeseries.controller.InvalidArgumentException;
import org.openskope.timeseries.model.IndexRange;
import org.openskope.timeseries.model.TimeScale;
import org.openskope.timeseries.model.TimeseriesRequest;
import org.openskope.timeseries.model.TimeseriesResponse;
import org.yesworkflow.util.exec.ProcessRunner;
import org.yesworkflow.util.exec.StreamSink;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TimeseriesService implements InitializingBean {
    
	@Value("${TIMESERIES_DATA_PATH_TEMPLATE}") public String timeseriesDataPath;
	@Value("${TIMESERIES_DATA_FILE_EXTENSIONS}") public String timeseriesDatafileExtensions;
	@Value("${TIMESERIES_GDALLOCATIONINFO_COMMAND}") public String gdallocationinfoCommand;
	@Value("${TIMESERIES_ZONALINFO_COMMAND}") public String zonalinfoCommand;

    private UriTemplate dataPathTemplate;
    private String[] extensions;
    private Map<String, Number> nodataSettingForFile = new HashMap<String,Number>();
    private Map<String, String> uriVariables = new HashMap<String,String>();
    private ObjectMapper mapper = new ObjectMapper();
    
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
        
        TimeScale timeScale = new TimeScale(request.getTimeResolution(), request.getTimeZero());
        Number nodataValue = getNodataValue(request.getNodata(), dataFile);
        String[] fullTimeSeries = getFullTimeseries(request, dataFile);
        IndexRange responseRange = timeScale.getResponseIndexRange(request.getStart(), request.getEnd(), fullTimeSeries.length);
        
        Number[] valuesInRequestedRange = getRangeOfStringValuesAsNumbers(fullTimeSeries, responseRange.startIndex, responseRange.endIndex);
        
        Number[] values =  (request.getArray() == null || request.getArray()) ? valuesInRequestedRange : null;
        String csv = (request.getCsv() == null || request.getCsv()) ? getTable(responseRange, timeScale, request.getVariableName(), valuesInRequestedRange) : null;
		        
        boolean containsNodata = containsNodataValue(nodataValue, valuesInRequestedRange);
        
        return new TimeseriesResponse(
        		request.getDatasetId(),
        		request.getVariableName(),
        		request.getBoundaryGeometry(),
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
	
	private String[] getFullTimeseries(TimeseriesRequest request, File dataFile) throws Exception {
		
		String[] fullTimeSeries = null; 
		
		if (request.getBoundaryGeometryType().equalsIgnoreCase("POINT")) {
			fullTimeSeries = runGdalLocationInfo(dataFile, request.getLongitude(), request.getLatitude());
		} else {
			fullTimeSeries = runZonalInfo(dataFile, request);			
		}
		
        if (fullTimeSeries.length == 0) {
        	throw new InvalidArgumentException("Coordinates are outside region covered by the dataset");
        }
        return fullTimeSeries;
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
		
		String dataFilePath = dataFile.getAbsolutePath();
		Number nodataValue = nodataSettingForFile.get(dataFilePath);

		if (nodataValue == null) {
        
			String commandLine = String.format("gdalinfo %s ", dataFilePath);
	        System.out.println(commandLine);
	
	        String gdalinfoOutput;
	        try { 
		        StreamSink streams[] = ProcessRunner.run(commandLine, "", new String[0], null);
		        gdalinfoOutput = streams[0].toString();
	        } catch(Exception e) {
	        	return null;
	        }
	        
	        String nodataValueString = null;
	        Pattern pattern = Pattern.compile("NoData Value=(-?[0-9.]+)");
	        Matcher matcher = pattern.matcher(gdalinfoOutput);
	        if (matcher.find()) {
	        	nodataValueString = matcher.group(1);
	        	nodataValue = parseIntegerOrDouble(nodataValueString);
	        	nodataSettingForFile.put(dataFilePath, nodataValue);
	        }
		}
		
    	return nodataValue;
	}
	
	private boolean containsNodataValue(Number nodataValue, Number[] values) {
		if (nodataValue == null) return false;
		for (Number n : values) {
			if (n.intValue() == nodataValue.intValue()) return true;
		}
		return false;
	}
	
	private String[] runGdalLocationInfo(File dataFile, double longitude, double latitude) throws Exception {
        String commandLine = String.format(
                "%s -valonly -geoloc %s %f %f", gdallocationinfoCommand, dataFile.getAbsolutePath(), longitude, latitude);
        System.out.println(commandLine);
        StreamSink streams[] = ProcessRunner.run(commandLine, "", new String[0], null);
        return streams[0].toString().split("\\s+");
	}
	
	private String[] runZonalInfo(File dataFile, TimeseriesRequest request) throws Exception {
        String commandLine = String.format(
                "%s --valonly --geometry - %s", zonalinfoCommand, dataFile.getAbsolutePath());
        System.out.println(commandLine);
        String stdin = mapper.writeValueAsString(request.getBoundaryGeometry());
        System.out.println(stdin);
        StreamSink streams[] = ProcessRunner.run(commandLine, mapper.writeValueAsString(request.getBoundaryGeometry()), new String[0], null);
        return streams[0].toString().split("\\s+");
	}
	
	private Number[] getRangeOfStringValuesAsNumbers(String[] sa, int start, int end) {
		Number[] ia = new Number[end - start + 1];
		for (int si = 0, ii = 0; si < sa.length; ++si) {
			if (si >= start && si <= end) {
				ia[ii++] = parseIntegerOrDouble(sa[si]);
			}
		}
		return ia;
	}
	
	private Number parseIntegerOrDouble(String s) {
		Number n;
		if (s.contains(".")) {
			Double d= Double.valueOf(s);
			n = d;
		} else {
			Integer i = Integer.valueOf(s);
			n = i;
		}
		return n;
	}
	
	public String getTable(IndexRange responseRange, TimeScale timeScale, String variableName, Number[] values) throws Exception {
		StringBuffer buffer = new StringBuffer();
        buffer.append(timeScale + ", " + variableName + "\n");
        for (int i = 0; i < values.length; ++i) {
        	if (values[i] instanceof Double) {
        		buffer.append(String.format("%s, %.6g\n", timeScale.getTimeForIndex(i + responseRange.startIndex), values[i]));
        	} else {
        		buffer.append(String.format("%s, %d\n", timeScale.getTimeForIndex(i + responseRange.startIndex), values[i]));
        	}
        }
        return buffer.toString();
	}
}