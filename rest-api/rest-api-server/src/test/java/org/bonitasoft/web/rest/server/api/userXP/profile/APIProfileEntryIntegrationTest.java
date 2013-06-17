package org.bonitasoft.web.rest.server.api.userXP.profile;
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
// import static junit.framework.Assert.assertNotNull;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_DESCRIPTION;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_INDEX;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_NAME;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_PAGE;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_PARENT_ID;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_PROFILE_ID;
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.ATTRIBUTE_TYPE;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.COMMAND_ADD;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.COMMAND_GET;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_DESCRIPTION;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_INDEX;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_NAME;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_PAGE;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_PARENT_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_PROFILE_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileEntryEngineDatastoreUtils.PROFILE_ENTRY_TYPE;
// import static org.bonitasoft.console.server.model.builder.profile.ProfileItemBuilder.aProfileItem;
// import static org.bonitasoft.console.server.model.builder.userxp.profile.ProfileEntryItemBuilder.aProfileEntryItem;
// import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
// import static org.junit.Assert.assertNull;
//
// import java.io.Serializable;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Map;
//
// import org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem;
// import org.bonitasoft.console.client.model.userxp.profile.ProfileEntryItem.VALUE_TYPE;
// import org.bonitasoft.console.client.model.userxp.profile.ProfileItem;
// import org.bonitasoft.console.server.AbstractConsoleTest;
// import org.bonitasoft.console.server.datastore.userXP.profile.ProfileEngineDatastoreUtils;
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
// public class APIProfileEntryIntegrationTest extends AbstractConsoleTest {
//
// private APIProfileEntry apiProfileEntry;
//
// private long anEngineProfileId;
//
// @Override
// public void consoleTestSetUp() throws Exception {
// this.apiProfileEntry = new APIProfileEntry();
// this.apiProfileEntry.setCaller(getAPICaller(getInitiator().getSession(), "API/userXP/profileEntry"));
//
// this.anEngineProfileId = addToEngine(aProfileItem().withName("defaultProfil").build()).getId().toLong();
// }
//
// @Override
// protected TestUser getInitiator() {
// return TestUserFactory.getJohnCarpenter();
// }
//
// @SuppressWarnings("unchecked")
// private ProfileEntryItem addToEngine(final ProfileEntryItem item) throws Exception {
// return aProfileEntryItem().fromEngineItem(
// (Map<String, Serializable>) getCommandAPI().execute(COMMAND_ADD, buildAddCommandParameters(item))).build();
// }
//
// @SuppressWarnings("unchecked")
// private ProfileItem addToEngine(final ProfileItem profileItem) throws Exception {
// final Map<String, Serializable> cmdParameters = new HashMap<String, Serializable>();
// cmdParameters.put(ProfileEngineDatastoreUtils.PROFILE_NAME, profileItem.getName());
// cmdParameters.put(ProfileEngineDatastoreUtils.PROFILE_DESCRITION, profileItem.getDescription());
// cmdParameters.put(ProfileEngineDatastoreUtils.PROFILE_ICON, profileItem.getIcon());
//
// final long addedId = (Long) ((Map<String, Serializable>) getCommandAPI().execute(ProfileEngineDatastoreUtils.COMMAND_ADD, cmdParameters))
// .get(ProfileEngineDatastoreUtils.PROFILE_ID);
// profileItem.setId(makeAPIID(addedId));
// return profileItem;
// }
//
// private Map<String, Serializable> buildAddCommandParameters(final ProfileEntryItem item) {
// final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
// parameters.put(PROFILE_ENTRY_TYPE, item.getType().name());
// parameters.put(PROFILE_ENTRY_PAGE, item.getPage());
// parameters.put(PROFILE_ENTRY_NAME, item.getName());
// if (item.getParentId() != null) {
// parameters.put(PROFILE_ENTRY_PARENT_ID, item.getParentId().toLong());
// }
// parameters.put(PROFILE_ENTRY_PROFILE_ID, item.getProfileId().toLong());
// parameters.put(PROFILE_ENTRY_DESCRIPTION, item.getDescription());
// parameters.put(PROFILE_ENTRY_INDEX, (long) item.getIndex());
// return parameters;
// }
//
// @SuppressWarnings("unchecked")
// private ProfileEntryItem getAProfileEntryFromEngine(final APIID id) throws Exception {
// final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
// parameters.put(PROFILE_ENTRY_ID, id.toLong());
// try {
// return aProfileEntryItem().fromEngineItem((Map<String, Serializable>) getCommandAPI().execute(COMMAND_GET, parameters)).build();
// } catch (final CommandExecutionException e) {
// return null;
// }
// }
//
// private Map<String, String> buildUpdateAttributes(final String type, final String page, final String name, final String parentId,
// final String descritption, final String index) {
// final Map<String, String> attributes = new HashMap<String, String>();
// attributes.put(ATTRIBUTE_TYPE, type);
// attributes.put(ATTRIBUTE_PAGE, page);
// attributes.put(ATTRIBUTE_NAME, name);
// attributes.put(ATTRIBUTE_PARENT_ID, parentId);
// attributes.put(ATTRIBUTE_DESCRIPTION, descritption);
// attributes.put(ATTRIBUTE_INDEX, index);
// return attributes;
// }
//
// private ArrayList<String> buildDeployAllAttributes() {
// final ArrayList<String> deploys = new ArrayList<String>();
// deploys.add(ATTRIBUTE_PROFILE_ID);
// deploys.add(ATTRIBUTE_PARENT_ID);
// return deploys;
// }
//
// private Map<String, String> buildFilters(final long profileId) {
// return Collections.singletonMap(ATTRIBUTE_PROFILE_ID, String.valueOf(profileId));
// }
//
// private CommandAPI getCommandAPI() throws Exception {
// return TenantAPIAccessor.getCommandAPI(getInitiator().getSession());
// }
//
// @Test
// @Ignore("until ENGINE-831 is resolved")
// public void testGet() throws Exception {
// final ProfileEntryItem expectedItem = addToEngine(aProfileEntryItem().withProfileId(this.anEngineProfileId).build());
//
// final ProfileEntryItem fetchedItem = this.apiProfileEntry.runGet(expectedItem.getId(), null, null);
//
// assertItemEquals(expectedItem, fetchedItem);
// }
//
// @Test
// public void testGethWithDeploys() throws Exception {
// final ProfileEntryItem parentProfileEntry = addToEngine(aProfileEntryItem().withProfileId(this.anEngineProfileId).build());
// final ProfileEntryItem profileEntry = addToEngine(aProfileEntryItem().withParentId(parentProfileEntry.getId().toLong())
// .withProfileId(this.anEngineProfileId).build());
// final ArrayList<String> deploys = buildDeployAllAttributes();
//
// final ProfileEntryItem searchResult = this.apiProfileEntry.runGet(profileEntry.getId(), deploys, null);
//
// assertNotNull(searchResult.getProfile());
// assertNotNull(searchResult.getParentEntry());
// }
//
// @Test
// public void getWithDeploysReturnANullParentEntryIfProfilEntryHAsNoParent() throws Exception {
// final ProfileEntryItem profileEntry = addToEngine(aProfileEntryItem().withParentId(0L).withProfileId(this.anEngineProfileId).build());
// final ArrayList<String> deploys = buildDeployAllAttributes();
//
// final ProfileEntryItem searchResult = this.apiProfileEntry.runGet(profileEntry.getId(), deploys, null);
//
// assertNull(searchResult.getParentEntry());
// }
//
// @Test
// @Ignore("until ENGINE-831 is resolved")
// public void testAdd() throws Exception {
//
// final ProfileEntryItem addedItem = this.apiProfileEntry.runAdd(aProfileEntryItem().withProfileId(this.anEngineProfileId).build());
//
// assertItemEquals(getAProfileEntryFromEngine(addedItem.getId()), addedItem);
// }
//
// @Test
// public void testDelete() throws Exception {
// final ProfileEntryItem addedItem = addToEngine(aProfileEntryItem().withProfileId(this.anEngineProfileId).build());
//
// this.apiProfileEntry.runDelete(asList(addedItem.getId()));
//
// assertNull(getAProfileEntryFromEngine(addedItem.getId()));
// }
//
// @Test
// @Ignore("until ENGINE-831 is resolved")
// public void testUpdate() throws Exception {
// final ProfileEntryItem addedProfil = addToEngine(aProfileEntryItem().withProfileId(this.anEngineProfileId).build());
// final Map<String, String> attributes = buildUpdateAttributes(VALUE_TYPE.link.name(), "newPage", "newName", "2", "newDescription", "4");
//
// final ProfileEntryItem updatedItem = this.apiProfileEntry.runUpdate(addedProfil.getId(), attributes);
//
// final ProfileEntryItem fetchedItem = getAProfileEntryFromEngine(addedProfil.getId());
// assertItemEquals(fetchedItem, updatedItem);
// assertEquals(fetchedItem.getType(), VALUE_TYPE.link);
// assertEquals(fetchedItem.getPage(), "newPage");
// assertEquals(fetchedItem.getParentId(), makeAPIID(2L));
// assertEquals(fetchedItem.getProfileId(), makeAPIID(3L));
// assertEquals(fetchedItem.getDescription(), "newDescription");
// assertEquals((int) fetchedItem.getIndex(), 4);
// }
//
// @Test
// @SuppressWarnings("unused")
// @Ignore("until ENGINE-831 is resolved")
// public void searchCanBeFiltered() throws Exception {
// final ProfileEntryItem expectedEntry = addToEngine(aProfileEntryItem().withProfileId(this.anEngineProfileId).withName("aName").build());
// final ProfileEntryItem expectedEntry2 = addToEngine(aProfileEntryItem().withProfileId(this.anEngineProfileId).withName("anOtherName").build());
// final long anotherProfilId = addToEngine(aProfileItem().withName("anOtherProfil").build()).getId().toLong();
// final ProfileEntryItem notExpectedEntry = addToEngine(aProfileEntryItem().withProfileId(anotherProfilId).build());
//
// final ItemSearchResult<ProfileEntryItem> searchResult =
// this.apiProfileEntry.runSearch(0, 10, null, null, buildFilters(this.anEngineProfileId), null, null);
//
// assertItemEquals(expectedEntry, searchResult.getResults().get(0));
// assertItemEquals(expectedEntry2, searchResult.getResults().get(1));
// assertEquals(2L, searchResult.getTotal());
// }
// }
