


class  Settings (object) :
	
	
        
	
    
	
	
	
	
 
~~FSTMerge~~ settings.base_headers = {
    'User-Agent': 'python-requests.org',
    'Accept-Encoding': ', '.join([ 'identity', 'deflate', 'compress', 'gzip' ]),
}

 ##FSTMerge## settings.base_headers = {'User-Agent': 'python-requests.org'}

 ##FSTMerge##      ~~FSTMerge~~ settings.decode_unicode = False

 ##FSTMerge## settings.decode_unicode = True

 ##FSTMerge## 
 settings.gracefull_hooks = True

 
settings.default_hooks = {
    'args': list(),
    'pre_request': list(),
    'post_request': list(),
    'response': list()
}


