package com.yammer.dropwizard.testing.tests.junit; 
import com.google.common.io.Resources; 
import com.sun.jersey.api.client.Client; 
import com.yammer.dropwizard.config.Environment; 
import com.yammer.dropwizard.testing.junit.DropwizardServiceRule; 
import org.junit.ClassRule; 
import org.junit.Test; 
import java.io.File; 

import static org.hamcrest.core.Is.is; 
import static org.junit.Assert.assertNotNull; 
import static org.junit.Assert.assertThat; 

import com.fasterxml.jackson.annotation.JsonProperty; 
import com.yammer.dropwizard.Service; 
import com.yammer.dropwizard.config.Bootstrap; 
import com.yammer.dropwizard.config.Configuration; 
import org.hibernate.validator.constraints.NotEmpty; 

import javax.ws.rs.GET; 
import javax.ws.rs.Path; 

  class  DropwizardServiceRuleTest {
	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1391204982883/fstmerge_var1_3418300227242492711
=======
@Test
    public void canGetExpectedResourceOverHttp() {
        final String content = new Client().resource("http://localhost:" +
                RULE.getLocalPort()
                + "/test").get(String.class);

        assertThat(content, is("Yes, it's here"));
    }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1391204982883/fstmerge_var2_6933238099600795066


	

    

	

    

	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1391204983061/fstmerge_var1_3489430783907461615
=======
public static String resourceFilePath(String resourceClassPathLocation) {
        try {

            return new File(Resources.getResource(resourceClassPathLocation).toURI()).getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1391204983061/fstmerge_var2_6364833933888804880


	


    public static  class  TestService  extends Service<TestConfiguration> {
		
        @Override
        public void initialize(Bootstrap<TestConfiguration> bootstrap) {
        }

		

        @Override
        public void run(TestConfiguration configuration, Environment environment) throws Exception {
            environment.getJerseyEnvironment().addResource(new TestResource(configuration.getMessage()));
        }


	}

	

    @Path("/")
    public static  class  TestResource {
		

        private final String message;

		

        public TestResource(String message) {
            this.message = message;
        }

		

        @Path("test")
        @GET
        public String test() {
            return message;
        }


	}

	

    public static  class  TestConfiguration  extends Configuration {
		

        @JsonProperty
        @NotEmpty
        private String message;

		

        public String getMessage() {
            return message;
        }


	}


}
