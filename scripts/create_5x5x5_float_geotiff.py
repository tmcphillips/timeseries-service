
import osr
import numpy
from osgeo import gdal

filename = "5x5x5_temp_float32.tif"
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


array1 = numpy.array([[100, 101.1, 102.2, 103.3, 104.4],
                      [110, 111.1, 112.2, 113.3, 114.4],
                      [120, 121.1, 122.2, 123.3, 124.4],
                      [130, 131.1, 132.2, 133.3, nodata],
                      [140, 141.1, 142.2, 143.3, 144.4],
                    ])

array2 = numpy.array([[200, 201.1, 202.2, 203.3, 204.4],
                      [210, 211.1, 212.2, 213.3, 214.4],
                      [220, 221.1, 222.2, 223.3, 224.4],
                      [230, 231.1, 232.2, 233.3, nodata],
                      [240, 241.1, 242.2, 243.3, 244.4]
                     ])

array3 = numpy.array([[300, 301.1, 302.2, 303.3, 304.4],
                      [310, 311.1, 312.2, 313.3, 314.4],
                      [320, 321.1, 322.2, 323.3, nodata],
                      [330, 331.1, 332.2, 333.3, nodata],
                      [340, 341.1, 342.2, 343.3, 344.4]
                     ])

array4 = numpy.array([[400, 401.1, 402.2, 403.3, 404.4],
                      [410, 411.1, 412.2, 413.3, 414.4],
                      [420, 421.1, 422.2, 423.3, 424.4],
                      [430, 431.1, 432.2, 433.3, nodata],
                      [440, 441.1, 442.2, 443.3, 444.4]
                     ])

array5 = numpy.array([[500, 501.1, 502.2, 503.3, 504.4],
                      [510, 511.1, 512.2, 513.3, 514.4],
                      [520, 521.1, 522.2, 523.3, 524.4],
                      [530, 531.1, 532.2, 533.3, nodata],
                      [540, 541.1, 542.2, 543.3, 544.4]
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
