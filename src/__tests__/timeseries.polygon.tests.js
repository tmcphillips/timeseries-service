const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );

describe("When a GET request selects from a region exactly covering a 2x2 pixel area in the dataset", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/timeseries',
		    entity: {
		    	datasetId: '5x5x5',
		    	variableName: 'temp',
		    	boundaryGeometry: {
    		    "type": "Polygon",
    		    "coordinates": [[
	    		    	[-123,45],
	    		        [-123,47],
	    		        [-121,47],
	    		        [-121,45],
	    		        [-123,45]
	    		    	]]
	    		},
	    		start: 0,
	    		end: 4
		    }
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
    
    it ('The array should comprise the average of four pixels in each band', async function() {
        expect(response.entity.values[0]).toBeCloseTo(105.5,3);
        expect(response.entity.values[1]).toBeCloseTo(205.5,3);
        expect(response.entity.values[2]).toBeCloseTo(305.5,3);
        expect(response.entity.values[3]).toBeCloseTo(405.5,3);
        expect(response.entity.values[4]).toBeCloseTo(505.5,3);
    });
    
    it ('The data column should comprise the average of four pixels in each band', async function() {
        expect(response.entity.csv).toEqual( 
        		"index, temp"	+ "\n" +
        		"0, 105.500"	+ "\n" +
        		"1, 205.500"	+ "\n" +
        		"2, 305.500"	+ "\n" +
        		"3, 405.500"	+ "\n" +
    			"4, 505.500"	+ "\n"
		);
    });
});

describe("When a GET request selects from a region of zero area using a polygon geometry type", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/timeseries',
		    entity: {
		    	datasetId: '5x5x5',
		    	variableName: 'temp',
		    	boundaryGeometry: {
    		    "type": "Polygon",
    		    "coordinates": [[
	    		    	[-122.5,45.5],
	    		    	[-122.5,45.5],
	    		    	[-122.5,45.5],
	    		    	[-122.5,45.5],
	    		    	[-122.5,45.5],
	    		    	]]
	    		},
	    		start: 0,
	    		end: 4
		    }
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });
    
    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that longitude parameter is not present', async function() {
        expect(response.entity.message).toContain("Input shapes do not overlap raster.")
    }); 
});

describe("When a GET request selects from a 2x2 pixel region that intersects a 2x1 pixel region in dataset", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/timeseries',
		    entity: {
		    	datasetId: '5x5x5',
		    	variableName: 'temp',
		    	boundaryGeometry: {
    		    "type": "Polygon",
    		    "coordinates": [[
    		    	[-123,44],
    		        [-123,46],
    		        [-121,46],
    		        [-121,44],
    		        [-123,44]
	    		    	]]
	    		},
	    		start: 0,
	    		end: 4
		    }
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
    
    it ('The array should comprise the average of four pixels in each band', async function() {
        expect(response.entity.values[0]).toBeCloseTo(100.5,3);
        expect(response.entity.values[1]).toBeCloseTo(200.5,3);
        expect(response.entity.values[2]).toBeCloseTo(300.5,3);
        expect(response.entity.values[3]).toBeCloseTo(400.5,3);
        expect(response.entity.values[4]).toBeCloseTo(500.5,3);
    });
    
    it ('The data column should comprise the average of the two pixels intersection of polygon and dataset', async function() {
        expect(response.entity.csv).toEqual( 
        		"index, temp"	+ "\n" +
        		"0, 100.500"	+ "\n" +
        		"1, 200.500"	+ "\n" +
        		"2, 300.500"	+ "\n" +
        		"3, 400.500"	+ "\n" +
    			"4, 500.500"	+ "\n"
		);
    });
});

describe("When a GET request selects a region just outside coverage of dataset", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/timeseries',
		    entity: {
		    	datasetId: '5x5x5',
		    	variableName: 'temp',
		    	boundaryGeometry: {
    		    "type": "Polygon",
    		    "coordinates": [[
    		    	[-124,45],
    		        [-124,47],
    		        [-123,47],
    		        [-123,45],
    		        [-123,45]
	    		    	]]
	    		},
	    		start: 0,
	    		end: 4
		    }
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });
    
    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that longitude parameter is not present', async function() {
        expect(response.entity.message).toContain("Input shapes do not overlap raster.")
    }); 
});

describe("When a GET request selects exactly the top-left corner pixel", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/timeseries',
		    entity: {
		    	datasetId: '5x5x5',
		    	variableName: 'temp',
		    	boundaryGeometry: {
    		    "type": "Polygon",
    		    "coordinates": [[
	    		    	[-123.0,45],
	    		        [-123.0,46],
	    		        [-122,46],
	    		        [-122,45],
	    		        [-123.0,45]
	    		    	]]
	    		},
	    		start: 0,
	    		end: 4
		    }
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
    
    it ('The array should comprise the values of the top-left pixels in each band', async function() {
        expect(response.entity.values[0]).toBeCloseTo(100.0,3);
        expect(response.entity.values[1]).toBeCloseTo(200.0,3);
        expect(response.entity.values[2]).toBeCloseTo(300.0,3);
        expect(response.entity.values[3]).toBeCloseTo(400.0,3);
        expect(response.entity.values[4]).toBeCloseTo(500.0,3);
    });
    
    it ('The data column should comprise the values of the top-left pixels in each band', async function() {
        expect(response.entity.csv).toEqual( 
        		"index, temp"	+ "\n" +
        		"0, 100.000"	+ "\n" +
        		"1, 200.000"	+ "\n" +
        		"2, 300.000"	+ "\n" +
        		"3, 400.000"	+ "\n" +
    			"4, 500.000"	+ "\n"
		);
    });
});

describe("When a GET request selects 1/4 of the top-left corner pixel the service reports an error", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/timeseries',
		    entity: {
		    	datasetId: '5x5x5',
		    	variableName: 'temp',
		    	boundaryGeometry: {
    		    "type": "Polygon",
    		    "coordinates": [[
	    		    	[-123.0,45],
	    		        [-123.0,45.25],
	    		        [-122.75,45.25],
	    		        [-122.75,45.25],
	    		        [-123.0,45]
	    		    	]]
	    		},
	    		start: 0,
	    		end: 4
		    }
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(400);
    });
    
    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that longitude parameter is not present', async function() {
        expect(response.entity.message).toContain("Warning: converting a masked element to nan.")
    }); 
});
