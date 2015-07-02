namespace  Nancy.ViewEngines.NHaml {
	
    using global::NHaml; 

    public class  NHamlViewEngine {
		
        public  NHamlViewEngine() : this(new AspNetTemplateLocator())
        {
        }
 

        public  NHamlViewEngine(IViewLocator viewTemplateLocator)
        {
            ViewTemplateLocator = viewTemplateLocator;
        }
 

        public  IViewLocator ViewTemplateLocator { get; private set; }
 

        public  <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770875474/fstmerge_var1_8987127344281157306
ViewResult RenderView<TModel>(string viewTemplate, TModel model)
        {
            var templateEngine = new TemplateEngine();
            var result = ViewTemplateLocator.GetTemplateContents(viewTemplate);
            var location = result.Location;

            var compiledTemplate = templateEngine.Compile(location, typeof(NHamlView<TModel>));
            var view = (NHamlView<TModel>) compiledTemplate.CreateInstance();

            view.Model = model;

            return new ViewResult(view, location);
        }
=======
ViewResult RenderView<TModel>(string viewTemplate, TModel model)
        {
            var templateEngine = new TemplateEngine();
            var result = ViewTemplateLocator.GetTemplateContents(viewTemplate);
            string location = result.Location;

            CompiledTemplate compiledTemplate = templateEngine.Compile(location, typeof(NHamlView<TModel>));
            var view = (NHamlView<TModel>) compiledTemplate.CreateInstance();

            view.Model = model;

            return new ViewResult(view, location);
        }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770875474/fstmerge_var2_8643743631049826350

	}

}
