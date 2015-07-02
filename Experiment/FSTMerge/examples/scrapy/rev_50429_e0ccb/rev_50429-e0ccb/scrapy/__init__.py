"""
Scrapy - a screen scraping framework written in Python
"""


~~FSTMerge~~ version_info = (0, 7, 0, 'final', 0) ##FSTMerge## version_info = (0, 7, 0, 'candidate', 0) ##FSTMerge## version_info = (0, 8, 0, '', 0)

~~FSTMerge~~ __version__ = "0.7" ##FSTMerge## __version__ = "0.7.0-rc1" ##FSTMerge## __version__ = "0.8.0-dev"


import sys, os


if sys.version_info < (2,5):

    print "Scrapy %s requires Python 2.5 or above" % __version__

    sys.exit(1)



from scrapy.xlib.patches import apply_patches

apply_patches()


optional_features = set()


try:

    import OpenSSL

except ImportError:

    pass

else:

    optional_features.add('ssl')



if sys.version_info < (2,5):

    print "Scrapy %s requires Python 2.5 or above" % __version__

    sys.exit(1)




try:

    import OpenSSL

except ImportError:

    pass

else:

    optional_features.add('ssl')


