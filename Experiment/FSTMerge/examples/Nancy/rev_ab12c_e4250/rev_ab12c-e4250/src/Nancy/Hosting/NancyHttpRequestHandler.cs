namespace  Nancy.Hosting {
	
    using System.Web; 
    using Routing; 

    public class  NancyHttpRequestHandler  : IHttpHandler {
		
        private readonly static  INancyApplication application = new NancyApplication();
 

        public  bool IsReusable
        {
            get { return false; }
        }
 

        public  void ProcessRequest(HttpContext context)
        {
<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390771093869/fstmerge_var1_21830477328575819
            var engine = new NancyEngine(
                CreateModuleLocator(),
                new RouteResolver(),
                application);
=======
            var engine = new NancyEngine(application, new RouteResolver(), application);
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390771093869/fstmerge_var2_7944606357862322714

            var wrappedContext = new HttpContextWrapper(context);
            var handler = new NancyHandler(engine);
            handler.ProcessRequest(wrappedContext);
        }
 

          
	}

}
