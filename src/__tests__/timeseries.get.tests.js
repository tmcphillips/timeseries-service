const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );

describe("When a values request selects first pixel of each band in 5x5x5 data cube", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/values?dataset=5x5x5&variable=temp&lng=-123.0&lat=45.0&start=0&end=4'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });

    it ('Dataset id should match that in the request', async function() {
        expect(response.entity.datasetId).toBe('5x5x5');
    });

	it ('Variable name should match that in the request', async function() {
        expect(response.entity.variableName).toBe('temp');
    });

	it ('Boundary geometry type should be point', async function() {
        expect(response.entity.boundaryGeometry.type).toBe('Point');
    });

    it ('Boundary geometry coordinates should be array with requested lat and lng ', async function() {
        expect(response.entity.boundaryGeometry.coordinates).toEqual( [-123, 45] );
    });

    it ('Series range start and end should match the request', async function() {
        expect(response.entity.range.start).toEqual( 0 );
        expect(response.entity.range.end).toEqual( 4 );
    });
    
    it ('Values should be an array with one element for the first pixel of each band', async function() {
        expect(response.entity.values).toEqual( [100,200,300,400,500] );
    });

});

describe("When a values request selects first pixel of first band in 5x5x5 data cube", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/values?dataset=5x5x5&variable=temp&lng=-123.0&lat=45.0&start=0&end=0'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });

    it ('Series range start and end should match the request', async function() {
        expect(response.entity.range.start).toEqual( 0 );
        expect(response.entity.range.end).toEqual( 0 );
    });
    
    it ('Values should be an array with one element for the first pixel of each band', async function() {
        expect(response.entity.values).toEqual( [100] );
    });

});

describe("When a values request selects last pixel of each band in 5x5x5 data cube", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/values?dataset=5x5x5&variable=temp&lng=-119.0&lat=49.0&start=0&end=4'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });

    it ('Series range start and end should match the request', async function() {
        expect(response.entity.range.start).toEqual( 0 );
        expect(response.entity.range.end).toEqual( 4 );
    });

    it ('Values should be an array with one element for the first pixel of each band', async function() {
        expect(response.entity.values).toEqual( [144,244,344,444,544] );
    });

});

describe("When a values request selects last pixel of last band in 5x5x5 data cube", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/values?dataset=5x5x5&variable=temp&lng=-119.0&lat=49.0&start=4&end=4'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });

    it ('Series range start and end should match the request', async function() {
        expect(response.entity.range.start).toEqual( 4 );
        expect(response.entity.range.end).toEqual( 4 );
    });

    it ('Values should be an array with one element for the first pixel of each band', async function() {
        expect(response.entity.values).toEqual( [544] );
    });

});