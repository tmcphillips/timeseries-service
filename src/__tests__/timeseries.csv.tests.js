const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );

describe("When a GET request selects first pixel of each band in 5x5x5 data cube in csv but not array formats", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp?longitude=-123.0&latitude=45.0&start=0&end=4&csv=true&array=false'
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
        		"index, temp"	+ "\n" +
        		"0, 100"		+ "\n" +
        		"1, 200"		+ "\n" +
        		"2, 300"		+ "\n" +
        		"3, 400"		+ "\n" +
    			"4, 500"		+ "\n"
		);
    });

})

describe("When a GET request selects first pixel of each band in 5x5x5 data cube in array but not csv formats", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp?longitude=-123.0&latitude=45.0&start=0&end=4&csv=false&array=true'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
    
    it ('Values should be present', async function() {
        expect(response.entity.values).toEqual( [100,200,300,400,500] );
    });
    
    it ('Csv should be absent', async function() {
        expect(response.entity.csv).toEqual( null );
    });

})


describe("When a POST request selects first pixel of each band in 5x5x5 data cube in csv format but not array format", async () => {
    
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
		    	csv: true,
		    	array: false
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
        		"index, temp"	+ "\n" +
        		"0, 100"		+ "\n" +
        		"1, 200"		+ "\n" +
        		"2, 300"		+ "\n" +
        		"3, 400"		+ "\n" +
    			"4, 500"		+ "\n"
		);
    });

});


describe("When a POST request selects first pixel of each band in 5x5x5 data cube in array format but not csv format", async () => {
    
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
		    	csv: false,
		    	array: true
		    }
		}); 
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
    
    it ('Values should be present', async function() {
        expect(response.entity.values).toEqual( [100,200,300,400,500] );
    });
    
    it ('Csv should be present', async function() {
        expect(response.entity.csv).toEqual( null );
    });
});


describe("When a GET request selects first pixel of each band in 5x5x5 data cube in units of years", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp?longitude=-123.0&latitude=45.0&start=1&end=5&csv=true&timeResolution=year'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
        
    it ('Csv should be present', async function() {
        expect(response.entity.csv).toEqual( 
        		"year, temp"	+ "\n" +
        		"1, 100"		+ "\n" +
        		"2, 200"		+ "\n" +
        		"3, 300"		+ "\n" +
        		"4, 400"		+ "\n" +
    			"5, 500"		+ "\n"
		);
    });
})

describe("When a GET request selects first pixel of each band in 5x5x5 data cube in units of years without specyifying start, end, or timezero", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp?longitude=-123.0&latitude=45.0&csv=true&timeResolution=year'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
        
    it ('Csv should be present', async function() {
        expect(response.entity.csv).toEqual( 
        		"year, temp"	+ "\n" +
        		"1, 100"		+ "\n" +
        		"2, 200"		+ "\n" +
        		"3, 300"		+ "\n" +
        		"4, 400"		+ "\n" +
    			"5, 500"		+ "\n"
		);
    });
})

describe("When a GET request selects first pixel of each band in 5x5x5 data cube in units of years without specyifying start or end but with timezero", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp?longitude=-123.0&latitude=45.0&csv=true&timeResolution=year&timeZero=501'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
        
    it ('Csv should be present', async function() {
        expect(response.entity.csv).toEqual( 
        		"year, temp"	+ "\n" +
        		"501, 100"		+ "\n" +
        		"502, 200"		+ "\n" +
        		"503, 300"		+ "\n" +
        		"504, 400"		+ "\n" +
    			"505, 500"		+ "\n"
		);
    });
})

describe("When a GET request selects first pixel of 5x5x5 data cube in units of years and specifies end beyond dataset coverage", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp?longitude=-123.0&latitude=45.0&csv=true&timeResolution=year&timeZero=501&start=503&end=510'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
        
    it ('Csv should be present', async function() {
        expect(response.entity.csv).toEqual( 
        		"year, temp"	+ "\n" +
        		"503, 300"		+ "\n" +
        		"504, 400"		+ "\n" +
    			"505, 500"		+ "\n"
		);
    });
})

describe("When a GET request selects first pixel of 5x5x5 data cube in units of years and specifies only end", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'GET',
		    path: timeseriesServiceBase + '/timeseries/5x5x5/temp?longitude=-123.0&latitude=45.0&csv=true&timeResolution=year&timeZero=501&end=503'
		});
    });

    it ('HTTP response status code should be 200 - success', async function() {
        expect(response.status.code).toBe(200);
    });
        
    it ('Csv should be present', async function() {
        expect(response.entity.csv).toEqual( 
        		"year, temp"	+ "\n" +
        		"501, 100"		+ "\n" +
        		"502, 200"		+ "\n" +
    			"503, 300"		+ "\n"
		);
    });
})