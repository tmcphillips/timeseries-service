SKOPE Time-Series Service
=========================

This repository contains the source code for the microservice that produces
the time series displayed graphically in the SKOPE web application.  The
service is implemented in Java using the Spring Boot framework and is
REST-like.  The service invokes GDAL applications on the service host and
uses GDAL-compatible data files.  A docker image is provided for running the
microservice on any platform that supports Docker without having to install
Java, GDAL, or any other application or framework on the host.

This README describes how to configure and run the service; how to invoke the
service using REST calls; and how to build the software, run the service
in a debugger, and perform a set of automated tests on the service.

