from scrapy import signals

from scrapy.exceptions import NotConfigured, IgnoreRequest

from scrapy.utils.httpobj import urlparse_cached

from scrapy.utils.misc import load_object

class  HttpCacheMiddleware (object) :
	def __init__(self, settings, stats):

        if not settings.getbool('HTTPCACHE_ENABLED'):

            raise NotConfigured

        self.policy = load_object(settings['HTTPCACHE_POLICY'])(settings)

        self.storage = load_object(settings['HTTPCACHE_STORAGE'])(settings)

        self.ignore_missing = settings.getbool('HTTPCACHE_IGNORE_MISSING')

        self.stats = stats

	@classmethod

    def from_crawler(cls, crawler):

        o = cls(crawler.settings, crawler.stats)

        crawler.signals.connect(o.spider_opened, signal=signals.spider_opened)

        crawler.signals.connect(o.spider_closed, signal=signals.spider_closed)

        return o

	def spider_opened(self, spider):

        self.storage.open_spider(spider)

	def spider_closed(self, spider):

        self.storage.close_spider(spider)

	def process_request(self, request, spider):

                if not self.policy.should_cache_request(request):

            request.meta['_dont_cache'] = True  

            return


                cachedresponse = self.storage.retrieve_response(spider, request)

        if cachedresponse is None:

            self.stats.inc_value('httpcache/miss', spider=spider)

            if self.ignore_missing:

                self.stats.inc_value('httpcache/ignore', spider=spider)

                raise IgnoreRequest("Ignored request not in cache: %s" % request)

            return  


                cachedresponse.flags.append('cached')

        if self.policy.is_cached_response_fresh(cachedresponse, request):

            self.stats.inc_value('httpcache/hit', spider=spider)

            return cachedresponse


                        request.meta['cached_response'] = cachedresponse

	def process_response(self, request, response, spider):

                if 'cached' in response.flags or '_dont_cache' in request.meta:

            request.meta.pop('_dont_cache', None)

            return response


                        if 'Date' not in response.headers:

            response.headers['Date'] = formatdate(usegmt=1)


                cachedresponse = request.meta.pop('cached_response', None)

        if cachedresponse is None:

            self.stats.inc_value('httpcache/firsthand', spider=spider)

            self._cache_response(spider, response, request, cachedresponse)

            return response


        if self.policy.is_cached_response_valid(cachedresponse, response, request):

            self.stats.inc_value('httpcache/revalidate', spider=spider)

            return cachedresponse


        self.stats.inc_value('httpcache/invalidate', spider=spider)

        self._cache_response(spider, response, request, cachedresponse)

        return response

	
	
	def _cache_response(self, spider, response, request, cachedresponse):

        if self.policy.should_cache_response(response, request):

            self.stats.inc_value('httpcache/store', spider=spider)

            self.storage.store_response(spider, request, response)

        else:

            self.stats.inc_value('httpcache/uncacheable', spider=spider)




from scrapy.contrib.httpcache import FilesystemCacheStorage as _FilesystemCacheStorage
class  FilesystemCacheStorage (_FilesystemCacheStorage) :
	def __init__(self, *args, **kwargs):

        import warnings

        from scrapy.exceptions import ScrapyDeprecationWarning

        warnings.warn('Importing FilesystemCacheStorage from '
                      'scrapy.contrib.downloadermiddlware.httpcache is '
                      'deprecated, use scrapy.contrib.httpcache instead.',
                      category=ScrapyDeprecationWarning, stacklevel=1)

        super(_FilesystemCacheStorage, self).__init__(*args, **kwargs)




from email.utils import formatdate

from weakref import WeakKeyDictionary

from scrapy.contrib.httpcache import rfc1123_to_epoch, parse_cachecontrol

