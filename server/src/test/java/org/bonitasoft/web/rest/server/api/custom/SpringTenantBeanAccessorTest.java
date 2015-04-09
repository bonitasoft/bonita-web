package org.bonitasoft.web.rest.server.api.custom;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Test;


public class SpringTenantBeanAccessorTest {

	@Test
	public void should_get_the_rest_configuration() throws Exception {
		URL resource = SpringTenantBeanAccessorTest.class.getResource("restAPI.xml");
		File file = new File(resource.toURI());
		SpringTenantBeanAccessor accessor = new SpringTenantBeanAccessor(file.getParentFile().getAbsoluteFile());
		List<CustomResourceDescriptor> configuration = accessor.getRestConfiguration();
		assertThat(configuration).hasSize(2);
	}

	@Test
	public void should_get_no_rest_configuration_due_to_the_file_miss() throws Exception {
		File file = new File("here");
		SpringTenantBeanAccessor accessor = new SpringTenantBeanAccessor(file);
		List<CustomResourceDescriptor> configuration = accessor.getRestConfiguration();
		assertThat(configuration).hasSize(0);
	}
	
}
