const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );

describe("When a values GET request is missing the dataset parameter", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/values?variable=temp&lng=-123.0&lat=45.0&start=0&end=4'
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });

    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that dataset parameter is not present', async function() {
        expect(response.entity.message).toBe("Required String parameter 'dataset' is not present");
    });    
});

describe("When a values GET request is missing the variable parameter", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/values?dataset=5x5x5&lng=-123.0&lat=45.0&start=0&end=4'
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });

    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that variable parameter is not present', async function() {
        expect(response.entity.message).toBe("Required String parameter 'variable' is not present");
    });    
});

describe("When a values GET request is missing the lng parameter", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/values?dataset=5x5x5&variable=temp&lat=45.0&start=0&end=4'
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });

    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that lng parameter is not present', async function() {
        expect(response.entity.message).toBe("Required String parameter 'lng' is not present");
    });    
});

describe("When a values GET request is missing the lat parameter", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/values?dataset=5x5x5&variable=temp&lng=-123.0&start=0&end=4'
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });

    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that lng parameter is not present', async function() {
        expect(response.entity.message).toBe("Required String parameter 'lat' is not present");
    });    
});