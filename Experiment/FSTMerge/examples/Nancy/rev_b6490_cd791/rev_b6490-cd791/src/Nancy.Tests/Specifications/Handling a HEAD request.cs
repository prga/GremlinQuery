namespace  Nancy.Tests.Specifications {
	
    using System.Net; 
    using Machine.Specifications; 
    using Nancy.Tests.Extensions; 

    [Subject("Handling a HEAD request")] 
    public class  when_head_request_matched_existing_route  : RequestSpec {
		
        Establish context = () =>
            request = ManufactureHEADRequestForRoute("/");
 

        Because of = () => 
            response = engine.HandleRequest(request);
 

        It should_set_status_code_to_ok = () =>
            response.StatusCode.ShouldEqual(HttpStatusCode.OK);
 

        It should_set_content_type_to_text_html = () =>
            response.ContentType.ShouldEqual("text/html");
 

    	private 

    	<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390771132642/fstmerge_var1_4011727177058159542
It should_set_blank_content = () =>
            response.GetStringContentsFromResponse().ShouldBeEmpty();
=======
It should_set_blank_content = () =>
    	    GetStringContentsFromResponse(response).ShouldBeEmpty();
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390771132642/fstmerge_var2_5568178847264468

	}
	

    [Subject("Handling a GET request")] 
    public class  when_head_request_does_not_matched_existing_route  : RequestSpec {
		
        Establish context = () =>
            request = ManufactureGETRequestForRoute("/invalid");
 

        Because of = () =>
            response = engine.HandleRequest(request);
 

        It should_set_status_code_to_not_found = () =>
            response.StatusCode.ShouldEqual(HttpStatusCode.NotFound);
 

        It should_set_content_type_to_text_html = () =>
            response.ContentType.ShouldEqual("text/html");
 

        <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390771132897/fstmerge_var1_2891718823272163693
It should_set_blank_content = () =>
            response.GetStringContentsFromResponse().ShouldBeEmpty();
=======
It should_set_blank_content = () =>
			GetStringContentsFromResponse(response).ShouldBeEmpty();
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390771132897/fstmerge_var2_3150594477023556207

	}

}
