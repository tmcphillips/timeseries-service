const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );

describe("When a GET request selects from a float32 datafile a coordinate with all integral values", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp_float32?longitude=-123.0&latitude=45.0&start=0&end=4&csv=true&array=true'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
    
    it ('The nodata value should be the one defined in the data file', async function() {
        expect(response.entity.nodata).toEqual(-1);
	});

    it ('No nodata values should be found', async function() {
        expect(response.entity.containsNodata).toEqual(false);
	});
    
    it ('The array should comprise all integral values', async function() {
        expect(response.entity.values).toEqual( [100, 200, 300, 400, 500] );
    });
    
    it ('The data column in the csv should comprise all integral values', async function() {
        expect(response.entity.csv).toEqual( 
        		"index, temp_float32"	+ "\n" +
        		"0, 100"				+ "\n" +
        		"1, 200"				+ "\n" +
        		"2, 300"				+ "\n" +
        		"3, 400"				+ "\n" +
    			"4, 500"				+ "\n"
		);
    });
});

describe("When a GET request selects from a float32 datafile a coordinate with all non-integral values", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp_float32?longitude=-121.0&latitude=47.0&start=0&end=4&csv=true&array=true'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
    
    it ('The nodata value should be the one defined in the data file', async function() {
        expect(response.entity.nodata).toEqual(-1);
	});

    it ('No nodata values should be found', async function() {
        expect(response.entity.containsNodata).toEqual(false);
	});
    
    it ('The array should comprise all integral values', async function() {
        expect(response.entity.values[0]).toBeCloseTo(122.2,3);
        expect(response.entity.values[1]).toBeCloseTo(222.2,3);
        expect(response.entity.values[2]).toBeCloseTo(322.2,3);
        expect(response.entity.values[3]).toBeCloseTo(422.2,3);
        expect(response.entity.values[4]).toBeCloseTo(522.2,3);
    });
    
    it ('The data column in the csv should comprise all integral values', async function() {
        expect(response.entity.csv).toEqual(
        		"index, temp_float32"	+ "\n" +
        		"0, 122.200"			+ "\n" +
        		"1, 222.200"			+ "\n" +
        		"2, 322.200"			+ "\n" +
        		"3, 422.200"			+ "\n" +
    			"4, 522.200"			+ "\n"
		);
    });
});

