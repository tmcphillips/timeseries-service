package org.openskope.timeseries.service;

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
			String datasetId,
			String variableName,
			double dLongitude,
			double dLatitude,
			int start,
			int end
	) throws Exception {

        String fileName = datasetId + "_" + variableName + ".tif";
        String[] stringOutputValues = runGdalLocationInfo(fileName, dLongitude, dLatitude);
        int[] valuesInRequestedRange = getRangeOfStringValuesAsInts(stringOutputValues, start, end);
		return new TimeseriesResponse(datasetId, variableName, dLatitude, dLongitude, start, end , valuesInRequestedRange);
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