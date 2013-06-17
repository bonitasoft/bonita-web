package org.bonitasoft.web.rest.server.api.profile;
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
// package org.bonitasoft.console.server.api.userXP.profile;
//
// import static java.util.Arrays.asList;
// import static junit.framework.Assert.assertEquals;
// import static junit.framework.Assert.assertTrue;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileItem.ATTRIBUTE_DESCRIPTION;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileItem.ATTRIBUTE_NAME;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEngineDatastoreUtils.COMMAND_ADD;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEngineDatastoreUtils.COMMAND_GET;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEngineDatastoreUtils.PROFILE_DESCRITION;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEngineDatastoreUtils.PROFILE_ICON;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEngineDatastoreUtils.PROFILE_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEngineDatastoreUtils.PROFILE_NAME;
// import static org.bonitasoft.console.server.model.builder.profile.ProfileItemBuilder.aProfileItem;
// import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
// import static org.bonitasoft.web.toolkit.client.data.item.template.ItemHasIcon.ATTRIBUTE_ICON;
// import static org.junit.Assert.assertNull;
//
// import java.io.Serializable;
// import java.util.HashMap;
// import java.util.Map;
//
// import org.bonitasoft.console.client.model.userxp.profile.ProfileItem;
// import org.bonitasoft.console.server.AbstractConsoleTest;
// import org.bonitasoft.engine.api.CommandAPI;
// import org.bonitasoft.engine.api.TenantAPIAccessor;
// import org.bonitasoft.engine.command.CommandExecutionException;
// import org.bonitasoft.test.toolkit.organization.TestUser;
// import org.bonitasoft.test.toolkit.organization.TestUserFactory;
// import org.bonitasoft.web.toolkit.client.data.APIID;
// import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;
// import org.junit.Ignore;
// import org.junit.Test;
//
// /**
// * @author Colin PUY
// *
// */
// public class APIProfileIntegrationTest extends AbstractConsoleTest {
//
// private APIProfile apiProfile;
//
// @Override
// public void consoleTestSetUp() throws Exception {
// apiProfile = new APIProfile();
// apiProfile.setCaller(getAPICaller(getInitiator().getSession(), "API/userXP/profile"));
// }
//
// @Override
// protected TestUser getInitiator() {
// return TestUserFactory.getJohnCarpenter();
// }
//
// @Deprecated
// @SuppressWarnings("unchecked")
// private ProfileItem addAProfileToEngine(ProfileItem profileItem) throws Exception {
// Map<String, Serializable> cmdParameters = new HashMap<String, Serializable>();
// cmdParameters.put(PROFILE_NAME, profileItem.getName());
// cmdParameters.put(PROFILE_DESCRITION, profileItem.getDescription());
// cmdParameters.put(PROFILE_ICON, profileItem.getIcon());
//
// long addedId = (Long) ((Map<String, Serializable>) getCommandAPI().execute(COMMAND_ADD, cmdParameters)).get(PROFILE_ID);
// profileItem.setId(makeAPIID(addedId));
// return profileItem;
// }
//
// @SuppressWarnings("unchecked")
// private ProfileItem getAProfileFromEngine(APIID apiid) throws Exception {
// Map<String, Serializable> cmdParameters = new HashMap<String, Serializable>();
// cmdParameters.put(PROFILE_ID, apiid.toLong());
// try {
// return aProfileItem().fromEngineItem((Map<String, Serializable>) getCommandAPI().execute(COMMAND_GET, cmdParameters)).build();
// } catch (CommandExecutionException e) {
// return null;
// }
// }
//
// private CommandAPI getCommandAPI() throws Exception {
// return TenantAPIAccessor.getCommandAPI(getInitiator().getSession());
// }
//
// @Ignore("bos engine api does'nt support to add a profile. Need to import config")
// @Test
// public void testGet() throws Exception {
// ProfileItem expectedItem = addAProfileToEngine(aProfileItem().build());
//
// ProfileItem fetchedItem = apiProfile.runGet(expectedItem.getId(), null, null);
//
// assertTrue(areEquals(expectedItem, fetchedItem));
// }
//
// @Ignore("bos engine api does'nt support to add a profile. Need to import config")
// @Test
// public void testAdd() throws Exception {
//
// ProfileItem addedItem = apiProfile.runAdd(aProfileItem().build());
//
// assertTrue(areEquals(getAProfileFromEngine(addedItem.getId()), addedItem));
// }
//
// @Ignore("bos engine api does'nt support to add a profile. Need to import config")
// @Test
// public void testDelete() throws Exception {
// ProfileItem addedProfil = addAProfileToEngine(aProfileItem().build());
//
// apiProfile.runDelete(asList(addedProfil.getId()));
//
// assertNull(getAProfileFromEngine(addedProfil.getId()));
// }
//
// @Ignore("bos engine api does'nt support to add a profile. Need to import config")
// @Test
// public void testUpdate() throws Exception {
// ProfileItem addedProfil = addAProfileToEngine(aProfileItem().build());
// Map<String, String> attributes = buildUpdateAttributes("newName", "newDescription", "newIcon");
//
// ProfileItem updatedItem = apiProfile.runUpdate(addedProfil.getId(), attributes);
//
// ProfileItem fetchedItem = getAProfileFromEngine(addedProfil.getId());
// assertItemEquals(fetchedItem, updatedItem);
// assertEquals(fetchedItem.getName(), "newName");
// assertEquals(fetchedItem.getDescription(), "newDescription");
// assertEquals(fetchedItem.getIcon(), "newIcon");
// }
//
// private Map<String, String> buildUpdateAttributes(String name, String description, String icon) {
// Map<String, String> attributes = new HashMap<String, String>();
// attributes.put(ATTRIBUTE_NAME, name);
// attributes.put(ATTRIBUTE_DESCRIPTION, description);
// attributes.put(ATTRIBUTE_ICON, icon);
// return attributes;
// }
//
// @Test
// // @Ignore("until ENGINE-830 resolved")
// @Ignore("bos engine api does'nt support to add a profile. Need to import config")
// @SuppressWarnings("unused")
// public void searchSearchBeginOfNameAndDecriptionAttributes() throws Exception {
// ProfileItem expectedProfile = addAProfileToEngine(aProfileItem().withName("aName").withDescription("coucou aDescription").build());
// ProfileItem expectedProfile2 = addAProfileToEngine(aProfileItem().withName("coucou aName").build());
// ProfileItem notExpectedProfile = addAProfileToEngine(aProfileItem().withName("notcoucou name").withDescription("notcoucou aDescription").build());
//
// ItemSearchResult<ProfileItem> searchResult = apiProfile.runSearch(0, 10, "coucou", null, null, null, null);
//
// assertEquals(2L, searchResult.getTotal());
// assertItemEquals(expectedProfile, searchResult.getResults().get(0));
// assertItemEquals(expectedProfile2, searchResult.getResults().get(1));
// }
//
// @Test
// @Ignore("bos engine api does'nt support to add a profile. Need to import config")
// @SuppressWarnings("unused")
// public void searchCanBePaginatedAndDefaultSortIsNameAsc() throws Exception {
// ProfileItem expectedProfile = addAProfileToEngine(aProfileItem().withName("aAName").build());
// ProfileItem expectedProfile2 = addAProfileToEngine(aProfileItem().withName("aBName").build());
//
// ItemSearchResult<ProfileItem> searchResult = apiProfile.runSearch(0, 1, null, null, null, null, null);
//
// assertEquals(2L, searchResult.getTotal());
// assertEquals(1L, searchResult.getResults().size());
// assertItemEquals(expectedProfile, searchResult.getResults().get(0));
// }
// }
