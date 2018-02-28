package org.openskope.timeseries.controller;

import org.openskope.timeseries.model.ServiceStatus;
import org.openskope.timeseries.model.Timeseries;
import org.openskope.timeseries.service.TimeseriesColumnService;
import org.openskope.timeseries.service.TimeseriesTableService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@RestController 
@EnableAutoConfiguration
@CrossOrigin 
@RequestMapping("${timeseries.endpoint}/")
public class TimeseriesController {
    
    @Autowired public TimeseriesColumnService timeseriesColumnService;
    @Autowired public TimeseriesTableService timeseriesTableService;
    @Value("${service.name}") public String timeseriesServiceName;

	@RequestMapping(value="/status", method=RequestMethod.GET)
    public @ResponseBody ServiceStatus getServiceStatus() throws Exception {
        return new ServiceStatus(timeseriesServiceName);
	}

    @RequestMapping(value="/timeseries", method=RequestMethod.GET)
    public @ResponseBody Timeseries getTimeSeries(
            @RequestParam(value="data", required=true) String datasetName,
            @RequestParam(value="var", required=true) String variableName,
            @RequestParam(value="lng", required=true) String longitude,
            @RequestParam(value="lat", required=true) String latitude,
            @RequestParam(value="start", required=true) int start,
            @RequestParam(value="end", required=true) int end
        ) throws Exception {

        return timeseriesColumnService.getTimeseries(datasetName, variableName, longitude, latitude, start, end);
	}

	@RequestMapping(value="/timeseries-download", method=RequestMethod.GET)
    public @ResponseBody String getTimeSeriesDownload(
            HttpServletResponse response,
            @RequestParam(value="long", required=true) String longitude,
            @RequestParam(value="lat", required=true) String latitude,
            @RequestParam(value="startYear", required=true) String startYear,
            @RequestParam(value="endYear", required=true) String endYear
        ) throws Exception {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=SKOPE.csv");
        return timeseriesTableService.getTable(longitude, latitude, startYear, endYear);
	}
}
