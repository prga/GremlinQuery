"""
Scrapy - a screen scraping framework written in Python
"""


~~FSTMerge~~ version_info = (0, 11, 0, 'dev') ##FSTMerge## version_info = (0, 10, 2, '') ##FSTMerge## version_info = (0, 10, 3, 'dev')

~~FSTMerge~~ __version__ = "0.11" ##FSTMerge## __version__ = "0.10.2" ##FSTMerge## __version__ = "0.10.3"


import sys, os, warnings


if sys.version_info < (2,5):

    print "Scrapy %s requires Python 2.5 or above" % __version__

    sys.exit(1)



warnings.filterwarnings('ignore', category=DeprecationWarning, module='twisted')


from twisted.python.zippath import ZipPath

ZipPath.setContent = lambda x, y: None


from scrapy.xlib import twisted_250_monkeypatches, urlparse_monkeypatches


optional_features = set()


try:

    import OpenSSL

except ImportError:

    pass

else:

    optional_features.add('ssl')



try:

    import boto

except ImportError:

    pass

else:

    optional_features.add('boto')



if sys.version_info < (2,5):

    print "Scrapy %s requires Python 2.5 or above" % __version__

    sys.exit(1)




try:

    import OpenSSL

except ImportError:

    pass

else:

    optional_features.add('ssl')



try:

    import boto

except ImportError:

    pass

else:

    optional_features.add('boto')


