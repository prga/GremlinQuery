namespace  Nancy.Tests.Unit {
	
     
     
     
     
     
     
     
     
     
     
     

     class  NancyEngineFixture {
		
           
           
           
           

           

         
           

         
           

         
           

         
           

         
          <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390771033860/fstmerge_var1_1248879547883903811
void Should_pass_all_registered_route_handlers_for_get_request_to_route_resolver()
        {
            // Given
            var request = new Request("GET", "/");
            var descriptions = GetRouteDescriptions(request, this.modules);

            A.CallTo(() => this.locator.GetModules()).Returns(modules);

            // When
            this.engine.HandleRequest(request);

            // Then
            A.CallTo(() => this.resolver.GetRoute(A<Request>.Ignored.Argument,
                A<IEnumerable<RouteDescription>>.That.IsSameSequenceAs(descriptions).Argument)).MustHaveHappened();
        }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390771033860/fstmerge_var2_5303577482932701007
 

         
           

         
           

         
           

         
           

         
           

         
           

         
           

         
           

         
           

         
           

         
           

         
           
        
           

          
	}

}
