package org.bonitasoft.web.rest.server.api.extension;

import org.junit.Test;

/**
 * @author Laurent Leseigneur
 */
public class ResourceExtensionDescriptorImplTest {

    public static final String PATH_TEMPLATE = "template";
    public static final String METHOD = "method";
    public static final String PAGE_NAME = "pageName";
    public static final String CLASS_FILE_NAME = "classFileName";

    @Test
    public void testResource() throws Exception {
        // given
        final ResourceExtensionDescriptorImpl resource = new ResourceExtensionDescriptorImpl(PATH_TEMPLATE, METHOD, PAGE_NAME, CLASS_FILE_NAME);

        // when then
        ResourceExtensionDescriptorAssert.assertThat(resource).hasPathTemplate(PATH_TEMPLATE);
        ResourceExtensionDescriptorAssert.assertThat(resource).hasMethod(METHOD);
        ResourceExtensionDescriptorAssert.assertThat(resource).hasPageName(PAGE_NAME);
        ResourceExtensionDescriptorAssert.assertThat(resource).hasClassFileName(CLASS_FILE_NAME);

    }


}