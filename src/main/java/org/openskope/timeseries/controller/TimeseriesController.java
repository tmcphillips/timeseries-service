package org.openskope.timeseries.controller;

import org.openskope.timeseries.VersionInfo;
import org.openskope.timeseries.model.ServiceStatus;
import org.openskope.timeseries.model.TimeseriesRequest;
import org.openskope.timeseries.model.TimeseriesResponse;
import org.openskope.timeseries.service.TimeseriesService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
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

    private VersionInfo versionInfo = VersionInfo.loadVersionInfoFromResource(
        "SKOPE Time-Series Service", 
        "https://github.com/openskope/timeseries-service.git",
        "git.properties",
        "maven.properties");
	
    @Autowired public TimeseriesService timeseriesColumnService;
    @Value("${TIMESERIES_SERVICE_NAME}") public String timeseriesServiceName;

	@RequestMapping(value="/status", method=RequestMethod.GET)
    public @ResponseBody ServiceStatus getServiceStatus() throws Exception {
        return new ServiceStatus(timeseriesServiceName);
	}

	@RequestMapping(value="/version", method=RequestMethod.GET, produces= {MediaType.TEXT_PLAIN_VALUE})
    public @ResponseBody String getVersionInfo() throws Exception {
        return versionInfo.versionBanner() + versionInfo.versionDetails();
	}
	
    @RequestMapping(value="/timeseries/{datasetId}/{variableName}", method=RequestMethod.GET)
    public @ResponseBody TimeseriesResponse requestUsingPathVariablesAndQueryParameters(
            @PathVariable String datasetId,
            @PathVariable String variableName,
            @RequestParam String longitude,
            @RequestParam String latitude,
            @RequestParam(required=false) String timeResolution,
            @RequestParam(required=false) String timeZero,
            @RequestParam(required=false) String start,
            @RequestParam(required=false) String end,
            @RequestParam(required=false) Boolean array,
            @RequestParam(required=false) Boolean csv,
            @RequestParam(required=false) String nodata,
            HttpServletResponse response
        ) throws Exception {
    	
    	TimeseriesRequest request = new TimeseriesRequest();

    	return requestUsingPathVariablesQueryParametersAndBody(
    			request, 
    			datasetId, 
    			variableName, 
    			longitude, 
    			latitude, 
    			timeResolution, 
    			timeZero, 
    			start, 
    			end, 
    			array, 
    			csv, 
    			nodata,
    			response
		);
	}

    @RequestMapping(value="/timeseries/{datasetId}/{variableName}", method=RequestMethod.POST)
    public @ResponseBody TimeseriesResponse requestUsingPathVariablesQueryParametersAndBody(
            @RequestBody TimeseriesRequest request,
            @PathVariable String datasetId,
            @PathVariable String variableName,
            @RequestParam(required=false) String longitude,
            @RequestParam(required=false) String latitude,
            @RequestParam(required=false) String timeResolution,
            @RequestParam(required=false) String timeZero,
            @RequestParam(required=false) String start,
            @RequestParam(required=false) String end,
            @RequestParam(required=false) Boolean array,
            @RequestParam(required=false) Boolean csv,
            @RequestParam(required=false) String nodata,
            HttpServletResponse response
        ) throws Exception {

    	// set datasetId and variableName to those given in URL 
    	// only if the request body did not already specify each
    	if (request.getDatasetId() == null) request.setDatasetId(datasetId); 
    	if (request.getVariableName() == null) request.setVariableName(variableName);

    	return requestUsingQueryParametersAndBody(
    			request, 
    			longitude, 
    			latitude, 
    			timeResolution, 
    			timeZero, 
    			start, 
    			end, 
    			array, 
    			csv,
    			nodata,
    			response
		);
	}
    
    @RequestMapping(value="/timeseries", method=RequestMethod.POST)
    public @ResponseBody TimeseriesResponse requestUsingQueryParametersAndBody(
            @RequestBody TimeseriesRequest request,
            @RequestParam(required=false) String longitude,
            @RequestParam(required=false) String latitude,
            @RequestParam(required=false) String timeResolution,
            @RequestParam(required=false) String timeZero,
            @RequestParam(required=false) String start,
            @RequestParam(required=false) String end,
            @RequestParam(required=false) Boolean array,
            @RequestParam(required=false) Boolean csv,
            @RequestParam(required=false) String nodata,
            HttpServletResponse response
        ) throws Exception {

    	if (request.getLatitude() == null) request.setLatitude(latitude); 
    	if (request.getLongitude() == null) request.setLongitude(longitude); 
    	if (request.getTimeResolution() == null) request.setTimeResolution(timeResolution); 
    	if (request.getTimeZero() == null) request.setTimeZero(timeZero); 
    	if (request.getStart() == null) request.setStart(start); 
    	if (request.getEnd() == null) request.setEnd(end);
    	if (request.getArray() == null) request.setArray(array);
    	if (request.getCsv() == null) request.setCsv(csv);
    	if (request.getNodata() == null) request.setNodata(nodata);
    	
    	request.validate();
    	
        return timeseriesColumnService.getTimeseries(request);
	}

//        response.setContentType("text/csv");
//        response.setHeader("Content-Disposition", "attachment; filename=timeseries.csv");
//        
//        return timeseriesTableService.getTable(request);
//	}

}
