




















from scrapy.contrib.httpcache import FilesystemCacheStorage, DbmCacheStorage

from scrapy.contrib.downloadermiddleware.httpcache import HttpCacheMiddleware

class  HttpCacheMiddlewareTest (unittest.TestCase) :
	~~FSTMerge~~ ##FSTMerge## storage_class = FilesystemCacheStorage ##FSTMerge## storage_class = DbmCacheStorage
	
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390857512705/fstmerge_var1_531018837047832903
=======
def setUp(self):

        self.crawler = get_crawler()

        self.spider = BaseSpider('example.com')

        self.tmpdir = tempfile.mkdtemp()

        self.request = Request('http://www.example.com',
                               headers={'User-Agent': 'test'})

        self.response = Response('http://www.example.com',
                                 headers={'Content-Type': 'text/html'},
                                 body='test body',
                                 status=202)

        self.crawler.stats.open_spider(self.spider)
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390857512705/fstmerge_var2_4287096704803729775

	
	
	
	
	
	
	
	
	
	
	
	

class  FilesystemCacheStorageTest (HttpCacheMiddlewareTest) :
	storage = FilesystemCacheStorage

if __name__ == '__main__':

    unittest.main()


from scrapy.contrib.downloadermiddleware.httpcache import \
    FilesystemCacheStorage, HttpCacheMiddleware

if __name__ == '__main__':

    unittest.main()


