package org.openskope.timeseries.service;

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

        String fileName = request.getDatasetId() + "_" + request.getVariableName() + ".tif";
        String[] stringOutputValues = runGdalLocationInfo(fileName,  request.getLongitude(), request.getLatitude());

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
	
	private String[] runGdalLocationInfo(String fileName, double longitude, double latitude) throws Exception {
        String commandLine = String.format(
                "gdallocationinfo -valonly -geoloc %s %f %f", dataDirectory + "/" + fileName, longitude, latitude);
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