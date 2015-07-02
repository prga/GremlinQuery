# -*- coding: utf-8 -*-

#   __
#  /__)  _  _     _   _ _/   _
# / (   (- (/ (/ (- _)  /  _)
#          /

"""
requests
~~~~~~~~

:copyright: (c) 2011 by Kenneth Reitz.
:license: ISC, see LICENSE for more details.

"""

__title__ = 'requests'
__version__ = '0.7.6'
__build__ = 0x000706
__author__ = 'Kenneth Reitz'
__license__ = 'ISC'
__copyright__ = 'Copyright 2011 Kenneth Reitz'


from . import utils
from .models import Request, Response
from .api import request, get, options, head, post, patch, put, delete
from .sessions import session
from .status_codes import codes
from .exceptions import (
    RequestException, AuthenticationError, Timeout, URLRequired,
    TooManyRedirects
)