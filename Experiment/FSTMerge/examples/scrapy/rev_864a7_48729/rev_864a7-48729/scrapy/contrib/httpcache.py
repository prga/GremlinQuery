





from w3lib.http import headers_raw_to_dict, headers_dict_to_raw










class  DbmCacheStorage (object) :
	
	
	
	<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390857512013/fstmerge_var1_7766291591375668968
=======
def retrieve_response(self, spider, request):

        data = self._read_data(spider, request)

        if data is None:

            return  

        url = data['url']

        status = data['status']

        headers = Headers(data['headers'])

        body = data['body']

        respcls = responsetypes.from_args(headers=headers, url=url)

        response = respcls(url=url, headers=headers, status=status, body=body)

        return response
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390857512013/fstmerge_var2_7222521726937741242

	
	
	

class  FilesystemCacheStorage (object) :
	def __init__(self, settings):

        self.cachedir = data_path(settings['HTTPCACHE_DIR'])

        self.expiration_secs = settings.getint('HTTPCACHE_EXPIRATION_SECS')


    
	def open_spider(self, spider):

        pass


    
	def close_spider(self, spider):

        pass


    
	def retrieve_response(self, spider, request):

        """Return response if present in cache, or None otherwise."""

        metadata = self._read_meta(spider, request)

        if metadata is None:

            return  

        rpath = self._get_request_path(spider, request)

        with open(os.path.join(rpath, 'response_body'), 'rb') as f:

            body = f.read()

        with open(os.path.join(rpath, 'response_headers'), 'rb') as f:

            rawheaders = f.read()

        url = metadata.get('response_url')

        status = metadata['status']

        headers = Headers(headers_raw_to_dict(rawheaders))

        respcls = responsetypes.from_args(headers=headers, url=url)

        response = respcls(url=url, headers=headers, status=status, body=body)

        return response


    
	def store_response(self, spider, request, response):

        """Store the given response in the cache."""

        rpath = self._get_request_path(spider, request)

        if not os.path.exists(rpath):

            os.makedirs(rpath)

        metadata = {
            'url': request.url,
            'method': request.method,
            'status': response.status,
            'response_url': response.url,
            'timestamp': time(),
        }

        with open(os.path.join(rpath, 'meta'), 'wb') as f:

            f.write(repr(metadata))

        with open(os.path.join(rpath, 'pickled_meta'), 'wb') as f:

            pickle.dump(metadata, f, protocol=2)

        with open(os.path.join(rpath, 'response_headers'), 'wb') as f:

            f.write(headers_dict_to_raw(response.headers))

        with open(os.path.join(rpath, 'response_body'), 'wb') as f:

            f.write(response.body)

        with open(os.path.join(rpath, 'request_headers'), 'wb') as f:

            f.write(headers_dict_to_raw(request.headers))

        with open(os.path.join(rpath, 'request_body'), 'wb') as f:

            f.write(request.body)


    
	def _get_request_path(self, spider, request):

        key = request_fingerprint(request)

        return os.path.join(self.cachedir, spider.name, key[0:2], key)


    
	def _read_meta(self, spider, request):

        rpath = self._get_request_path(spider, request)

        metapath = os.path.join(rpath, 'pickled_meta')

        if not os.path.exists(metapath):

            return  

        mtime = os.stat(rpath).st_mtime

        if 0 < self.expiration_secs < time() - mtime:

            return  

        with open(metapath, 'rb') as f:

            return pickle.load(f)



