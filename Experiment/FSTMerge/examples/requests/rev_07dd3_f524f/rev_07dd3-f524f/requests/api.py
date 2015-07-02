
"""
requests.api
~~~~~~~~~~~~

This module implements the Requests API.

:copyright: (c) 2011 by Kenneth Reitz.
:license: ISC, see LICENSE for more details.

"""


from ._config import get_config

from .models import Request, Response





from .utils import cookiejar_from_dict, header_expand






def request(method, url,
    params=None, data=None, headers=None, cookies=None, files=None, auth=None,
    timeout=None, allow_redirects=False, proxies=None, hooks=None,
    config=None, _pools=None, _return_request=False):


    """Constructs and sends a :class:`Request <Request>`.
    Returns :class:`Response <Response>` object.

    :param method: method for the new :class:`Request` object.
    :param url: URL for the new :class:`Request` object.
    :param params: (optional) Dictionary or bytes to be sent in the query string for the :class:`Request`.
    :param data: (optional) Dictionary or bytes to send in the body of the :class:`Request`.
    :param headers: (optional) Dictionary of HTTP Headers to send with the :class:`Request`.
    :param cookies: (optional) Dict or CookieJar object to send with the :class:`Request`.
    :param files: (optional) Dictionary of 'filename': file-like-objects for multipart encoding upload.
    :param auth: (optional) AuthObject to enable Basic HTTP Auth.
    :param timeout: (optional) Float describing the timeout of the request.
    :param allow_redirects: (optional) Boolean. Set to True if POST/PUT/DELETE redirect following is allowed.
    :param proxies: (optional) Dictionary mapping protocol to the URL of the proxy.
    :param _pools: (optional) An HTTP PoolManager to use.
    """


    method = str(method).upper()

    config = get_config(config)


    if cookies is None:

        cookies = {}


    
        if headers:

        for k, v in headers.items() or {}:

            headers[k] = header_expand(v)


    args = dict(
        method=method,
        url=url,
        data=data,
        params=params,
        headers=headers,
        cookies=cookies,
        files=files,
        auth=auth,
        timeout=timeout or config.get('timeout'),
        hooks=hooks,
        allow_redirects=allow_redirects,
        proxies=proxies or config.get('proxies'),
        _pools=_pools
    )


        args = dispatch_hook('args', hooks, args)


        r = Request(**args)


        r = dispatch_hook('pre_request', hooks, r)


        if _return_request:

        return r


        r.send()


            r = dispatch_hook('post_request', hooks, r)


        r.response = dispatch_hook('response', hooks, r.response)


    return r.response




<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390855481957/fstmerge_var1_7648361899466434873
=======
def get(url, **kwargs):

    """Sends a GET request. Returns :class:`Response` object.

    :param url: URL for the new :class:`Request` object.
    :param **kwargs: Optional arguments that ``request`` takes.
    """


    if 'allow_redirects' not in kwargs:

        kwargs['allow_redirects'] = True


    return request('get', url, **kwargs)
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390855481957/fstmerge_var2_8266251244329100816

<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390855482065/fstmerge_var1_4483263997225345461
=======
def head(url, **kwargs):

    """Sends a HEAD request. Returns :class:`Response` object.

    :param url: URL for the new :class:`Request` object.
    :param **kwargs: Optional arguments that ``request`` takes.
    """


    if 'allow_redirects' not in kwargs:

        kwargs['allow_redirects'] = True


    return request('head', url, **kwargs)
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390855482065/fstmerge_var2_8848109585808442187



<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390855482228/fstmerge_var1_7758935086157354079
=======
def patch(url, data='', **kwargs):

    """Sends a PATCH request. Returns :class:`Response` object.

    :param url: URL for the new :class:`Request` object.
    :param data: (optional) Dictionary or bytes to send in the body of the :class:`Request`.
    :param **kwargs: Optional arguments that ``request`` takes.
    """


    return request('patch', url, data=data, **kwargs)
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390855482228/fstmerge_var2_6302817628623499807



"""
requests.api
~~~~~~~~~~~~

This module impliments the Requests API.

:copyright: (c) 2011 by Kenneth Reitz.
:license: ISC, see LICENSE for more details.

"""


import config

from .models import Request, Response, AuthObject

from .utils import cookiejar_from_dict


from urlparse import urlparse


def request(method, url,
    params=None, data=None, headers=None, cookies=None, files=None, auth=None,
    timeout=None, allow_redirects=False, proxies=None, hooks=None):


    """Constructs and sends a :class:`Request <Request>`.
    Returns :class:`Response <Response>` object.

    :param method: method for the new :class:`Request` object.
    :param url: URL for the new :class:`Request` object.
    :param params: (optional) Dictionary or bytes to be sent in the query string for the :class:`Request`.
    :param data: (optional) Dictionary or bytes to send in the body of the :class:`Request`.
    :param headers: (optional) Dictionary of HTTP Headers to send with the :class:`Request`.
    :param cookies: (optional) Dict or CookieJar object to send with the :class:`Request`.
    :param files: (optional) Dictionary of 'filename': file-like-objects for multipart encoding upload.
    :param auth: (optional) AuthObject to enable Basic HTTP Auth.
    :param timeout: (optional) Float describing the timeout of the request.
    :param allow_redirects: (optional) Boolean. Set to True if POST/PUT/DELETE redirect following is allowed.
    :param proxies: (optional) Dictionary mapping protocol to the URL of the proxy.
    """


    method = str(method).upper()


    if cookies is None:

        cookies = {}


    cookies = cookiejar_from_dict(cookies)


    args = dict(
        method = method,
        url = url,
        data = data,
        params = params,
        headers = headers,
        cookiejar = cookies,
        files = files,
        auth = auth,
        timeout = timeout or config.settings.timeout,
        allow_redirects = allow_redirects,
        proxies = proxies or config.settings.proxies,
    )


        args = dispatch_hook('args', hooks, args)


    r = Request(**args)


        r = dispatch_hook('pre_request', hooks, r)


        r.send()


        r = dispatch_hook('post_request', hooks, r)


        r.response = dispatch_hook('response', hooks, r.response)


    return r.response



