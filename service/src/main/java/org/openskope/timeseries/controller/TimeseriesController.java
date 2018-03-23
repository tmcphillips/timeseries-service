package org.openskope.timeseries.controller;

import org.openskope.timeseries.model.ServiceStatus;
import org.openskope.timeseries.model.TimeseriesRequest;
import org.openskope.timeseries.model.TimeseriesResponse;
import org.openskope.timeseries.service.TimeseriesService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @Autowired public TimeseriesService timeseriesColumnService;
    @Value("${TIMESERIES_SERVICE_NAME}") public String timeseriesServiceName;

	@RequestMapping(value="/status", method=RequestMethod.GET)
    public @ResponseBody ServiceStatus getServiceStatus() throws Exception {
        return new ServiceStatus(timeseriesServiceName);
	}

    @RequestMapping(value="/timeseries/{datasetId}/{variableName}", method=RequestMethod.GET)
    public @ResponseBody TimeseriesResponse requestUsingQueryLineOnly(
            @PathVariable(value="datasetId", required=true) String datasetId,
            @PathVariable(value="variableName", required=true) String variableName,
            @RequestParam(value="longitude", required=true) String longitude,
            @RequestParam(value="latitude", required=true) String latitude,
            @RequestParam(value="start", required=false) String start,
            @RequestParam(value="end", required=false) String end,
            @RequestParam(value="format", required=false) String format,
            HttpServletResponse response
        ) throws Exception {
    	
    	TimeseriesRequest requestBody = new TimeseriesRequest();

    	return requestUsingBodyAndQueryLine(requestBody, datasetId, variableName, longitude, latitude, start, end, format, response);
	}

    @RequestMapping(value="/values", method=RequestMethod.POST)
    public @ResponseBody TimeseriesResponse requestUsingBodyAndQueryLine(
            @RequestBody TimeseriesRequest requestBody,
            @RequestParam(value="datasetId", required=false) String datasetId,
            @RequestParam(value="variableName", required=false) String variableName,
            @RequestParam(value="longitude", required=false) String longitude,
            @RequestParam(value="latitude", required=false) String latitude,
            @RequestParam(value="start", required=false) String start,
            @RequestParam(value="end", required=false) String end,
            @RequestParam(value="format", required=false) String format,
            HttpServletResponse response
        ) throws Exception {

    	requestBody.setDatasetId(datasetId); 
    	requestBody.setVariableName(variableName); 
    	requestBody.setLatitude(latitude); 
    	requestBody.setLongitude(longitude); 
    	requestBody.setStart(start); 
    	requestBody.setEnd(end); 
    	requestBody.setFormat(format);
    	
    	requestBody.validate();
    	
        return timeseriesColumnService.getTimeseries(requestBody);
	}

//        response.setContentType("text/csv");
//        response.setHeader("Content-Disposition", "attachment; filename=timeseries.csv");
//        
//        return timeseriesTableService.getTable(request);
//	}

}
