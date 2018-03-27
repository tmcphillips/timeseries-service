package org.openskope.timeseries.controller;

import org.openskope.timeseries.VersionInfo;
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

	@RequestMapping(value="/version", method=RequestMethod.GET)
    public @ResponseBody String getVersionInfo() throws Exception {
		
		
        VersionInfo versionInfo = 
                VersionInfo.loadVersionInfoFromResource(
                        "SKOPE Time-Series Service", 
                        "https://github.com/openskope/timeseries-service.git",
                        "git.properties",
                        "maven.properties");
        
        
        return versionInfo.versionDetails();
	}
	
    @RequestMapping(value="/timeseries/{datasetId}/{variableName}", method=RequestMethod.GET)
    public @ResponseBody TimeseriesResponse requestUsingPathVariablesAndQueryParameters(
            @PathVariable(value="datasetId") String datasetId,
            @PathVariable(value="variableName") String variableName,
            @RequestParam(value="longitude", required=true) String longitude,
            @RequestParam(value="latitude", required=true) String latitude,
            @RequestParam(value="start", required=false) String start,
            @RequestParam(value="end", required=false) String end,
            @RequestParam(value="array", required=false) Boolean returnArray,
            @RequestParam(value="csv", required=false) Boolean returnCsv,
            HttpServletResponse response
        ) throws Exception {
    	
    	TimeseriesRequest request = new TimeseriesRequest();

    	return requestUsingPathVariablesQueryParametersAndBody(request, datasetId, variableName, longitude, latitude, start, end, returnArray, returnCsv, response);
	}

    @RequestMapping(value="/timeseries/{datasetId}/{variableName}", method=RequestMethod.POST)
    public @ResponseBody TimeseriesResponse requestUsingPathVariablesQueryParametersAndBody(
            @RequestBody TimeseriesRequest request,
            @PathVariable(value="datasetId") String datasetId,
            @PathVariable(value="variableName") String variableName,
            @RequestParam(value="longitude", required=false) String longitude,
            @RequestParam(value="latitude", required=false) String latitude,
            @RequestParam(value="start", required=false) String start,
            @RequestParam(value="end", required=false) String end,
            @RequestParam(value="array", required=false) Boolean returnArray,
            @RequestParam(value="csv", required=false) Boolean returnCsv,
            HttpServletResponse response
        ) throws Exception {

    	request.setDatasetId(datasetId); 
    	request.setVariableName(variableName);

    	return requestUsingQueryParametersAndBody(request, longitude, latitude, start, end, returnArray, returnCsv, response);
	}
    
    @RequestMapping(value="/timeseries", method=RequestMethod.POST)
    public @ResponseBody TimeseriesResponse requestUsingQueryParametersAndBody(
            @RequestBody TimeseriesRequest request,
            @RequestParam(value="longitude", required=false) String longitude,
            @RequestParam(value="latitude", required=false) String latitude,
            @RequestParam(value="start", required=false) String start,
            @RequestParam(value="end", required=false) String end,
            @RequestParam(value="array", required=false) Boolean returnArray,
            @RequestParam(value="csv", required=false) Boolean returnCsv,
            HttpServletResponse response
        ) throws Exception {

    	request.setLatitude(latitude); 
    	request.setLongitude(longitude); 
    	request.setStart(start); 
    	request.setEnd(end);
    	request.setArray(returnArray);
    	request.setCsv(returnCsv);
    	
    	request.validate();
    	
        return timeseriesColumnService.getTimeseries(request);
	}

//        response.setContentType("text/csv");
//        response.setHeader("Content-Disposition", "attachment; filename=timeseries.csv");
//        
//        return timeseriesTableService.getTable(request);
//	}

}
