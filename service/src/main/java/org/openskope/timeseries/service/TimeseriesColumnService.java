package org.openskope.timeseries.service;

import org.openskope.timeseries.model.Timeseries;

import org.yesworkflow.util.exec.ProcessRunner;
import org.yesworkflow.util.exec.StreamSink;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TimeseriesColumnService implements InitializingBean {
    
	@Value("${timeseries.data}") public String timeseriesDataDirectory;

    private String dataDirectory;

    public void afterPropertiesSet() {
        dataDirectory = (new File(timeseriesDataDirectory)).getAbsolutePath();
    }

	public Timeseries getTimeseries(
			String dataSetName,
			String variableName,
			String longitude,
			String latitude,
			int start,
			int end
	) throws Exception {
        String fileName = dataSetName + "_" + variableName + ".tif";
        String[] stringOutputValues = runGdalLocationInfo(fileName, longitude, latitude);
        int[] valuesInRequestedRange = getRangeOfStringValuesAsInts(stringOutputValues, start, end);
		return new Timeseries(valuesInRequestedRange);
	}
	
	private String[] runGdalLocationInfo(String fileName, String longitude, String latitude) throws Exception {
        String commandLine = String.format(
                "gdallocationinfo -valonly -wgs84 %s %s %s", dataDirectory + "/" + fileName, longitude, latitude);
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