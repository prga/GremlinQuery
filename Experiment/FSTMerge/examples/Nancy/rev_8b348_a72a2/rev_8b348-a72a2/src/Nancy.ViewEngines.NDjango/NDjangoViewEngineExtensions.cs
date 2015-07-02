namespace  Nancy.ViewEngines.NDjango {
	
    using System; 
    using System.IO; 

    public static class  NDjangoViewEngineExtensions {
		
        public static  Action<Stream> Django(this IViewEngine source, string name)
        {
            return Django(source, name, (object)null);
        }
 
       
        public static  <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770875249/fstmerge_var1_8446510415441553633
Action<Stream> Django<TModel>(this IViewEngine source, string name, TModel model)
        {
            var viewEngine = new NDjangoViewEngine();

            return stream =>
            {
                var result = viewEngine.RenderView(name, model);
                result.Execute(stream);
            };
        }
=======
Action<Stream> Django<TModel>(this IViewEngine source, string name, TModel model)
        {
            var viewEngine = new NDjangoViewEngine();

            return stream =>
                       {
                           var result = viewEngine.RenderView(name, model);
                           result.Execute(stream);
                       };
        }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770875249/fstmerge_var2_8937096594943126615

	}

}
