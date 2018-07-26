#! /usr/bin/env python3

from flask import Flask, jsonify, request

TIMESERIES_SERVICE_BASE = '/timeseries-service/api/v1'

app = Flask(__name__)
TIMESERIES_SERVICE_BASE = '/timeseries-service/api/v1'

@app.route(TIMESERIES_SERVICE_BASE + '/status')
def get_status():
    return jsonify({'name': 'SKOPE Timeseries Service'})

app.run(port=8001)