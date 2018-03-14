package org.openskope.timeseries.service;

import org.openskope.timeseries.controller.InvalidArgumentException;
import org.openskope.timeseries.model.TimeseriesRequest;
import org.openskope.timeseries.model.TimeseriesResponse;
import org.yesworkflow.util.exec.ProcessRunner;
import org.yesworkflow.util.exec.StreamSink;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TimeseriesColumnService implements InitializingBean {
    
	@Value("${TIMESERIES_SERVICE_DATA}") public String timeseriesDataDirectory;

    private String dataDirectory;

    public void afterPropertiesSet() {
        dataDirectory = (new File(timeseriesDataDirectory)).getAbsolutePath();
    }

	public TimeseriesResponse getTimeseries(
			TimeseriesRequest request
	) throws Exception {

        File dataFile = getDataFile(request.getDatasetId(), request.getVariableName());
        if (!dataFile.exists()) {
        	throw new InvalidArgumentException("Data file " + dataFile.getName() + " does not exist on timeseries server.");
        }
        
        String[] stringOutputValues = runGdalLocationInfo(dataFile, request.getLongitude(), request.getLatitude());
        if (stringOutputValues.length == 0) {
        	throw new InvalidArgumentException("Coordinates are outside region covered by the dataset");
        }

        Integer rangeStart = (request.getStart() == null) ? 0 : Integer.parseInt(request.getStart());
    	Integer rangeEnd = (request.getEnd() == null) ? stringOutputValues.length - 1: Integer.parseInt(request.getEnd());
        int[] valuesInRequestedRange = getRangeOfStringValuesAsInts(stringOutputValues, rangeStart, rangeEnd);
		
        return new TimeseriesResponse(
        		request.getDatasetId(), 
        		request.getVariableName(), 
        		request.getLatitude(),  
        		request.getLongitude(), 
        		rangeStart, 
        		rangeEnd, 
        		valuesInRequestedRange
    		);
	}
	
	private File getDataFile(String datasetId, String variableName) {
        String fileName = dataDirectory + "/" + datasetId + "_" + variableName + ".tif";
        return new File(fileName); 
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
}