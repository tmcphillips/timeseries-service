const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );

describe("When a GET request selects from a float32 datafile a coordinate with all integral values", async () => {
    
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

