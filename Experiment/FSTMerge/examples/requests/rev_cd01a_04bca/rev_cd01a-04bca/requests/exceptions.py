
"""
requests.exceptions
~~~~~~~~~~~~~~~

"""

class  RequestException (Exception) :
	"""There was an ambiguous exception that occured while handling your
    request."""
	"""There was an ambiguous exception that occured while handling your
    request."""

class  Timeout (RequestException) :
	"""The request timed out."""
	"""The request timed out."""

class  URLRequired (RequestException) :
	"""A valid URL is required to make a request."""
	"""A valid URL is required to make a request."""

class  TooManyRedirects (RequestException) :
	"""Too many redirects."""
	"""Too many redirects."""

class  AuthenticationError (RequestException) :
	"""The authentication credentials provided were invalid."""
	"""The authentication credentials provided were invalid."""

class  InvalidMethod (RequestException) :
	"""An inappropriate method was attempted."""
	"""An inappropriate method was attempted."""

