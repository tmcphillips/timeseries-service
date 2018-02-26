const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1/';

// const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );
const callRESTService = rest

describe("When timeseries service is running", () => {
    
    it ('GET / should return service name', async function() {
        var response = await callRESTService({
            method: 'GET',
            path: timeseriesServiceBase
        });
        expect(response.status.code).toBe(200);
        expect(response.entity).toBe("SKOPE Timeseries Service");
    });

});
