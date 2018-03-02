from openjdk:8-slim

ARG DEBIAN_FRONTEND=noninteractive

ENV DOCKER_IMAGE_NAME openskope/timeseries-service

RUN echo '***** update apt packages and install utilities *****'                                    \
 && apt-get -y update                                                                               \
 && apt-get -y install -y apt-utils                                                                 \
 && apt-get -y install wget makepasswd                                                              \
                                                                                                    \
 && echo '***** create the skope group *****'                                                       \
 && groupadd skope --gid 1000                                                                       \
                                                                                                    \
 && echo '***** create the skope user *****'                                                        \
 && useradd skope --uid 1000                                                                        \
                  --gid skope                                                                       \
                  --shell /bin/bash                                                                 \
                  --create-home                                                                     \
                  --password `echo skope | makepasswd --crypt-md5 --clearfrom - | cut -b11-`        \
                                                                                                    \
 && echo '***** create the workspace directory *****'                                               \
 && mkdir /workspace                                                                                \
 && chown skope.skope /workspace

VOLUME ["/workspace"]
WORKDIR /workspace

RUN echo '***** install development packages required to build service *****'                       \
 && apt-get -y install git maven                                                                    

RUN echo '***** Install python2, pip *****'                                                         \
 && apt-get -y install python python-pip                                                            \
 && pip2 install --upgrade pip                                                                      \
                                                                                                    \
 && echo '***** install numpy package for python2 *****'                                            \
 && pip2 install numpy

RUN echo '***** download source package for GDAL 2.1.2 *****'                                       \
 && mkdir -p /tmp/builds                                                                            \
 && cd /tmp/builds                                                                                  \
 && wget http://download.osgeo.org/gdal/2.1.2/gdal-2.1.2.tar.gz                                     \
 && gunzip gdal-2.1.2.tar.gz                                                                        \
 && tar -xvvf gdal-2.1.2.tar                                                                        \
                                                                                                    \
 && echo '***** configure and build GDAL with Python2 bindings ******'                              \
 && cd /tmp/builds/gdal-2.1.2                                                                       \
 && mkdir -p /opt/gdal                                                                              \
 && ./configure --prefix /opt/gdal/gdal-2.1.2 --with-python=$(which python2)                        \
 && make install                                                                                    \
                                                                                                    \
 && echo '**** clean up GDAL build directories *****'                                               \
 && rm -rf /tmp/builds

ENV LD_LIBRARY_PATH=/opt/gdal/gdal-2.1.2/lib/
ENV PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/opt/gdal/gdal-2.1.2/bin/
ENV PYTHONPATH=/opt/gdal/gdal-2.1.2/lib/python2.7/site-packages/

USER skope
WORKDIR /home/skope

RUN echo '***** Clone timeseries-service and build the executable JAR *****'                        \
 && cd ~skope                                                                                       \
 && git clone https://github.com/openskope/timeseries-service.git                                   \
 && cd timeseries-service                                                                           \
 && mvn install

RUN echo '***** Create scripts in ~skope/bin directory *****'                                       \
 && echo "#!/bin/bash" > start                                                                      \
 && echo "java -jar ~/timeseries-service/app/target/time-series-service-app-0.1.5.jar" >> start     \
 && chmod +rx start                                                                                 \
 && echo "export PATH=$PATH:/home/skope" >> ~/.bashrc

ENV PATH=$PATH:/home/skope

CMD echo "Usage: docker run openskope/timeseries"