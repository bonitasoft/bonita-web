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
// import static junit.framework.Assert.assertEquals;
// import static junit.framework.Assert.assertTrue;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileItem.ATTRIBUTE_DESCRIPTION;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileItem.ATTRIBUTE_NAME;
// import static org.bonitasoft.console.server.model.builder.profile.ProfileItemBuilder.aProfileItem;
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
// import java.util.List;
// import java.util.Map;
//
// import org.bonitasoft.console.client.model.userxp.profile.ProfileItem;
// import org.bonitasoft.console.server.APITestWithMock;
// import org.bonitasoft.engine.api.CommandAPI;
// import org.bonitasoft.web.toolkit.client.data.APIID;
// import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;
// import org.junit.Before;
// import org.junit.Test;
// import org.mockito.Mock;
//
// /**
// * @author Colin PUY
// *
// */
// @SuppressWarnings("unchecked")
// public class ProfileDatastoreTest extends APITestWithMock {
//
// @Mock
// private CommandAPI commandAPI;
//
// private ProfileDatastore profileDatastore;
//
// @Before
// public void initializeMocks() {
// initMocks(this);
//
// this.profileDatastore = spy(new ProfileDatastore(null));
//
// doReturn(this.commandAPI).when(this.profileDatastore).getCommandAPI();
// }
//
// private HashMap<String, Serializable> anEngineItem(final Long profilId, final String name, final String description, final String icon) {
// final HashMap<String, Serializable> engineItem = new HashMap<String, Serializable>();
// // engineItem.put(PROFILE_ID, profilId);
// // engineItem.put(PROFILE_NAME, name);
// // engineItem.put(PROFILE_DESCRITION, description);
// // engineItem.put(PROFILE_ICON, icon);
// return engineItem;
// }
//
// @Test
// public void testConvertEngineToConsoleItem() throws Exception {
// final HashMap<String, Serializable> engineItem = anEngineItem(1L, "aName", "aDescription", "anIcon");
//
// final ProfileItem profileItem = this.profileDatastore.convertEngineToConsoleItem(engineItem);
//
// assertTrue(areEquals(aProfileItem().fromEngineItem(engineItem).build(), profileItem));
// }
//
// @Test
// public void testConvertEngineToConsoleItems() throws Exception {
// final HashMap<String, Serializable> firstEngineItem = anEngineItem(1l, "aName", "aDescription", "anIcon");
// final HashMap<String, Serializable> secondEngineItem = anEngineItem(1l, "anOtherName", "anOtherDescription", "anOtherIcon");
//
// final List<ProfileItem> profileItems = this.profileDatastore.convertEngineToConsoleItems(
// asList(firstEngineItem, secondEngineItem));
//
// assertTrue(areEquals(aProfileItem().fromEngineItem(firstEngineItem).build(), profileItems.get(0)));
// assertTrue(areEquals(aProfileItem().fromEngineItem(secondEngineItem).build(), profileItems.get(1)));
// }
//
// @Test
// public void testCount() throws Exception {
// when(this.commandAPI.execute(eq(COMMAND_COUNT), anyMap())).thenReturn(2L);
//
// final long count = this.profileDatastore.count("");
//
// assertEquals(2, count);
// }
//
// @Test
// public void buildAddParametersSetAttributesIfNotBlanck() throws Exception {
// final ProfileItem item = aProfileItem().build();
// final HashMap<String, Serializable> expectedParameters = new HashMap<String, Serializable>();
// expectedParameters.put(PROFILE_NAME, item.getName());
// expectedParameters.put(PROFILE_DESCRITION, item.getDescription());
// expectedParameters.put(PROFILE_ICON, item.getIcon());
//
// final HashMap<String, Serializable> addParameters = this.profileDatastore.buildAddParameters(item);
//
// assertEquals(expectedParameters, addParameters);
// }
//
// @Test
// public void testAdd() throws Exception {
// final ProfileItem profileItem = aProfileItem().build();
// final Map<String, Serializable> expectedEngineItem =
// anEngineItem(1L, profileItem.getName(), profileItem.getDescription(), profileItem.getIcon());
// when(this.commandAPI.execute(eq(COMMAND_ADD), anyMap())).thenReturn((Serializable) expectedEngineItem);
//
// final ProfileItem addedItem = this.profileDatastore.add(profileItem);
//
// assertTrue(areEquals(aProfileItem().fromEngineItem(expectedEngineItem).build(), addedItem));
// }
//
// @Test
// public void testGet() throws Exception {
// final long anId = 1L;
// final Map<String, Serializable> expectedEngineItem = anEngineItem(anId, "aName", "aDescription", "anIcon");
// final HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
// parameters.put(PROFILE_ID, anId);
// when(this.commandAPI.execute(COMMAND_GET, parameters)).thenReturn((Serializable) expectedEngineItem);
//
// final ProfileItem profileItem = this.profileDatastore.get(APIID.makeAPIID(anId));
//
// assertTrue(areEquals(aProfileItem().fromEngineItem(expectedEngineItem).build(), profileItem));
// }
//
// @Test
// public void buildSearchParametersDontSetSearchAndOrderIfNull() throws Exception {
// final Map<String, Serializable> expectedParameters = new HashMap<String, Serializable>();
// expectedParameters.put(COMMAND_SEARCH_INDEX, 0);
// expectedParameters.put(COMMAND_SEARCH_NUMBER, 10);
//
// final Map<String, Serializable> searchParameters = this.profileDatastore.buildSearchParameters(0, 10, null, null);
//
// assertEquals(expectedParameters, searchParameters);
// }
//
// @Test
// public void buildSearchParametersDontSetSearchIfNotNull() throws Exception {
// final Map<String, Serializable> expectedParameters = new HashMap<String, Serializable>();
// expectedParameters.put(COMMAND_SEARCH_INDEX, 0);
// expectedParameters.put(COMMAND_SEARCH_NUMBER, 10);
// expectedParameters.put(COMMAND_SEARCH_SEARCH, "name = colin");
//
// final Map<String, Serializable> searchParameters = this.profileDatastore.buildSearchParameters(0, 10, "name = colin", null);
//
// assertEquals(expectedParameters, searchParameters);
// }
//
// @Test
// public void buildSearchParametersCanDetectSearchFieldAndOrder() throws Exception {
// final Map<String, Serializable> expectedParameters = new HashMap<String, Serializable>();
// expectedParameters.put(COMMAND_SEARCH_INDEX, 0);
// expectedParameters.put(COMMAND_SEARCH_NUMBER, 10);
// expectedParameters.put(COMMAND_SEARCH_FIELD, "name");
// expectedParameters.put(COMMAND_SEARCH_ORDER, "desc");
//
// final Map<String, Serializable> searchParameters = this.profileDatastore.buildSearchParameters(0, 10, null, "name desc");
//
// assertEquals(expectedParameters, searchParameters);
// }
//
// @Test
// public void buildSearchParametersDefaultOrderIsAsc() throws Exception {
// final Map<String, Serializable> expectedParameters = new HashMap<String, Serializable>();
// expectedParameters.put(COMMAND_SEARCH_INDEX, 0);
// expectedParameters.put(COMMAND_SEARCH_NUMBER, 10);
// expectedParameters.put(COMMAND_SEARCH_FIELD, "name");
// expectedParameters.put(COMMAND_SEARCH_ORDER, "ASC");
//
// final Map<String, Serializable> searchParameters = this.profileDatastore.buildSearchParameters(0, 10, null, "name");
//
// assertEquals(expectedParameters, searchParameters);
// }
//
// @Test
// public void searchCanBeFilteredByUserId() throws Exception {
// HashMap<String, Serializable> profile1 = anEngineItem(1L, "aName", "aDescription", "anIcon");
// HashMap<String, Serializable> profile2 = anEngineItem(2L, "anOtherName", "anOtherDescription", "anOtherIcon");
// when(commandAPI.execute(eq(ProfileMemberEngineDatastoreUtils.COMMAND_GET), anyMap())).thenReturn((Serializable) asList(profile1, profile2));
// HashMap<String, String> filter = new HashMap<String, String>();
// filter.put(ProfileItem.FILTER_USER_ID, "1");
//
// final ItemSearchResult<ProfileItem> searchResult = this.profileDatastore.search(0, 10, null, null, filter);
//
// assertEquals(2, searchResult.getTotal());
// assertItemEquals(aProfileItem().fromEngineItem(profile1).build(), searchResult.getResults().get(0));
// assertItemEquals(aProfileItem().fromEngineItem(profile2).build(), searchResult.getResults().get(1));
// }
//
// @Test
// public void buildUpdateParametersSetAttributesIfNotBlanck() throws Exception {
// // Empty
// final Map<String, Serializable> expectedParameters = new HashMap<String, Serializable>();
// expectedParameters.put(ProfileEngineDatastoreUtils.PROFILE_ID, 1L);
// final Map<String, String> attributes = new HashMap<String, String>();
//
// Map<String, Serializable> updateParameters = this.profileDatastore.buildUpdateParameters(APIID.makeAPIID("1"), attributes);
//
// assertEquals(expectedParameters, updateParameters);
//
// // Name
// expectedParameters.put(PROFILE_NAME, "aName");
// attributes.put(ATTRIBUTE_NAME, "aName");
//
// updateParameters = this.profileDatastore.buildUpdateParameters(APIID.makeAPIID("1"), attributes);
//
// assertEquals(expectedParameters, updateParameters);
//
// // Description
// expectedParameters.put(PROFILE_DESCRITION, "aDecription");
// attributes.put(ATTRIBUTE_DESCRIPTION, "aDecription");
//
// updateParameters = this.profileDatastore.buildUpdateParameters(APIID.makeAPIID("1"), attributes);
//
// assertEquals(expectedParameters, updateParameters);
//
// // Icon
// expectedParameters.put(PROFILE_ICON, "anIcon");
// attributes.put(ProfileItem.ATTRIBUTE_ICON, "anIcon");
//
// updateParameters = this.profileDatastore.buildUpdateParameters(APIID.makeAPIID("1"), attributes);
//
// assertEquals(expectedParameters, updateParameters);
// }
//
// @Test
// public void testUpdate() throws Exception {
// final Map<String, Serializable> updatedEngineItem = anEngineItem(1L, "updatedName", "updatedDescription", "updatedIcon");
// when(this.commandAPI.execute(eq(COMMAND_UPDATE), anyMap())).thenReturn((Serializable) updatedEngineItem);
//
// final ProfileItem updatedItem = this.profileDatastore.update(APIID.makeAPIID("1"), new HashMap<String, String>());
//
// assertTrue(areEquals(aProfileItem().fromEngineItem(updatedEngineItem).build(), updatedItem));
// }
//
// @Test
// public void testDelete() throws Exception {
// final APIID firstId = APIID.makeAPIID("1");
// final APIID secondId = APIID.makeAPIID("2");
// final List<APIID> idsTobeDeleted = asList(firstId, secondId);
//
// this.profileDatastore.delete(idsTobeDeleted);
//
// verify(this.commandAPI).execute(COMMAND_DELETE, idParameter(firstId));
// verify(this.commandAPI).execute(COMMAND_DELETE, idParameter(secondId));
// }
//
// private Map<String, Serializable> idParameter(final APIID firstId) {
// final Map<String, Serializable> addParameters = new HashMap<String, Serializable>();
// addParameters.put(PROFILE_ID, firstId.toLong());
// return addParameters;
// }
// }
