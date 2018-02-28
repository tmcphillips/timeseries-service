const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceBase = 'http://localhost:8001/timeseries-service/api/v1';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );

describe("When timeseries service is running ", () => {
    
    it ('GET /timeseries should return a timeseries', async function() {
        var response = await callRESTService({
            method: 'GET',
            path: timeseriesServiceBase + '/timeseries?data=dummy&var=temp_5x5x5&lng=-123&lat=45&start=1&end=5'
        });
        expect(response.status.code).toBe(200);
        expect(response.entity.values).toEqual(['100','200','300','400','500']);
    });

});