package org.bonitasoft.web.rest.server.api.extension;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Test;


public class TenantSpringBeanAccessorTest {

	@Test
	public void should_get_the_rest_configuration() throws Exception {
		URL resource = TenantSpringBeanAccessorTest.class.getResource("restAPI.xml");
		File file = new File(resource.toURI());
		TenantSpringBeanAccessor accessor = new TenantSpringBeanAccessor(file.getParentFile().getAbsoluteFile());
		List<ResourceExtensionDescriptor> configuration = accessor.getResourceExtensionConfiguration();
		assertThat(configuration).hasSize(2);
	}

	@Test
	public void should_get_no_rest_configuration_due_to_the_file_miss() throws Exception {
		File file = new File("here");
		TenantSpringBeanAccessor accessor = new TenantSpringBeanAccessor(file);
		List<ResourceExtensionDescriptor> configuration = accessor.getResourceExtensionConfiguration();
		assertThat(configuration).hasSize(0);
	}
	
}
