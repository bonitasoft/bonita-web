/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.organization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;

import org.bonitasoft.engine.identity.GroupUpdater;
import org.bonitasoft.engine.identity.GroupUpdater.GroupField;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.engineclient.GroupEngineClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Colin PUY
 */
public class GroupUpdaterConverterTest extends APITestWithMock {

    @Mock
    private GroupEngineClient groupEngineClient;
    
    private GroupUpdaterConverter groupUpdaterConverter;
    
    @Before
    public void init() {
        initMocks(this);
        groupUpdaterConverter = new GroupUpdaterConverter(groupEngineClient);
    }
    
    private String getFieldValue(GroupUpdater groupUpdater, GroupField field) {
        return (String) groupUpdater.getFields().get(field);
    }
    
    private HashMap<String, String> buildSimpleAttribute(String attributeName, String attributeValue) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put(attributeName, attributeValue);
        return attributes;
    }

    @Test
    public void convert_return_a_groupUpdater_with_required_fields() throws Exception {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put(GroupItem.ATTRIBUTE_DESCRIPTION, "aNewDescription");
        attributes.put(GroupItem.ATTRIBUTE_ICON, "aNewIcon");
        attributes.put(GroupItem.ATTRIBUTE_NAME, "aNewName");
        attributes.put(GroupItem.ATTRIBUTE_DISPLAY_NAME, "aNewDisplayName");
        
        GroupUpdater updater = groupUpdaterConverter.convert(attributes);
        
        assertThat(getFieldValue(updater, GroupField.DESCRIPTION), is("aNewDescription"));
        assertThat(getFieldValue(updater, GroupField.ICON_PATH), is("aNewIcon"));
        assertThat(getFieldValue(updater, GroupField.NAME), is("aNewName"));
        assertThat(getFieldValue(updater, GroupField.DISPLAY_NAME), is("aNewDisplayName"));
    }
    
    @Test
    public void convert_dont_update_name_if_this_is_a_blank_value() throws Exception {
        String unexpectedName = " ";
        HashMap<String, String> attribute = buildSimpleAttribute(GroupItem.ATTRIBUTE_NAME, unexpectedName);
        
        GroupUpdater updater = groupUpdaterConverter.convert(attribute);
        
        assertThat(getFieldValue(updater, GroupField.NAME), nullValue());
    }

    @Test
    public void convert_update_parent_path_if_a_parent_group_id_is_specified() throws Exception {
        HashMap<String, String> attributes = buildSimpleAttribute(GroupItem.ATTRIBUTE_PARENT_GROUP_ID, "101");
        when(groupEngineClient.getPath("101")).thenReturn("/Expected/Parent/Path");
        
        GroupUpdater updater = groupUpdaterConverter.convert(attributes);
        
        assertThat(getFieldValue(updater, GroupField.PARENT_PATH), is("/Expected/Parent/Path"));
    }
    
    @Test
    public void convert_set_parent_path_to_empty_if_parentGroupId_is_an_empty_string() throws Exception {
        HashMap<String, String> attributes = buildSimpleAttribute(GroupItem.ATTRIBUTE_PARENT_GROUP_ID, "");
        
        GroupUpdater updater = groupUpdaterConverter.convert(attributes);
        
        assertThat(getFieldValue(updater, GroupField.PARENT_PATH), is(""));
    }

}
