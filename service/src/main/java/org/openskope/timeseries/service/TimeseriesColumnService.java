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
        String commandLine = String.format(
            "gdallocationinfo -valonly -wgs84 %s %s %s", dataDirectory + "/" + fileName, longitude, latitude);
        System.out.println(commandLine);
        StreamSink streams[] = ProcessRunner.run(commandLine, "", new String[0], null);
        Timeseries timeseries = new Timeseries(end - start + 1);
        timeseries.setValues(streams[0].toString().split("\\s+"));

		return timeseries;
	}
}