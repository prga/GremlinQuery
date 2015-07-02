








try:

    import omnijson as json

except ImportError:

    import json





















class  RequestsTestSuite (unittest.TestCase) :
	"""Requests test cases."""
	

    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	"""Requests test cases."""
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



    

if __name__ == '__main__':

    unittest.main()



try:

    import omnijson as json

except ImportError:

    import json



if __name__ == '__main__':

    unittest.main()


