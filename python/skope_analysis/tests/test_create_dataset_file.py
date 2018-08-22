import pytest
import os
from osgeo import gdal
import skope_analysis

@pytest.fixture(scope='module')
def datafile_directory(tmpdir_factory):
    return tmpdir_factory.mktemp('test_directory')

@pytest.fixture(scope='module')
def path_to_datafile(datafile_directory):
    filepath = str(datafile_directory) + 'test.tif'
    skope_analysis.create_dataset_file(
        filename     = filepath,
        format       = 'GTiff',
        pixel_type   = gdal.GDT_Float32, 
        rows         = 5, 
        cols         = 5, 
        bands        = 5,
        origin_x     = -123,
        origin_y     = 45,
        pixel_width  = 1, 
        pixel_height = 1,
        coordinate_system='WGS84'
    )
    return filepath

def test_datafile_exists(path_to_datafile):
    assert os.path.isfile(path_to_datafile) 
    