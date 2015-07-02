namespace  Nancy.Hosting {
	
     
    using System.Web; 
     

    public class  NancyHttpRequestHandler  : IHttpHandler {
		
        public  bool IsReusable
        {
            get { return false; }
        }
 

        public  void ProcessRequest(HttpContext context)
        {
            var wrappedContext = new HttpContextWrapper(context);
            var handler = new NancyHandler();
            handler.ProcessRequest(wrappedContext);
        }
 

          <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390771227712/fstmerge_var1_2538478290212333980
=======
IRequest CreateNancyRequest(HttpContext context)
        {
        	return new Request(
                context.Request.HttpMethod,
                context.Request.Url.AbsolutePath);
        }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390771227712/fstmerge_var2_105876807331686254
 

          
	}

}
