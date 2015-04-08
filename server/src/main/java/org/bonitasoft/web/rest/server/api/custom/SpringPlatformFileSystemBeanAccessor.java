package org.bonitasoft.web.rest.server.api.custom;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Method;

public class SpringPlatformFileSystemBeanAccessor {

//	private AbsoluteFileSystemXmlApplicationContext context;
//
//	public SpringPlatformFileSystemBeanAccessor() {
//		File confFolder = WebBonitaConstantsUtils.getInstance().getConfFolder();
//		File restFile = new File(confFolder, "rest.xml");
//		if (restFile.exists()) {
//			context = new AbsoluteFileSystemXmlApplicationContext(new String [] {restFile.getPath()}, true, null);
//		}
//	}

List<CustomResourceDescriptor> customResourceDescriptors;

	public List<CustomResourceDescriptor> getRestConfiguration() {
		customResourceDescriptors =new ArrayList<>();

		customResourceDescriptors.add(new CustomResourceDescriptor() {
			@Override
			public String getPathTemplate() {
				return "helloworld";
			}

			@Override
			public Method getMethod() {
				return Method.GET;
			}

			@Override
			public String getPageName() {
				return "custompage_helloworld";
			}
		});

		return customResourceDescriptors;

//		if (context == null) {
//			return Collections.emptyMap();
//		}
//		return context.getBean(Map.class);
	}

}
