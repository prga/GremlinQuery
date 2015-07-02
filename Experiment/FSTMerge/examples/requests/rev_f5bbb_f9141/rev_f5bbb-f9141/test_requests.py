
from __future__ import with_statement


import time

import os

import unittest


import requests

import envoy

from requests import HTTPError


try:

    import omnijson as json

except ImportError:

    import json




PORT = os.environ.get('HTTPBIN_PORT', '7077')


HTTPBIN_URL = 'http://0.0.0.0:%s/' % (PORT)



def httpbin(*suffix):

    """Returns url for HTTPBIN resource."""


    return HTTPBIN_URL + '/'.join(suffix)

SERVICES = (httpbin, )


_httpbin = False

class  RequestsTestSuite (unittest.TestCase) :
	"""Requests test cases."""
	
        _multiprocess_can_split_ = True
	
    def setUp(self):


        global _httpbin


        if not _httpbin:


            c = envoy.connect('gunicorn httpbin:app --bind=0.0.0.0:%s' % (PORT))


            self.httpbin = c

            _httpbin = True

            time.sleep(1)

	def tearDown(self):

        """Teardown."""

	def test_entry_points(self):


        requests.session

        requests.session().get

        requests.session().head

        requests.get

        requests.head

        requests.put

        requests.patch

        requests.post

	def test_invalid_url(self):

        self.assertRaises(ValueError, requests.get, 'hiwpefhipowhefopw')

	def test_HTTP_200_OK_GET(self):

        r = requests.get(httpbin('/get'))

        self.assertEqual(r.status_code, 200)

	def test_HTTP_302_ALLOW_REDIRECT_GET(self):

        r = requests.get(httpbin('redirect', '1'))

        self.assertEqual(r.status_code, 200)

	def test_HTTP_302_GET(self):

        r = requests.get(httpbin('redirect', '1'), allow_redirects=False)

        self.assertEqual(r.status_code, 302)

	def test_HTTP_200_OK_GET_WITH_PARAMS(self):

        heads = {'User-agent': 'Mozilla/5.0'}


        r = requests.get(httpbin('user-agent'), headers=heads)


        assert heads['User-agent'] in r.content

        self.assertEqual(r.status_code, 200)

	def test_HTTP_200_OK_GET_WITH_MIXED_PARAMS(self):

        heads = {'User-agent': 'Mozilla/5.0'}


        r = requests.get(httpbin('get') + '?test=true', params={'q': 'test'}, headers=heads)

        self.assertEqual(r.status_code, 200)

	def test_user_agent_transfers(self):

        """Issue XX"""


        heads = {
            'User-agent':
                'Mozilla/5.0 (github.com/kennethreitz/requests)'
        }


        r = requests.get(httpbin('user-agent'), headers=heads);

        self.assertTrue(heads['User-agent'] in r.content)


        heads = {
            'user-agent':
                'Mozilla/5.0 (github.com/kennethreitz/requests)'
        }


        r = requests.get(httpbin('user-agent'), headers=heads);

        self.assertTrue(heads['user-agent'] in r.content)

	def test_HTTP_200_OK_HEAD(self):

        r = requests.head(httpbin('/get'))

        self.assertEqual(r.status_code, 200)

	def test_HTTP_200_OK_PUT(self):

        r = requests.put(httpbin('put'))

        self.assertEqual(r.status_code, 200)

	def test_HTTP_200_OK_PATCH(self):

        r = requests.patch(httpbin('patch'))

        self.assertEqual(r.status_code, 200)

	def test_BASICAUTH_HTTP_200_OK_GET(self):


        for service in SERVICES:


            auth = ('user', 'pass')

            url = service('basic-auth', 'user', 'pass')


            r = requests.get(url, auth=auth)

            self.assertEqual(r.status_code, 200)


            r = requests.get(url)

            self.assertEqual(r.status_code, 401)



            s = requests.session(auth=auth)

            r = s.get(url)

            self.assertEqual(r.status_code, 200)

	def test_DIGESTAUTH_HTTP_200_OK_GET(self):


        for service in SERVICES:


            auth = ('digest', 'user', 'pass')

            url = service('digest-auth', 'auth', 'user', 'pass')


            r = requests.get(url, auth=auth)

            self.assertEqual(r.status_code, 200)


            r = requests.get(url)

            self.assertEqual(r.status_code, 401)



            s = requests.session(auth=auth)

            r = s.get(url)

            self.assertEqual(r.status_code, 200)

	def test_POSTBIN_GET_POST_FILES(self):


        for service in SERVICES:


            url = service('post')

            post = requests.post(url).raise_for_status()


            post = requests.post(url, data={'some': 'data'})

            self.assertEqual(post.status_code, 200)


            post2 = requests.post(url, files={'some': open('test_requests.py')})

            self.assertEqual(post2.status_code, 200)


            post3 = requests.post(url, data='[{"some": "json"}]')

            self.assertEqual(post3.status_code, 200)

	def test_POSTBIN_GET_POST_FILES_WITH_PARAMS(self):


        for service in SERVICES:


            url = service('post')

            post = requests.post(url,
                files={'some': open('test_requests.py')},
                data={'some': 'data'})


            self.assertEqual(post.status_code, 200)

	def test_POSTBIN_GET_POST_FILES_WITH_HEADERS(self):


        for service in SERVICES:


            url = service('post')


            post2 = requests.post(url,
                files={'some': open('test_requests.py')},
                headers = {'User-Agent': 'requests-tests'})


            self.assertEqual(post2.status_code, 200)

	def test_nonzero_evaluation(self):


        for service in SERVICES:


            r = requests.get(service('status', '500'))

            self.assertEqual(bool(r), False)


            r = requests.get(service('/get'))

            self.assertEqual(bool(r), True)

	def test_request_ok_set(self):


        for service in SERVICES:


            r = requests.get(service('status', '404'))

                                    self.assertEqual(r.ok, False)

	def test_status_raising(self):

        r = requests.get(httpbin('status', '404'))

        self.assertRaises(HTTPError, r.raise_for_status)


        r = requests.get(httpbin('status', '200'))

        self.assertFalse(r.error)

        r.raise_for_status()

	def test_decompress_gzip(self):


        r = requests.get(httpbin('gzip'))

        r.content.decode('ascii')

	def test_unicode_get(self):


        for service in SERVICES:


            url = service('/get')


            requests.get(url, params={'foo': u'føø'})

            requests.get(url, params={u'føø': u'føø'})

            requests.get(url, params={'føø': 'føø'})

            requests.get(url, params={'foo': u'foo'})

            requests.get(service('ø'), params={'foo': u'foo'})

	def test_httpauth_recursion(self):


        http_auth = ('user', 'BADpass')


        for service in SERVICES:

            r = requests.get(service('basic-auth', 'user', 'pass'), auth=http_auth)

            self.assertEquals(r.status_code, 401)

	def test_urlencoded_post_data(self):


        for service in SERVICES:


            r = requests.post(service('post'), data=dict(test='fooaowpeuf'))


            self.assertEquals(r.status_code, 200)

            self.assertEquals(r.headers['content-type'], 'application/json')

            self.assertEquals(r.url, service('post'))


            rbody = json.loads(r.content)


            self.assertEquals(rbody.get('form'), dict(test='fooaowpeuf'))

            self.assertEquals(rbody.get('data'), '')

	def test_nonurlencoded_post_data(self):


        for service in SERVICES:


            r = requests.post(service('post'), data='fooaowpeuf')


            self.assertEquals(r.status_code, 200)

            self.assertEquals(r.headers['content-type'], 'application/json')

            self.assertEquals(r.url, service('post'))


            rbody = json.loads(r.content)

                                    self.assertEquals(rbody.get('form'), None)

            self.assertEquals(rbody.get('data'), 'fooaowpeuf')

	def test_urlencoded_post_querystring(self):


        for service in SERVICES:


            r = requests.post(service('post'), params=dict(test='fooaowpeuf'))


            self.assertEquals(r.status_code, 200)

            self.assertEquals(r.headers['content-type'], 'application/json')

            self.assertEquals(r.url, service('post?test=fooaowpeuf'))


            rbody = json.loads(r.content)

            self.assertEquals(rbody.get('form'), {}) 

            self.assertEquals(rbody.get('data'), '')

	def test_nonurlencoded_post_querystring(self):


        for service in SERVICES:


            r = requests.post(service('post'), params='fooaowpeuf')


            self.assertEquals(r.status_code, 200)

            self.assertEquals(r.headers['content-type'], 'application/json')

            self.assertEquals(r.url, service('post?fooaowpeuf'))


            rbody = json.loads(r.content)

            self.assertEquals(rbody.get('form'), {}) 

            self.assertEquals(rbody.get('data'), '')

	def test_urlencoded_post_query_and_data(self):


        for service in SERVICES:


            r = requests.post(
                service('post'),
                params=dict(test='fooaowpeuf'),
                data=dict(test2="foobar"))


            self.assertEquals(r.status_code, 200)

            self.assertEquals(r.headers['content-type'], 'application/json')

            self.assertEquals(r.url, service('post?test=fooaowpeuf'))


            rbody = json.loads(r.content)

            self.assertEquals(rbody.get('form'), dict(test2='foobar'))

            self.assertEquals(rbody.get('data'), '')

	def test_nonurlencoded_post_query_and_data(self):


        for service in SERVICES:


            r = requests.post(service('post'),
                params='fooaowpeuf', data="foobar")


            self.assertEquals(r.status_code, 200)

            self.assertEquals(r.headers['content-type'], 'application/json')

            self.assertEquals(r.url, service('post?fooaowpeuf'))


            rbody = json.loads(r.content)


            self.assertEquals(rbody.get('form'), None)

            self.assertEquals(rbody.get('data'), 'foobar')

	def test_urlencoded_get_query_multivalued_param(self):


        for service in SERVICES:


            r = requests.get(service('get'), params=dict(test=['foo','baz']))

            self.assertEquals(r.status_code, 200)

            self.assertEquals(r.url, service('get?test=foo&test=baz'))

	def test_urlencoded_post_querystring_multivalued(self):


        for service in SERVICES:


            r = requests.post(service('post'), params=dict(test=['foo','baz']))

            self.assertEquals(r.status_code, 200)

            self.assertEquals(r.headers['content-type'], 'application/json')

            self.assertEquals(r.url, service('post?test=foo&test=baz'))


            rbody = json.loads(r.content)

            self.assertEquals(rbody.get('form'), {}) 

            self.assertEquals(rbody.get('data'), '')

	def test_urlencoded_post_query_multivalued_and_data(self):


        for service in SERVICES:


            r = requests.post(
                service('post'),
                params=dict(test=['foo','baz']),
                data=dict(test2="foobar",test3=['foo','baz']))


            self.assertEquals(r.status_code, 200)

            self.assertEquals(r.headers['content-type'], 'application/json')

            self.assertEquals(r.url, service('post?test=foo&test=baz'))

            rbody = json.loads(r.content)

            self.assertEquals(rbody.get('form'), dict(test2='foobar',test3='foo'))

            self.assertEquals(rbody.get('data'), '')

	def test_GET_no_redirect(self):


        for service in SERVICES:


            r = requests.get(service('redirect', '3'), allow_redirects=False)

            self.assertEquals(r.status_code, 302)

            self.assertEquals(len(r.history), 0)

	def test_HEAD_no_redirect(self):


        for service in SERVICES:


            r = requests.head(service('redirect', '3'), allow_redirects=False)

            self.assertEquals(r.status_code, 302)

            self.assertEquals(len(r.history), 0)

	def test_redirect_history(self):


        for service in SERVICES:


            r = requests.get(service('redirect', '3'))

            self.assertEquals(r.status_code, 200)

            self.assertEquals(len(r.history), 3)

	def test_relative_redirect_history(self):


        for service in SERVICES:


            r = requests.get(service('relative-redirect', '3'))

            self.assertEquals(r.status_code, 200)

            self.assertEquals(len(r.history), 3)

	def test_session_HTTP_200_OK_GET(self):


        s = requests.session()

        r = s.get(httpbin('/get'))

        self.assertEqual(r.status_code, 200)

	def test_session_persistent_headers(self):


        heads = {'User-agent': 'Mozilla/5.0'}


        s = requests.session()

        s.headers = heads


                r1 = s.get(httpbin('user-agent'))

        assert heads['User-agent'] in r1.content


        r2 = s.get(httpbin('user-agent'))

        assert heads['User-agent'] in r2.content


        new_heads = {'User-agent': 'blah'}

        r3 = s.get(httpbin('user-agent'), headers=new_heads)

        assert new_heads['User-agent'] in r3.content


        self.assertEqual(r2.status_code, 200)

	def test_session_persistent_cookies(self):


        s = requests.session()


                _c = {'kenneth': 'reitz', 'bessie': 'monke'}

        r = s.get(httpbin('cookies'), cookies=_c)

        r = s.get(httpbin('cookies'))


                c = json.loads(r.content).get('cookies')

        assert c == _c


                r = s.get(httpbin('cookies'), cookies={})

        c = json.loads(r.content).get('cookies')

        assert c == _c


                r = s.get(httpbin('cookies'), cookies={'bessie': None})

        c = json.loads(r.content).get('cookies')

        del _c['bessie']

        assert c == _c



        s = requests.session(cookies=_c)

        c = json.loads(r.content).get('cookies')

        assert c == _c




    
	def test_session_persistent_params(self):


        params = {'a': 'a_test'}


        s = requests.session()

        s.params = params


                r1 = s.get(httpbin('get'))

        assert params['a'] in r1.content



        params2 = {'b': 'b_test'}


        r2 = s.get(httpbin('get'), params=params2)

        assert params['a'] in r2.content

        assert params2['b'] in r2.content



        params3 = {'b': 'b_test', 'a': None, 'c': 'c_test'}


        r3 = s.get(httpbin('get'), params=params3)


        assert not params['a'] in r3.content

        assert params3['b'] in r3.content

        assert params3['c'] in r3.content

	def test_cookies(self):


        s = requests.session()

        r = s.get(httpbin('cookies', 'set', 'face', 'book'))

        print r.headers

        print r.history[0].cookies

        print r.content

        print r.url




	
	"""Requests test cases."""
	def test_invalid_content(self):


                        r = requests.get('http://somedomainthatclearlydoesntexistg.com', allow_redirects=False)


        assert r.content == None





if __name__ == '__main__':

    unittest.main()







try:

    import omnijson as json

except ImportError:

    import json




if __name__ == '__main__':

    unittest.main()

