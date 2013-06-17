package org.bonitasoft.web.rest.server.datastore.userXP.profile;
// /**
// * Copyright (C) 2012 BonitaSoft S.A.
// * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 2.0 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// */
// package org.bonitasoft.console.server.datastore.userXP.profile;
//
// import static java.util.Arrays.asList;
// import static junit.framework.Assert.assertTrue;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_DESCRIPTION;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_INDEX;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_NAME;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_PAGE;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_PARENT_ID;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_PROFILE_ID;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_TYPE;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.COMMAND_ADD;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.COMMAND_COUNT;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.COMMAND_COUNT_PROFILE_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.COMMAND_DELETE;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.COMMAND_GET;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.COMMAND_UPDATE;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_DESCRIPTION;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_INDEX;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_NAME;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_PAGE;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_PARENT_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_PROFILE_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_TYPE;
// import static org.bonitasoft.console.server.model.builder.userxp.profile.ProfileEntryItemBuilder.aProfileEntryItem;
// import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
// import static org.junit.Assert.assertEquals;
// import static org.mockito.Matchers.anyMap;
// import static org.mockito.Matchers.eq;
// import static org.mockito.Mockito.doReturn;
// import static org.mockito.Mockito.spy;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import static org.mockito.MockitoAnnotations.initMocks;
//
// import java.io.Serializable;
// import java.util.HashMap;
// import java.util.Map;
//
// import org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem;
// import org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.VALUE_TYPE;
// import org.bonitasoft.console.server.APITestWithMock;
// import org.bonitasoft.engine.api.CommandAPI;
// import org.bonitasoft.engine.command.CommandExecutionException;
// import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
// import org.junit.Before;
// import org.junit.Ignore;
// import org.junit.Test;
// import org.mockito.Mock;
//
// /**
// * @author Colin PUY
// *
// */
// @SuppressWarnings("unchecked")
// public class ProfileEntryDatastoreTest extends APITestWithMock {
//
// @Mock
// private CommandAPI commandAPI;
//
// private ProfileEntryDatastore datastore;
//
// @Before
// public void initializeMocks() throws Exception {
// initMocks(this);
//
// datastore = spy(new ProfileEntryDatastore(null));
//
// doReturn(commandAPI).when(datastore).getCommandAPI();
// }
//
// private Map<String, Serializable> anEngineItem(Long id) {
// Map<String, Serializable> engineItem = new HashMap<String, Serializable>();
// engineItem.put(PROFILE_ENTRY_ID, String.valueOf(id));
// engineItem.put(PROFILE_ENTRY_NAME, "aName");
// engineItem.put(PROFILE_ENTRY_PROFILE_ID, "2");
// engineItem.put(PROFILE_ENTRY_INDEX, "1");
// engineItem.put(PROFILE_ENTRY_TYPE, VALUE_TYPE.folder.name());
// engineItem.put(PROFILE_ENTRY_DESCRIPTION, "aDescription");
// engineItem.put(PROFILE_ENTRY_PARENT_ID, "3");
// engineItem.put(PROFILE_ENTRY_PAGE, "aPage");
// return engineItem;
// }
//
// private Map<String, Serializable> buildCommandParameters(String commandAttribute, Serializable attribute) {
// Map<String, Serializable> parameters = new HashMap<String, Serializable>();
// parameters.put(commandAttribute, attribute);
// return parameters;
// }
//
// private Map<String, String> buildFilters(String profileId) {
// Map<String, String> filters = new HashMap<String, String>();
// filters.put(ATTRIBUTE_PROFILE_ID, profileId);
// return filters;
// }
//
// private Map<String, String> buildUpdateAttributes(ProfileEntryItem item) {
// Map<String, String> attributes = new HashMap<String, String>();
// attributes.put(ATTRIBUTE_TYPE, item.getType().name());
// attributes.put(ATTRIBUTE_PAGE, item.getPage());
// attributes.put(ATTRIBUTE_NAME, item.getName());
// attributes.put(ATTRIBUTE_PARENT_ID, String.valueOf(item.getParentId()));
// attributes.put(ATTRIBUTE_PROFILE_ID, String.valueOf(item.getProfileId()));
// attributes.put(ATTRIBUTE_DESCRIPTION, item.getDescription());
// attributes.put(ATTRIBUTE_INDEX, String.valueOf(item.getIndex()));
// return attributes;
// }
//
// @Test
// public void getFetchAProfileEntryFromEngine() throws Exception {
// Map<String, Serializable> parameters = buildCommandParameters(PROFILE_ENTRY_ID, 1L);
// Map<String, Serializable> expectedEngineItem = anEngineItem(1L);
// when(commandAPI.execute(COMMAND_GET, parameters)).thenReturn((Serializable) expectedEngineItem);
//
// ProfileEntryItem fetchedItem = datastore.get(makeAPIID(1L));
//
// assertTrue(areEquals(aProfileEntryItem().fromEngineItem(expectedEngineItem).build(), fetchedItem));
// }
//
// @Test(expected = APIException.class)
// public void getThrowExceptionIfProfileEntryNotFoundInEngine() throws Exception {
// Map<String, Serializable> parameters = buildCommandParameters(PROFILE_ENTRY_ID, 1L);
// when(commandAPI.execute(COMMAND_GET, parameters)).thenThrow(CommandExecutionException.class);
//
// datastore.get(makeAPIID(1L));
// }
//
// @Test
// public void testCount() throws Exception {
// Map<String, Serializable> parameters = buildCommandParameters(COMMAND_COUNT_PROFILE_ID, 1L);
// when(commandAPI.execute(COMMAND_COUNT, parameters)).thenReturn(2L);
// Map<String, String> filters = buildFilters("1");
//
// long count = datastore.count(filters);
//
// assertEquals(2L, count);
// }
//
// @Test
// public void testAdd() throws Exception {
// Map<String, Serializable> anEngineItem = anEngineItem(1L);
// when(commandAPI.execute(eq(COMMAND_ADD), anyMap())).thenReturn((Serializable) anEngineItem);
// ProfileEntryItem expectedProfilEntryItem = aProfileEntryItem().fromEngineItem(anEngineItem).build();
//
// ProfileEntryItem updatedProfilEntryItem = datastore.add(expectedProfilEntryItem);
//
// assertTrue(areEquals(expectedProfilEntryItem, updatedProfilEntryItem));
// }
//
// @Test
// public void updateUpdateAProfileEntryInEngine() throws Exception {
// Map<String, Serializable> anEngineItem = anEngineItem(1L);
// when(commandAPI.execute(eq(COMMAND_UPDATE), anyMap())).thenReturn((Serializable) anEngineItem);
// ProfileEntryItem expectedProfilEntryItem = aProfileEntryItem().fromEngineItem(anEngineItem).build();
// Map<String, String> attributes = buildUpdateAttributes(expectedProfilEntryItem);
//
// ProfileEntryItem updatedProfilEntryItem = datastore.update(makeAPIID(1L), attributes);
//
// assertTrue(areEquals(expectedProfilEntryItem, updatedProfilEntryItem));
// }
//
// @Test(expected = APIException.class)
// public void updateThrowExceptionIfProdileEntryNotFoundInEngine() throws Exception {
// when(commandAPI.execute(eq(COMMAND_UPDATE), anyMap())).thenThrow(CommandExecutionException.class);
//
// datastore.update(makeAPIID(1L), new HashMap<String, String>());
// }
//
// @Test
// public void deleteRemoveAProfileEntryInEngine() throws Exception {
// Map<String, Serializable> expectedParameters1 = buildCommandParameters(PROFILE_ENTRY_ID, 1L);
// Map<String, Serializable> expectedParameters2 = buildCommandParameters(PROFILE_ENTRY_ID, 2L);
//
// datastore.delete(asList(makeAPIID(1L), makeAPIID(2L)));
//
// verify(commandAPI).execute(COMMAND_DELETE, expectedParameters1);
// verify(commandAPI).execute(COMMAND_DELETE, expectedParameters2);
// }
//
// @Test(expected = APIException.class)
// public void deleteThrowExceptionIfProfileEntryNotFoundInEngine() throws Exception {
// when(commandAPI.execute(eq(COMMAND_DELETE), anyMap())).thenThrow(CommandExecutionException.class);
//
// datastore.delete(asList(makeAPIID(1L)));
// }
//
// }
