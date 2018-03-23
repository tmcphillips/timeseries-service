const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );

describe("When a csv POST request selects first pixel of each band in 5x5x5 data cube", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/timeseries',
		    entity: {
		    	datasetId: '5x5x5',
		    	variableName: 'temp',
		    	boundaryGeometry: {
		    		type: 'Point',
		    		coordinates: [-123, 45]
		    	},
		    	range: {
		    		start: 0,
		    		end: 4
		    	},
		    	format: 'csv'
		    }
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
    
    it ('Values should be absent', async function() {
        expect(response.entity.values).toEqual( null );
    });
    
    it ('Csv should be present', async function() {
        expect(response.entity.csv).toEqual( 
        		"Year, temp"	+ "\n" +
        		"1, 100"		+ "\n" +
        		"2, 200"		+ "\n" +
        		"3, 300"		+ "\n" +
        		"4, 400"		+ "\n" +
    			"5, 500"		+ "\n"
		);
    });

});
