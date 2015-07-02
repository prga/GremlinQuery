"""
This module contains the default values for all settings used by Scrapy. 

For more information about these settings you can read the settings
documentation in docs/ref/settings.rst

Scrapy developers, if you add a setting here remember to:

* add it in alphabetical order
* group similar settings without leaving blank lines
* add its documentation to the available settings documentation
  (docs/ref/settings.rst)

"""


from os.path import join, abspath, dirname


ADAPTORS_DEBUG = False


BOT_NAME = 'scrapybot'

BOT_VERSION = '1.0'


CLOSEDOMAIN_TIMEOUT = 0

CLOSEDOMAIN_NOTIFY = []


CLUSTER_LOGDIR = ''


CLUSTER_MASTER_PORT = 8790

CLUSTER_MASTER_ENABLED = 0

CLUSTER_MASTER_POLL_INTERVAL = 60

CLUSTER_MASTER_NODES = {}

CLUSTER_MASTER_STATEFILE = ""


CLUSTER_WORKER_ENABLED = 0

CLUSTER_WORKER_MAXPROC = 4

CLUSTER_WORKER_PORT = 8789


COMMANDS_MODULE = ''

COMMANDS_SETTINGS_MODULE = ''


CONCURRENT_DOMAINS = 8


COOKIES_DEBUG = False


DEFAULT_ITEM_CLASS = 'scrapy.item.ScrapedItem'


DEFAULT_REQUEST_HEADERS = {
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'en',
}


DEFAULT_SPIDER = None


DEPTH_LIMIT = 0

DEPTH_STATS = True


DOWNLOAD_DELAY = 0

DOWNLOAD_TIMEOUT = 180


DOWNLOADER_DEBUG = False


DOWNLOADER_MIDDLEWARES = {}


~~FSTMerge~~ DOWNLOADER_MIDDLEWARES_BASE = {
    
    'scrapy.contrib.downloadermiddleware.robotstxt.RobotsTxtMiddleware': 100,
    'scrapy.contrib.downloadermiddleware.httpauth.HttpAuthMiddleware': 300,
    'scrapy.contrib.downloadermiddleware.useragent.UserAgentMiddleware': 400,
    'scrapy.contrib.downloadermiddleware.retry.RetryMiddleware': 500,
    'scrapy.contrib.downloadermiddleware.common.CommonMiddleware': 550,
    'scrapy.contrib.downloadermiddleware.redirect.RedirectMiddleware': 600,
    'scrapy.contrib.downloadermiddleware.cookies.CookiesMiddleware': 700,
    'scrapy.contrib.downloadermiddleware.httpcompression.HttpCompressionMiddleware': 800,
    'scrapy.contrib.downloadermiddleware.debug.CrawlDebug': 840,
    'scrapy.contrib.downloadermiddleware.stats.DownloaderStats': 850,
    'scrapy.contrib.downloadermiddleware.cache.HttpCacheMiddleware': 900,
    
} ##FSTMerge## DOWNLOADER_MIDDLEWARES_BASE = {
    
    'scrapy.contrib.downloadermiddleware.robotstxt.RobotsTxtMiddleware': 100,
    'scrapy.contrib.downloadermiddleware.errorpages.ErrorPagesMiddleware': 200,
    'scrapy.contrib.downloadermiddleware.httpauth.HttpAuthMiddleware': 300,
    'scrapy.contrib.downloadermiddleware.useragent.UserAgentMiddleware': 400,
    'scrapy.contrib.downloadermiddleware.retry.RetryMiddleware': 500,
    'scrapy.contrib.downloadermiddleware.common.CommonMiddleware': 550,
    'scrapy.contrib.downloadermiddleware.redirect.RedirectMiddleware': 600,
    'scrapy.contrib.downloadermiddleware.cookies.CookiesMiddleware': 700,
    'scrapy.contrib.downloadermiddleware.httpcompression.HttpCompressionMiddleware': 800,
    'scrapy.contrib.downloadermiddleware.debug.CrawlDebug': 840,
    'scrapy.contrib.downloadermiddleware.stats.DownloaderStats': 850,
    'scrapy.contrib.downloadermiddleware.cache.HttpCacheMiddleware': 900,
    
} ##FSTMerge## DOWNLOADER_MIDDLEWARES_BASE = {
    
    'scrapy.contrib.downloadermiddleware.robotstxt.RobotsTxtMiddleware': 100,
    'scrapy.contrib.downloadermiddleware.errorpages.ErrorPagesMiddleware': 200,
    'scrapy.contrib.downloadermiddleware.httpauth.HttpAuthMiddleware': 300,
    'scrapy.contrib.downloadermiddleware.useragent.UserAgentMiddleware': 400,
    'scrapy.contrib.downloadermiddleware.retry.RetryMiddleware': 500,
    'scrapy.contrib.downloadermiddleware.defaultheaders.DefaultHeadersMiddleware': 550,
    'scrapy.contrib.downloadermiddleware.redirect.RedirectMiddleware': 600,
    'scrapy.contrib.downloadermiddleware.cookies.CookiesMiddleware': 700,
    'scrapy.contrib.downloadermiddleware.httpcompression.HttpCompressionMiddleware': 800,
    'scrapy.contrib.downloadermiddleware.stats.DownloaderStats': 850,
    'scrapy.contrib.downloadermiddleware.cache.HttpCacheMiddleware': 900,
    
}


DOWNLOADER_STATS = True


DUPEFILTER_FILTERCLASS = 'scrapy.dupefilter.SimplePerDomainFilter'


ENABLED_SPIDERS_FILE = ''


ENGINE_DEBUG = False


EXTENSIONS = [
    'scrapy.stats.corestats.CoreStats',
    'scrapy.xpath.extension.ResponseLibxml2',
    'scrapy.management.web.WebConsole',
    'scrapy.management.telnet.TelnetConsole',
    'scrapy.contrib.webconsole.scheduler.SchedulerQueue',
    'scrapy.contrib.webconsole.livestats.LiveStats',
    'scrapy.contrib.webconsole.spiderctl.Spiderctl',
    'scrapy.contrib.webconsole.enginestatus.EngineStatus',
    'scrapy.contrib.webconsole.stats.StatsDump',
    'scrapy.contrib.webconsole.spiderstats.SpiderStats',
    'scrapy.contrib.spider.reloader.SpiderReloader',
    'scrapy.contrib.memusage.MemoryUsage',
    'scrapy.contrib.memdebug.MemoryDebugger',
    'scrapy.contrib.closedomain.CloseDomain',
    'scrapy.contrib.debug.StackTraceDump',
    'scrapy.contrib.response.soup.ResponseSoup',
]


GROUPSETTINGS_ENABLED = False

GROUPSETTINGS_MODULE = ''


HTTPCACHE_DIR = ''

HTTPCACHE_IGNORE_MISSING = False

HTTPCACHE_SECTORIZE = True

HTTPCACHE_EXPIRATION_SECS = 0


ITEM_PIPELINES = []


LOG_ENABLED = True

LOG_STDOUT = False

LOGLEVEL = 'DEBUG'

LOGFILE = None


MAIL_HOST = 'localhost'

MAIL_FROM = 'scrapy@localhost'


MEMDEBUG_ENABLED = False

MEMDEBUG_NOTIFY = []


MEMORYSTORE = 'scrapy.core.scheduler.MemoryStore'


MEMUSAGE_ENABLED = 1

MEMUSAGE_LIMIT_MB = 0

MEMUSAGE_NOTIFY_MAIL = []

MEMUSAGE_REPORT = False

MEMUSAGE_WARNING_MB = 0


MYSQL_CONNECTION_SETTINGS = {}


NEWSPIDER_MODULE = ''


PRIORITIZER = 'scrapy.core.prioritizers.RandomPrioritizer'





REQUESTS_QUEUE_SIZE = 0

REQUESTS_PER_DOMAIN = 8


RETRY_TIMES = 2

RETRY_HTTP_CODES = ['500', '503', '504', '400', '408']



ROBOTSTXT_OBEY = False


SCHEDULER = 'scrapy.core.scheduler.Scheduler'


SCHEDULER_MIDDLEWARES = {}


SCHEDULER_MIDDLEWARES_BASE = {
    'scrapy.contrib.schedulermiddleware.duplicatesfilter.DuplicatesFilterMiddleware': 500,
}


SCHEDULER_ORDER = 'BFO'


SPIDER_MODULES = []


SPIDERPROFILER_ENABLED = False


SPIDER_MIDDLEWARES = {}


SPIDER_MIDDLEWARES_BASE = {
    
    'scrapy.contrib.spidermiddleware.httperror.HttpErrorMiddleware': 50,
    'scrapy.contrib.itemsampler.ItemSamplerMiddleware': 100,
    'scrapy.contrib.spidermiddleware.limit.RequestLimitMiddleware': 200,
    'scrapy.contrib.spidermiddleware.restrict.RestrictMiddleware': 300,
    'scrapy.contrib.spidermiddleware.offsite.OffsiteMiddleware': 500,
    'scrapy.contrib.spidermiddleware.referer.RefererMiddleware': 700,
    'scrapy.contrib.spidermiddleware.urllength.UrlLengthMiddleware': 800,
    'scrapy.contrib.spidermiddleware.depth.DepthMiddleware': 900,
    
}


STATS_ENABLED = True

STATS_CLEANUP = False

STATS_DEBUG = False


STATS_WSPORT = 8089

STATS_WSTIMEOUT = 15


TEMPLATES_DIR = abspath(join(dirname(__file__), '..', 'templates'))


URLLENGTH_LIMIT = 2083


USER_AGENT = '%s/%s' % (BOT_NAME, BOT_VERSION)


TELNETCONSOLE_ENABLED = 1


WEBCONSOLE_ENABLED = True

WEBCONSOLE_PORT = None

WEBCONSOLE_LOGFILE = None


GLOBAL_CLUSTER_SETTINGS = []







REDIRECT_MAX_METAREFRESH_DELAY = 100

REDIRECT_MAX_TIMES = 20

