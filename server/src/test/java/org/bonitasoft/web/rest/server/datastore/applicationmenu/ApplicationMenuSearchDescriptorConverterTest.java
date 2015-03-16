package org.bonitasoft.web.rest.server.datastore.applicationmenu;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.engine.business.application.ApplicationMenuSearchDescriptor;
import org.bonitasoft.web.rest.model.applicationmenu.ApplicationMenuItem;
import org.bonitasoft.web.rest.server.datastore.applicationmenu.ApplicationMenuSearchDescriptorConverter;
import org.junit.Test;

public class ApplicationMenuSearchDescriptorConverterTest {

    private final ApplicationMenuSearchDescriptorConverter converter = new ApplicationMenuSearchDescriptorConverter();

    @Test
    public void should_return_ApplicationMenuSearchDescriptor_id_on_convert_attribute_id() throws Exception {
        //when
        final String convertedValue = converter.convert(ApplicationMenuItem.ATTRIBUTE_ID);

        //then
        assertThat(convertedValue).isEqualTo(ApplicationMenuSearchDescriptor.ID);
    }

    @Test
    public void should_return_ApplicationMenuSearchDescriptor_display_name_on_convert_attribute_display_name() throws Exception {
        //when
        final String convertedValue = converter.convert(ApplicationMenuItem.ATTRIBUTE_DISPLAY_NAME);

        //then
        assertThat(convertedValue).isEqualTo(ApplicationMenuSearchDescriptor.DISPLAY_NAME);
    }

    @Test
    public void should_return_ApplicationMenuSearchDescriptor_applicationPageId_on_convert_attribute_applicationPageId() throws Exception {
        //when
        final String convertedValue = converter.convert(ApplicationMenuItem.ATTRIBUTE_APPLICATION_PAGE_ID);

        //then
        assertThat(convertedValue).isEqualTo(ApplicationMenuSearchDescriptor.APPLICATION_PAGE_ID);
    }

    @Test
    public void should_return_ApplicationMenuSearchDescriptor_applicationId_on_convert_attribute_applicationId() throws Exception {
        //when
        final String convertedValue = converter.convert(ApplicationMenuItem.ATTRIBUTE_APPLICATION_ID);

        //then
        assertThat(convertedValue).isEqualTo(ApplicationMenuSearchDescriptor.APPLICATION_ID);
    }

    @Test
    public void should_return_ApplicationMenuSearchDescriptor_index_on_convert_attribute_menu_index() throws Exception {
        //when
        final String convertedValue = converter.convert(ApplicationMenuItem.ATTRIBUTE_MENU_INDEX);

        //then
        assertThat(convertedValue).isEqualTo(ApplicationMenuSearchDescriptor.INDEX);
    }

    @Test
    public void should_return_ApplicationMenuSearchDescriptor_index_on_convert_attribute_parent_menu() throws Exception {
        //when
        final String convertedValue = converter.convert(ApplicationMenuItem.ATTRIBUTE_PARENT_MENU_ID);

        //then
        assertThat(convertedValue).isEqualTo(ApplicationMenuSearchDescriptor.PARENT_ID);
    }

}
