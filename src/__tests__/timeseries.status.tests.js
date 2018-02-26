const rest = require('rest');
const mime = require('rest/interceptor/mime');

const timeseriesServiceUrl = 'http://localhost:8001';

const callRESTService =  rest.wrap(mime, { mime: 'application/json' } );
  
describe("When timeseries service is running", () => {
    
    it ('GET / should return service name', async function() {
        var response = await callRESTService({
            method: 'GET',
            path: timeseriesServiceUrl
        });
        expect(response.status.code).toBe(200);
        expect(response.entity.name).toBe("SKOPE Timeseries Service");
    });

});
