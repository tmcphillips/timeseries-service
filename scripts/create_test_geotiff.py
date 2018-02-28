
import osr
import numpy
from osgeo import gdal

filename = "dummy_temp_5x5x5.tif"
format = 'GTiff'
nodata_value = None
originX = -123
originY = 45
pixelWidth = 1
pixelHeight = 1
cols = 5
rows = 5
bands = 5

driver = gdal.GetDriverByName('GTiff')
dataset = driver.Create(filename, cols, rows, bands, gdal.GDT_UInt16)
dataset.SetGeoTransform((originX, pixelWidth, 0, originY, 0, pixelHeight))

srs = osr.SpatialReference()
srs.SetWellKnownGeogCS("WGS84")
dataset.SetProjection(srs.ExportToWkt())


array1 = numpy.array([[100, 101, 102, 103, 104],
                      [110, 111, 112, 113, 114],
                      [120, 121, 122, 123, 124],
                      [130, 131, 132, 133, 134],
                      [140, 141, 142, 143, 144],
                    ])

array2 = numpy.array([[200, 201, 202, 203, 204],
                      [210, 211, 212, 213, 214],
                      [220, 221, 222, 223, 224],
                      [230, 231, 232, 233, 234],
                      [240, 241, 242, 243, 244]
                     ])

array3 = numpy.array([[300, 301, 302, 303, 304],
                      [310, 311, 312, 313, 314],
                      [320, 321, 322, 323, 324],
                      [330, 331, 332, 333, 334],
                      [340, 341, 342, 343, 344]
                     ])

array4 = numpy.array([[400, 401, 402, 403, 404],
                      [410, 411, 412, 413, 414],
                      [420, 421, 422, 423, 424],
                      [430, 431, 432, 433, 434],
                      [440, 441, 442, 443, 444]
                     ])

array5 = numpy.array([[500, 501, 502, 503, 504],
                      [510, 511, 512, 513, 514],
                      [520, 521, 522, 523, 524],
                      [530, 531, 532, 533, 534],
                      [540, 541, 542, 543, 544]
                     ])


band = dataset.GetRasterBand(1)
band.WriteArray(array1)
band.FlushCache()

band = dataset.GetRasterBand(2)
band.WriteArray(array2)
band.FlushCache()

band = dataset.GetRasterBand(3)
band.WriteArray(array3)
band.FlushCache()

band = dataset.GetRasterBand(4)
band.WriteArray(array4)
band.FlushCache()

band = dataset.GetRasterBand(5)
band.WriteArray(array5)
band.FlushCache()
