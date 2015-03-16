package org.bonitasoft.web.rest.server.datastore.applicationpage;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.engine.business.application.ApplicationPageSearchDescriptor;
import org.bonitasoft.web.rest.model.applicationpage.ApplicationPageItem;
import org.bonitasoft.web.rest.server.datastore.applicationpage.ApplicationPageSearchDescriptorConverter;
import org.junit.Test;

public class ApplicationPageSearchDescriptorConverterTest {

    private final ApplicationPageSearchDescriptorConverter converter = new ApplicationPageSearchDescriptorConverter();

    @Test
    public void should_return_ApplicationPageSearchDescriptor_id_on_convert_attribute_id() throws Exception {
        //when
        final String convertedValue = converter.convert(ApplicationPageItem.ATTRIBUTE_ID);

        //then
        assertThat(convertedValue).isEqualTo(ApplicationPageSearchDescriptor.ID);
    }

    @Test
    public void should_return_ApplicationPageSearchDescriptor_name_on_convert_attribute_name() throws Exception {
        //when
        final String convertedValue = converter.convert(ApplicationPageItem.ATTRIBUTE_TOKEN);

        //then
        assertThat(convertedValue).isEqualTo(ApplicationPageSearchDescriptor.TOKEN);
    }

    @Test
    public void should_return_ApplicationPageSearchDescriptor_applicationId_on_convert_attribute_applicationId() throws Exception {
        //when
        final String convertedValue = converter.convert(ApplicationPageItem.ATTRIBUTE_APPLICATION_ID);

        //then
        assertThat(convertedValue).isEqualTo(ApplicationPageSearchDescriptor.APPLICATION_ID);
    }

    @Test
    public void should_return_ApplicationPageSearchDescriptor_pageId_on_convert_attribute_pageId() throws Exception {
        //when
        final String convertedValue = converter.convert(ApplicationPageItem.ATTRIBUTE_PAGE_ID);

        //then
        assertThat(convertedValue).isEqualTo(ApplicationPageSearchDescriptor.PAGE_ID);
    }

}
