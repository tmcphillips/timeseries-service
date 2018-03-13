package org.openskope.timeseries.controller;

import org.openskope.timeseries.model.ServiceStatus;
import org.openskope.timeseries.model.TimeseriesRequest;
import org.openskope.timeseries.model.TimeseriesResponse;
import org.openskope.timeseries.service.TimeseriesColumnService;
import org.openskope.timeseries.service.TimeseriesTableService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("${TIMESERIES_SERVICE_BASE}/")
public class TimeseriesController {
    
    @Autowired public TimeseriesColumnService timeseriesColumnService;
    @Autowired public TimeseriesTableService timeseriesTableService;
    @Value("${TIMESERIES_SERVICE_NAME}") public String timeseriesServiceName;

	@RequestMapping(value="/status", method=RequestMethod.GET)
    public @ResponseBody ServiceStatus getServiceStatus() throws Exception {
        return new ServiceStatus(timeseriesServiceName);
	}

    @RequestMapping(value="/values", method=RequestMethod.GET)
    public @ResponseBody TimeseriesResponse getTimeSeriesByGet(
            @RequestParam(value="dataset", required=true) String datasetName,
            @RequestParam(value="variable", required=true) String variableName,
            @RequestParam(value="lng", required=true) String longitude,
            @RequestParam(value="lat", required=true) String latitude,
            @RequestParam(value="start", required=false) String start,
            @RequestParam(value="end", required=false) String end
        ) throws Exception {
    	
    	TimeseriesRequest request = new TimeseriesRequest(
    		datasetName,
    		variableName,
    		latitude,
    		longitude,
    		start,
    		end
		);

        return timeseriesColumnService.getTimeseries(request);
	}

    @RequestMapping(value="/values", method=RequestMethod.POST)
    public @ResponseBody TimeseriesResponse getTimeSeriesByPost(
            @RequestBody TimeseriesRequest request,
            HttpServletResponse response
        ) throws Exception {
    	
    	request.validate();
    	
        return timeseriesColumnService.getTimeseries(request);
	}

	@RequestMapping(value="/download", method=RequestMethod.GET)
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
