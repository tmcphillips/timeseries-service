
import osr
import numpy
from osgeo import gdal

filename = "5x5x5_temp_float32_uncertainty.tif"
format = 'GTiff'
nodata = -1
originX = -123
originY = 45
pixelWidth = 1
pixelHeight = 1
cols = 5
rows = 5
bands = 5

driver = gdal.GetDriverByName('GTiff')
dataset = driver.Create(filename, cols, rows, bands, gdal.GDT_Float32)
dataset.SetGeoTransform((originX, pixelWidth, 0, originY, 0, pixelHeight))

srs = osr.SpatialReference()
srs.SetWellKnownGeogCS("WGS84")
dataset.SetProjection(srs.ExportToWkt())


array1 = numpy.array([[10, 10.1, 10.2, 10.3, 10.4],
                      [11, 11.1, 11.2, 11.3, 11.4],
                      [12, 12.1, 12.2, 12.3, 12.4],
                      [13, 13.1, 13.2, 13.3, 13.4],
                      [14, 14.1, 14.2, 14.3, 14.4],
                    ])

array2 = numpy.array([[20, 20.1, 20.2, 20.3, 20.4],
                      [21, 21.1, 21.2, 21.3, 21.4],
                      [22, 22.1, 22.2, 22.3, 22.4],
                      [23, 23.1, 23.2, 23.3, 23.4],
                      [24, 24.1, 24.2, 24.3, 24.4]
                     ])

array3 = numpy.array([[30, 30.1, 30.2, 30.3, 30.4],
                      [31, 31.1, 31.2, 31.3, 31.4],
                      [32, 32.1, 32.2, 32.3, 32.4],
                      [33, 33.1, 33.2, 33.3, 23.4],
                      [34, 34.1, 34.2, 34.3, 34.4]
                     ])

array4 = numpy.array([[40, 40.1, 40.2, 40.3, 40.4],
                      [41, 41.1, 41.2, 41.3, 41.4],
                      [42, 42.1, 42.2, 42.3, 42.4],
                      [43, 43.1, 43.2, 43.3, 43.4],
                      [44, 44.1, 44.2, 44.3, 44.4]
                     ])

array5 = numpy.array([[50, 50.1, 50.2, 50.3, 50.4],
                      [51, 51.1, 51.2, 51.3, 51.4],
                      [52, 52.1, 52.2, 52.3, 52.4],
                      [53, 53.1, 53.2, 53.3, 53.4],
                      [54, 54.1, 54.2, 54.3, 54.4]
                     ])



band = dataset.GetRasterBand(1)
band.WriteArray(array1)
band.SetNoDataValue(nodata)
band.FlushCache()

band = dataset.GetRasterBand(2)
band.WriteArray(array2)
band.SetNoDataValue(nodata)
band.FlushCache()

band = dataset.GetRasterBand(3)
band.WriteArray(array3)
band.SetNoDataValue(nodata)
band.FlushCache()

band = dataset.GetRasterBand(4)
band.WriteArray(array4)
band.SetNoDataValue(nodata)
band.FlushCache()

band = dataset.GetRasterBand(5)
band.WriteArray(array5)
band.SetNoDataValue(nodata)
band.FlushCache()
