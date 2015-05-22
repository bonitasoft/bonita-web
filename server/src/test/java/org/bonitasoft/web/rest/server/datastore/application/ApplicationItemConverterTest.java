package org.bonitasoft.web.rest.server.datastore.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.business.application.ApplicationCreator;
import org.bonitasoft.engine.business.application.ApplicationField;
import org.bonitasoft.engine.business.application.ApplicationState;
import org.bonitasoft.engine.business.application.ApplicationUpdater;
import org.bonitasoft.engine.business.application.impl.ApplicationImpl;
import org.bonitasoft.web.rest.model.application.ApplicationItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.junit.Before;
import org.junit.Test;


public class ApplicationItemConverterTest extends APITestWithMock {

    private static final String STATE = ApplicationState.DEACTIVATED.name();
    private static final long UPDATED_BY = 12L;
    private static final long CREATED_BY = 11;
    private static final String ICON_PATH = "/icon.jpg";
    private static final String DESCRIPTION = "App description";
    private static final String VERSION = "1.0";
    private static final String TOKEN = "app";
    private static final String DISPLAY_NAME = "display app name";
    private static final Date CREATION_DATE = new Date();
    private static final Date UPDATE_DATE = new Date(CREATION_DATE.getTime() + 1000);
    private static final long PROFILE_ID = 1L;
    private static final long HOME_PAGE_ID = 2L;
    private static final long LAYOUT_ID = 3L;
    private static final long THEME_ID = 4L;

    private ApplicationItemConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new ApplicationItemConverter();
    }

    @Test
    public void toApplicationItem_should_map_all_fields() throws Exception {
        //given
        final ApplicationImpl application = new ApplicationImpl(TOKEN, VERSION, DESCRIPTION, LAYOUT_ID, THEME_ID);
        application.setId(15);
        application.setDisplayName(DISPLAY_NAME);
        application.setIconPath(ICON_PATH);
        application.setCreationDate(CREATION_DATE);
        application.setCreatedBy(CREATED_BY);
        application.setLastUpdateDate(UPDATE_DATE);
        application.setUpdatedBy(UPDATED_BY);
        application.setState(STATE);
        application.setProfileId(PROFILE_ID);
        application.setHomePageId(HOME_PAGE_ID);

        //when
        final ApplicationItem item = converter.toApplicationItem(application);

        //then
        assertThat(item).isNotNull();
        assertThat(item.getId().toLong()).isEqualTo(15);
        assertThat(item.getToken()).isEqualTo(TOKEN);
        assertThat(item.getDisplayName()).isEqualTo(DISPLAY_NAME);
        assertThat(item.getVersion()).isEqualTo(VERSION);
        assertThat(item.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(item.getIconPath()).isEqualTo(ICON_PATH);
        assertThat(item.getCreationDate()).isEqualTo(String.valueOf(CREATION_DATE.getTime()));
        assertThat(item.getCreatedBy()).isEqualTo(CREATED_BY);
        assertThat(item.getLastUpdateDate()).isEqualTo(String.valueOf(UPDATE_DATE.getTime()));
        assertThat(item.getUpdatedBy()).isEqualTo(UPDATED_BY);
        assertThat(item.getState()).isEqualTo(STATE);
        assertThat(item.getProfileId().toLong()).isEqualTo(PROFILE_ID);
        assertThat(item.getHomePageId().toLong()).isEqualTo(HOME_PAGE_ID);
        assertThat(item.getLayoutId().toLong()).isEqualTo(LAYOUT_ID);

    }

    @Test
    public void applicationItem_with_null_homepage_id_should_not_return_null() throws Exception {
        //given
        final ApplicationImpl application = new ApplicationImpl(DISPLAY_NAME, VERSION, DESCRIPTION, LAYOUT_ID, THEME_ID);
        application.setId(15);
        application.setDisplayName(DISPLAY_NAME);
        application.setIconPath(ICON_PATH);
        application.setCreationDate(CREATION_DATE);
        application.setCreatedBy(CREATED_BY);
        application.setLastUpdateDate(UPDATE_DATE);
        application.setUpdatedBy(UPDATED_BY);
        application.setState(STATE);
        application.setProfileId(PROFILE_ID);
        application.setHomePageId(null);

        //when
        final ApplicationItem item = converter.toApplicationItem(application);

        //then
        assertThat(item).isNotNull();
        assertThat(item.getAttributeValue(ApplicationItem.ATTRIBUTE_HOME_PAGE_ID)).isEqualTo("-1");
    }

    @Test
    public void applicationItem_with_null_Layout_id_should_not_return_null() throws Exception {
        //given
        final ApplicationImpl application = new ApplicationImpl(DISPLAY_NAME, VERSION, DESCRIPTION, null, THEME_ID);
        application.setId(15);
        application.setDisplayName(DISPLAY_NAME);
        application.setIconPath(ICON_PATH);
        application.setCreationDate(CREATION_DATE);
        application.setCreatedBy(CREATED_BY);
        application.setLastUpdateDate(UPDATE_DATE);
        application.setUpdatedBy(UPDATED_BY);
        application.setState(STATE);
        application.setProfileId(PROFILE_ID);
        application.setHomePageId(null);

        //when
        final ApplicationItem item = converter.toApplicationItem(application);

        //then
        assertThat(item).isNotNull();
        assertThat(item.getAttributeValue(ApplicationItem.ATTRIBUTE_LAYOUT_ID)).isEqualTo("-1");
    }

    @Test
    public void applicationItem_with_null_Theme_id_should_not_return_null() throws Exception {
        //given
        final ApplicationImpl application = new ApplicationImpl(DISPLAY_NAME, VERSION, DESCRIPTION, LAYOUT_ID, null);
        application.setId(15);
        application.setDisplayName(DISPLAY_NAME);
        application.setIconPath(ICON_PATH);
        application.setCreationDate(CREATION_DATE);
        application.setCreatedBy(CREATED_BY);
        application.setLastUpdateDate(UPDATE_DATE);
        application.setUpdatedBy(UPDATED_BY);
        application.setState(STATE);
        application.setProfileId(PROFILE_ID);
        application.setHomePageId(null);

        //when
        final ApplicationItem item = converter.toApplicationItem(application);

        //then
        assertThat(item).isNotNull();
        assertThat(item.getAttributeValue(ApplicationItem.ATTRIBUTE_THEME_ID)).isEqualTo("-1");
    }

    @Test
    public void toApplicationCreator_should_map_all_fields() throws Exception {
        //given
        final ApplicationItem item = new ApplicationItem();
        item.setToken(TOKEN);
        item.setDisplayName(DISPLAY_NAME);
        item.setVersion(VERSION);
        item.setDescription(DESCRIPTION);
        item.setIconPath(ICON_PATH);
        item.setProfileId(PROFILE_ID);

        //when
        final ApplicationCreator creator = converter.toApplicationCreator(item);

        //then
        assertThat(creator).isNotNull();
        final Map<ApplicationField, Serializable> fields = creator.getFields();
        assertThat(fields.get(ApplicationField.TOKEN)).isEqualTo(TOKEN);
        assertThat(fields.get(ApplicationField.DISPLAY_NAME)).isEqualTo(DISPLAY_NAME);
        assertThat(fields.get(ApplicationField.VERSION)).isEqualTo(VERSION);
        assertThat(fields.get(ApplicationField.DESCRIPTION)).isEqualTo(DESCRIPTION);
        assertThat(fields.get(ApplicationField.ICON_PATH)).isEqualTo(ICON_PATH);
        assertThat(fields.get(ApplicationField.PROFILE_ID)).isEqualTo(PROFILE_ID);
    }

    @Test
    public void toApplicationUpdater_should_map_all_fields() {

        //given
        final HashMap<String, String> fields = new HashMap<String, String>();
        fields.put(ApplicationItem.ATTRIBUTE_TOKEN, TOKEN);
        fields.put(ApplicationItem.ATTRIBUTE_DISPLAY_NAME, DISPLAY_NAME);
        fields.put(ApplicationItem.ATTRIBUTE_DESCRIPTION, DESCRIPTION);
        fields.put(ApplicationItem.ATTRIBUTE_VERSION, VERSION);
        fields.put(ApplicationItem.ATTRIBUTE_ICON_PATH, ICON_PATH);
        fields.put(ApplicationItem.ATTRIBUTE_PROFILE_ID, String.valueOf(PROFILE_ID));
        fields.put(ApplicationItem.ATTRIBUTE_HOME_PAGE_ID, String.valueOf(HOME_PAGE_ID));
        fields.put(ApplicationItem.ATTRIBUTE_STATE, STATE);


        //when
        final ApplicationUpdater updater = converter.toApplicationUpdater(fields);

        //then
        assertThat(updater).isNotNull();
        assertThat(updater.getFields().get(ApplicationField.TOKEN)).isEqualTo(TOKEN);
        assertThat(updater.getFields().get(ApplicationField.DISPLAY_NAME)).isEqualTo(DISPLAY_NAME);
        assertThat(updater.getFields().get(ApplicationField.DESCRIPTION)).isEqualTo(DESCRIPTION);
        assertThat(updater.getFields().get(ApplicationField.VERSION)).isEqualTo(VERSION);
        assertThat(updater.getFields().get(ApplicationField.ICON_PATH)).isEqualTo(ICON_PATH);
        assertThat(updater.getFields().get(ApplicationField.PROFILE_ID)).isEqualTo(PROFILE_ID);
        assertThat(updater.getFields().get(ApplicationField.HOME_PAGE_ID)).isEqualTo(HOME_PAGE_ID);

    }

    @Test
    public void update_with_no_homePageId_should_send_null() {

        //given
        final HashMap<String, String> fields = new HashMap<String, String>();
        fields.put(ApplicationItem.ATTRIBUTE_HOME_PAGE_ID, "-1");

        //when
        final ApplicationUpdater updater = converter.toApplicationUpdater(fields);

        //then
        assertThat(updater).isNotNull();
        assertThat(updater.getFields().get(ApplicationField.HOME_PAGE_ID)).isEqualTo(null);

    }

    @Test
    public void update_with_no_layout_page_should_send_null() {

        //given
        final HashMap<String, String> fields = new HashMap<String, String>();
        fields.put(ApplicationItem.ATTRIBUTE_LAYOUT_ID, "-1");

        //when
        final ApplicationUpdater updater = converter.toApplicationUpdater(fields);

        //then
        assertThat(updater).isNotNull();
        assertThat(updater.getFields().get(ApplicationField.LAYOUT_ID)).isEqualTo(null);

    }

    @Test
    public void update_with_no_Theme_page_should_send_null() {

        //given
        final HashMap<String, String> fields = new HashMap<String, String>();
        fields.put(ApplicationItem.ATTRIBUTE_THEME_ID, "-1");

        //when
        final ApplicationUpdater updater = converter.toApplicationUpdater(fields);

        //then
        assertThat(updater).isNotNull();
        assertThat(updater.getFields().get(ApplicationField.THEME_ID)).isEqualTo(null);

    }
}
