const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );

describe("When a GET request selects a pixel with all nodata values and sets nodata parameter to ignore", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp?longitude=-119.0&latitude=48.0&start=0&end=4&csv=true&array=true&nodata=ignore'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });

    it ('Values should contain the nodata values', async function() {
        expect(response.entity.values).toEqual([65535,65535,65535,65535,65535]);
	});

    it ('The nodata values should be ignored', async function() {
        expect(response.entity.nodata).toEqual(false);
	});

    it ('Csv should contain the nodata values', async function() {
        expect(response.entity.csv).toEqual( 
        		"index, temp"	+ "\n" +
        		"0, 65535"		+ "\n" +
        		"1, 65535"		+ "\n" +
        		"2, 65535"		+ "\n" +
        		"3, 65535"		+ "\n" +
    			"4, 65535"		+ "\n"
		);
    });
})


describe("When a GET request selects a pixel with one nodata value and sets nodata parameter to ignore", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp?longitude=-119.0&latitude=47.0&start=0&end=4&csv=true&array=true&nodata=ignore'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
    
    it ('Values should contain the nodata value', async function() {
        expect(response.entity.values).toEqual([124,224,65535,424,524]);
	});
    
    it ('The nodata values should be ignored', async function() {
        expect(response.entity.nodata).toEqual(false);
	});

    it ('Csv should contain the nodata value', async function() {
        expect(response.entity.csv).toEqual( 
        		"index, temp"	+ "\n" +
        		"0, 124"		+ "\n" +
        		"1, 224"		+ "\n" +
        		"2, 65535"		+ "\n" +
        		"3, 424"		+ "\n" +
    			"4, 524"		+ "\n"
		);
    });

})
