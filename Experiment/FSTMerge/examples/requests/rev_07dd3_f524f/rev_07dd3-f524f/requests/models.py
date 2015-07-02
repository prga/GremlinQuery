
"""
requests.models
~~~~~~~~~~~~~~~

This module contains the primary classes that power Requests.
"""


import urllib

import zlib

from Cookie import SimpleCookie

from urlparse import urlparse, urlunparse, urljoin

from weakref import ref


from .packages import urllib3

from .packages.urllib3.filepost import encode_multipart_formdata


from ._config import get_config

from .structures import CaseInsensitiveDict

from .utils import *

from .status_codes import codes

from .exceptions import RequestException, Timeout, URLRequired, TooManyRedirects

from .packages.urllib3.poolmanager import PoolManager



REDIRECT_STATI = (codes.moved, codes.found, codes.other, codes.temporary_moved)

class  Request (object) :
	"""The :class:`Request <Request>` object. It carries out all functionality of
    Requests. Recommended interface is with the Requests functions.
    """
	
    def __init__(self,
        url=None, headers=dict(), files=None, method=None, data=dict(),
        params=dict(), auth=None, cookies=None, timeout=None, redirect=False,
        allow_redirects=False, proxies=None, config=None, hooks=None,
        _pools=None):


        if cookies is None:

            cookies = {}



                        self.timeout = timeout


                self.url = url


                self.headers = headers or {}


                self.files = files or {}


                self.method = method


                        self.data = None


                        self.params = None


                        self.redirect = redirect


                self.allow_redirects = allow_redirects


                self.proxies = proxies


        self.config = get_config(config)


        self.data = data

        self._enc_data = encode_params(data)

        self.params = params

        self._enc_params = encode_params(params)


                        self.response = Response()


                self.auth = auth


                self.cookies = cookies


                self.sent = False


        
        if self.config.get('accept_gzip'):

            self.headers.update({'Accept-Encoding': 'gzip'})


        if headers:

            headers = CaseInsensitiveDict(self.headers)

        else:

            headers = CaseInsensitiveDict()


        for (k, v) in self.config.get('base_headers').items():

            if k not in headers:

                headers[k] = v


        self.headers = headers


        self.hooks = hooks

        self._pools = _pools


    
	def __repr__(self):

        return '<Request [%s]>' % (self.method)


    
	def _checks(self):

        """Deterministic checks for consistency."""


        if not self.url:

            raise URLRequired


    
	def _build_response(self, resp, is_error=False):

        """Build internal :class:`Response <Response>` object
        from given response.
        """


        def build(resp):


            response = Response()


                        response.config = self.config


                        response.status_code = getattr(resp, 'status', None)


                        response.headers = CaseInsensitiveDict(getattr(resp, 'headers', None))


                        cookies = self.cookies or dict()


                        if 'set-cookie' in response.headers:

                cookie_header = response.headers['set-cookie']


                c = SimpleCookie()

                c.load(cookie_header)


                for k,v in c.items():

                    cookies.update({k: v.value})


                        response.cookies = cookies


                        response.raw = resp


                        if is_error:

                response.error = resp


            return response


                history = []


                r = build(resp)

        self.cookies.update(r.cookies)


                r._response = resp


                if r.status_code in REDIRECT_STATI and not self.redirect:


            while (
                
                ('location' in r.headers) and

                
                ((r.status_code is codes.see_other) or

                
                (self.allow_redirects))
            ):


                                
                                if len(history) >= self.config.get('max_redirects'):

                    raise TooManyRedirects()


                                history.append(r)


                url = cleanup_url(r.headers['location'], parent_url=self.url)


                                                if r.status_code is codes.see_other:

                    method = 'GET'

                else:

                    method = self.method


                                request = Request(
                    url=url,
                    headers=self.headers,
                    files=self.files,
                    method=method,
                    data=self.data,
                    
                    params=None,
                    auth=self.auth,
                    cookies=self.cookies,
                    _pools=self._pools,
                    config=self.config,

                    
                    redirect=True
                )


                                request.send()

                r = request.response


                self.cookies.update(r.cookies or {})


                        r.history = history


                self.response = r


                self.response.request = self



    
	def send(self, anyway=False):

        """Sends the HTTP Request. Populates `Request.response`.

        Returns True if everything went according to plan.
        """


                self._checks()


                url = build_url(self.url, self.params)


                body = None

        content_type = None


                if self.files:

            if not isinstance(self.data, basestring):

                fields = self.data.copy()

                for (k, v) in self.files.items():

                    fields.update({k: (k, v.read())})

                (body, content_type) = encode_multipart_formdata(fields)


                if self.data and (not body):

            if isinstance(self.data, basestring):

                body = self.data

            else:

                body = encode_params(self.data)

                content_type = 'application/x-www-form-urlencoded'


        
                if (content_type) and (not 'content-type' in self.headers):

            self.headers['Content-Type'] = content_type


                if (anyway) or (not self.sent):


            try:

                                if not self._pools:


                                        pools = PoolManager(
                        num_pools=self.config.get('max_connections'),
                        maxsize=1,
                        timeout=self.timeout
                    )


                                        connection = pools.connection_from_url(url)


                                        do_block = False

                else:

                                        connection = self._pools.connection_from_url(url)


                                        pools = self._pools


                                        do_block = False


                if self.cookies:

                                        if 'cookie' not in self.headers:


                                                c = SimpleCookie()

                        c.load(self.cookies)


                                                cookie_header = c.output(header='').strip()


                                                self.headers['Cookie'] = cookie_header


                                r = connection.urlopen(
                    method=self.method,
                    url=url,
                    body=body,
                    headers=self.headers,
                    redirect=False,
                    assert_same_host=False,
                    preload_content=do_block,
                    decode_content=False
                )


                                if self.config.get('keep_alive') and pools:

                    self._pools = pools



                        except Exception, why:

                print why.__dict__

                                                
                                print 'FUCK'

                print why


            else:

                                self._build_response(r)

                self.response.ok = True


        self.sent = self.response.ok


        return self.sent





class  Response (object) :
	"""The core :class:`Response <Response>` object.


    All :class:`Request <Request>` objects contain a :class:`response
    <Response>` attribute, which is an instance of this class.
    """
	
    def __init__(self):


        self._content = None

        self._content_consumed = False


                self.status_code = None


                                self.headers = CaseInsensitiveDict()


                self.raw = None


                self.ok = False


                self.error = None


                                self.history = []


                self.request = None


                self.cookies = None


                self.config = None


    
	def __repr__(self):

        return '<Response [%s]>' % (self.status_code)


    
	def __nonzero__(self):

        """Returns true if :attr:`status_code` is 'OK'."""


        return not self.error


    
	def iter_content(self, chunk_size=10 * 1024, decode_unicode=None):

        """Iterates over the response data.  This avoids reading the content
        at once into memory for large responses.  The chunk size is the number
        of bytes it should read into memory.  This is not necessarily the
        length of each item returned as decoding can take place.
        """

        if self._content_consumed:

            raise RuntimeError('The content for this response was '
                               'already consumed')


        def generate():

            while 1:

                chunk = self.raw.read(chunk_size)

                if not chunk:

                    break

                yield chunk

            self._content_consumed = True

        gen = generate


        if 'gzip' in self.headers.get('content-encoding', ''):

            gen = stream_decode_gzip(gen)


        if decode_unicode is None:

            decode_unicode = settings.decode_unicode


        if decode_unicode:

            gen = stream_decode_response_unicode(gen, self)


        return gen



    
	@property

    def content(self):

        """Content of the response, in bytes or unicode
        (if available).
        """


        if self._content is not None:

            return self._content


        if self._content_consumed:

            raise RuntimeError(
                'The content for this response was already consumed')


                self._content = self.raw.read() or self.raw.data


                if 'gzip' in self.headers.get('content-encoding', ''):

            try:

                self._content = decode_gzip(self._content)

            except zlib.error:

                pass


                if self.config.get('decode_unicode'):

            self._content = get_unicode_from_response(self)


        self._content_consumed = True

        return self._content



    
	def raise_for_status(self):

        """Raises stored :class:`HTTPError` or :class:`URLError`,
        if one occured.
        """


        if self.error:

            raise self.error


        if (self.status_code >= 300) and (self.status_code < 400):

            raise Exception('300 yo')


        elif (self.status_code >= 400) and (self.status_code < 500):

            raise Exception('400 yo')


        elif (self.status_code >= 500) and (self.status_code < 600):

            raise Exception('500 yo')



