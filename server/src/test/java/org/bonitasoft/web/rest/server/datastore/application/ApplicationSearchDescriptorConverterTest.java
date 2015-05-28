package org.bonitasoft.web.rest.server.datastore.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.engine.business.application.ApplicationSearchDescriptor;
import org.bonitasoft.web.rest.model.application.ApplicationItem;
import org.junit.Test;



public class ApplicationSearchDescriptorConverterTest {

    private final ApplicationSearchDescriptorConverter converter = new ApplicationSearchDescriptorConverter();

    @Test
    public void should_return_ApplicationSearchDescriptor_id_on_convert_attribute_id() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_ID);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.ID);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_name_on_convert_attribute_name() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_TOKEN);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.TOKEN);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_name_on_convert_attribute_displayName() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_DISPLAY_NAME);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.DISPLAY_NAME);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_version_on_convert_attribute_version() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_VERSION);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.VERSION);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_iconPath_on_convert_attribute_iconPath() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_ICON_PATH);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.ICON_PATH);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_creationDate_on_convert_attribute_creationDate() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_CREATION_DATE);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.CREATION_DATE);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_createdBy_on_convert_attribute_createdBy() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_CREATED_BY);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.CREATED_BY);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_lastUpdateDate_on_convert_attribute_lastUpdateDate() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_LAST_UPDATE_DATE);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.LAST_UPDATE_DATE);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_updatedBy_on_convert_attribute_updatedBy() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_UPDATED_BY);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.UPDATED_BY);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_state_on_convert_attribute_state() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_STATE);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.STATE);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_layoutid_on_convert_attribute_layoutid() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_LAYOUT_ID);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.LAYOUT_ID);
    }

    @Test
    public void should_return_ApplicationSearchDescriptor_themeid_on_convert_attribute_themeid() throws Exception {
        //when
        final String value = converter.convert(ApplicationItem.ATTRIBUTE_THEME_ID);

        //then
        assertThat(value).isEqualTo(ApplicationSearchDescriptor.THEME_ID);
    }
}
