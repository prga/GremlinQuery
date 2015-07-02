"""
Scrapy - a screen scraping framework written in Python
"""


~~FSTMerge~~ version_info = (0, 10, 0, 'dev') ##FSTMerge## version_info = (0, 9, 0, 'rc1') ##FSTMerge## version_info = (0, 9, 0, '')

~~FSTMerge~~ __version__ = "0.10-dev" ##FSTMerge## __version__ = "0.9-rc1" ##FSTMerge## __version__ = "0.9"


import sys, os, warnings


if sys.version_info < (2,5):

    print "Scrapy %s requires Python 2.5 or above" % __version__

    sys.exit(1)



warnings.filterwarnings('ignore', category=DeprecationWarning, module='twisted')


from scrapy.xlib import twisted_250_monkeypatches


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


