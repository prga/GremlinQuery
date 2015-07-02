namespace  Nancy.ViewEngines.NHaml {
	
    using System; 
    using System.IO; 

    public static class  NHamlViewEngineExtensions {
		
        public static  Action<Stream> Haml(this IViewEngine source, string name)
        {
            return Haml(source, name, (object) null);
        }
 

        public static  <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770875583/fstmerge_var1_697861722024179416
Action<Stream> Haml<TModel>(this IViewEngine source, string name, TModel model)
        {
            var viewEngine = new NHamlViewEngine();

            return stream =>
            {
                var result = viewEngine.RenderView(name, model);
                result.Execute(stream);
            };
        }
=======
Action<Stream> Haml<TModel>(this IViewEngine source, string name, TModel model)
        {
            var viewEngine = new NHamlViewEngine();

            return stream =>
                       {
                           var result = viewEngine.RenderView(name, model);
                           result.Execute(stream);
                       };
        }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770875583/fstmerge_var2_1639752447394405137

	}

}
