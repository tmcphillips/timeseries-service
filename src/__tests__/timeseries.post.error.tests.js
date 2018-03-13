const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );

describe("When a values POST request is missing the datasetId property", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/values',
		    entity: {
		    	variableName: 'temp',
		    	boundaryGeometry: {
		    		type: 'Point',
		    		coordinates: [-123, 45]
		    	},
		    	range: {
		    		start: 0,
		    		end: 4
		    	}
		    }
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });

    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that datasetId property is not present', async function() {
        expect(response.entity.message).toBe("Required property 'datasetId' is not present");
    });    
});

describe("When a values POST request is missing the variableName property", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/values',
		    entity: {
		    	datasetId: '5x5x5',
		    	boundaryGeometry: {
		    		type: 'Point',
		    		coordinates: [-123, 45]
		    	},
		    	range: {
		    		start: 0,
		    		end: 4
		    	}
		    }
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });

    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that variableName property is not present', async function() {
        expect(response.entity.message).toBe("Required property 'variableName' is not present");
    });    
});

describe("When a values POST request is missing the boundaryGeometry property", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/values',
		    entity: {
		    	datasetId: '5x5x5',
		    	variableName: 'temp',
		    	range: {
		    		start: 0,
		    		end: 4
		    	}
		    }
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });

    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that variableName property is not present', async function() {
        expect(response.entity.message).toBe("Required property 'boundaryGeometry' is not present");
    });    
});

describe("When a values POST request is missing the boundaryGeometry.type property", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/values',
		    entity: {
		    	datasetId: '5x5x5',
		    	variableName: 'temp',
		    	boundaryGeometry: {
		    		coordinates: [-123, 45]
		    	},		    	
		    	range: {
		    		start: 0,
		    		end: 4
		    	}
		    }
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });

    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that boundaryGeometry.type property is not present', async function() {
        expect(response.entity.message).toBe("Required property 'boundaryGeometry.type' is not present");
    });    
});

describe("When a values POST request is missing the boundaryGeometry.coordinates property", async () => {
    
	var response;
	
	beforeAll(async () => {
		response = await callRESTService({
		    method: 'POST',
		    path: timeseriesServiceBase + '/values',
		    entity: {
		    	datasetId: '5x5x5',
		    	variableName: 'temp',
		    	boundaryGeometry: {
		    		type: 'Point'
		    	},		    	
		    	range: {
		    		start: 0,
		    		end: 4
		    	}
		    }
		});
    });

    it ('HTTP response status code should be 400', async function() {
        expect(response.status.code).toBe(400);
    });

    it ('Error summary should be bad request', async function() {
        expect(response.entity.error).toBe("Bad Request");
    });

    it ('Error message should be that boundaryGeometry.coordintes property is not present', async function() {
        expect(response.entity.message).toBe("Required property 'boundaryGeometry.coordinates' is not present");
    });    
});
