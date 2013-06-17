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
// import static org.bonitasoft.console.client.model.userxp.profile.ProfileMemberItem.ATTRIBUTE_PROFILE_ID;
// import static org.bonitasoft.console.server.model.builder.profile.ProfileItemBuilder.aProfileItem;
// import static org.bonitasoft.console.server.model.builder.profile.member.ProfileMemberItemBuilder.aProfileMemberItem;
// import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
// import static org.junit.Assert.assertNull;
//
// import java.io.Serializable;
// import java.util.HashMap;
// import java.util.Map;
//
// import org.bonitasoft.console.client.model.userxp.profile.ProfileItem;
// import org.bonitasoft.console.client.model.userxp.profile.ProfileMemberItem;
// import org.bonitasoft.console.server.AbstractConsoleTest;
// import org.bonitasoft.console.server.datastore.ComposedDatastore;
// import org.bonitasoft.console.server.datastore.profile.member.SearchProfileMembersHelper;
// import org.bonitasoft.console.server.engineclient.EngineAPIAccessor;
// import org.bonitasoft.console.server.engineclient.EngineClientFactory;
// import org.bonitasoft.engine.api.CommandAPI;
// import org.bonitasoft.engine.api.TenantAPIAccessor;
// import org.bonitasoft.test.toolkit.organization.TestGroup;
// import org.bonitasoft.test.toolkit.organization.TestGroupFactory;
// import org.bonitasoft.test.toolkit.organization.TestRole;
// import org.bonitasoft.test.toolkit.organization.TestRoleFactory;
// import org.bonitasoft.test.toolkit.organization.TestUser;
// import org.bonitasoft.test.toolkit.organization.TestUserFactory;
// import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
// import org.bonitasoft.web.toolkit.client.data.APIID;
// import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;
// import org.junit.Test;
//
// /**
// * @author Colin PUY
// *
// */
// public class APIProfileMemberIntegrationTest extends AbstractConsoleTest {
//
// private APIProfileMember apiProfileMember;
//
// private long anEngineProfileId;
//
// private long anEngineUserId;
//
// @Override
// public void consoleTestSetUp() throws Exception {
// this.apiProfileMember = new APIProfileMember();
// this.apiProfileMember.setCaller(getAPICaller(getInitiator().getSession(), "API/userXP/profileMember"));
//
// this.anEngineProfileId = addToEngine(aProfileItem().withName("defaultProfil").build()).getId().toLong();
// this.anEngineUserId = getInitiator().getId();
// }
//
// @Override
// protected TestUser getInitiator() {
// return TestUserFactory.getJohnCarpenter();
// }
//
// private ProfileMemberItem addToEngine(final ProfileMemberItem item) throws Exception {
// return getProfileMemberDatastore().add(item);
// }
//
// private ComposedDatastore<ProfileMemberItem> getProfileMemberDatastore() {
// return (ComposedDatastore) this.apiProfileMember.defineDefaultDatastore();
// }
//
// @SuppressWarnings("unchecked")
// private ProfileItem addToEngine(final ProfileItem profileItem) throws Exception {
// final Map<String, Serializable> cmdParameters = new HashMap<String, Serializable>();
// cmdParameters.put(ProfileEngineDatastoreUtils.PROFILE_NAME, profileItem.getName());
// cmdParameters.put(ProfileEngineDatastoreUtils.PROFILE_DESCRITION, profileItem.getDescription());
// cmdParameters.put(ProfileEngineDatastoreUtils.PROFILE_ICON, profileItem.getIcon());
//
// final long addedId = (Long) ((Map<String, Serializable>) getCommandAPI().execute(COMMAND_ADD, cmdParameters))
// .get(ProfileEngineDatastoreUtils.PROFILE_ID);
// profileItem.setId(makeAPIID(addedId));
// return profileItem;
// }
//
// private CommandAPI getCommandAPI() throws Exception {
// return TenantAPIAccessor.getCommandAPI(getInitiator().getSession());
// }
//
// // FIXME to be replaced with engine access
// private ProfileMemberItem getAProfileMemberFromEngineForUser(final APIID id, final long profileId) throws Exception {
// final Map<String, String> filters = buildSearchFilters(profileId, ProfileMemberItem.VALUE_MEMBER_TYPE_USER);
// final ItemSearchResult<ProfileMemberItem> search = new SearchProfileMembersHelper(
// new EngineClientFactory(new EngineAPIAccessor())
// .createProfileMemberEngineClient(getInitiator().getSession()))
// .search(0, Integer.MAX_VALUE, null, null, filters);
// for (final ProfileMemberItem item : search.getResults()) {
// if (item.getId().toLong() == id.toLong()) {
// return item;
// }
// }
// return null;
// }
//
// private Map<String, String> buildSearchFilters(final long profileId, final String memberType) {
// final Map<String, String> filters = new HashMap<String, String>();
// filters.put(ATTRIBUTE_PROFILE_ID, String.valueOf(profileId));
// filters.put(ProfileMemberItem.FILTER_MEMBER_TYPE, memberType);
// return filters;
// }
//
// @Test
// public void testAdd() throws Exception {
// final ProfileMemberItem profileMemberToBeAdded =
// aProfileMemberItem().withProfileId(this.anEngineProfileId).withUserId(this.anEngineUserId).build();
//
// final ProfileMemberItem addedItem = this.apiProfileMember.runAdd(profileMemberToBeAdded);
//
// final ProfileMemberItem expectedItem = getAProfileMemberFromEngineForUser(addedItem.getId(), this.anEngineProfileId);
// assertItemEquals(addedItem, expectedItem);
// }
//
// @Test
// public void testDelete() throws Exception {
// final ProfileMemberItem addedItem = addToEngine(
// aProfileMemberItem().withProfileId(this.anEngineProfileId).withUserId(this.anEngineUserId).build());
//
// this.apiProfileMember.runDelete(asList(addedItem.getId()));
//
// final ProfileMemberItem deletedItem = getAProfileMemberFromEngineForUser(addedItem.getId(), this.anEngineProfileId);
// assertNull(deletedItem);
// }
//
// @Test
// public void searchCanBeFilteredByProfileIdAndMemberType() throws Exception {
// final ProfileMemberItem addedItem = addToEngine(
// aProfileMemberItem().withProfileId(this.anEngineProfileId).withUserId(this.anEngineUserId).build());
// final Map<String, String> filters = buildSearchFilters(this.anEngineProfileId, ProfileMemberItem.VALUE_MEMBER_TYPE_USER);
//
// final ItemSearchResult<ProfileMemberItem> searchResults = this.apiProfileMember.runSearch(0, 10, null, null, filters, null, null);
//
// assertItemEquals(addedItem, searchResults.getResults().get(0));
// }
//
// @Test(expected = APIForbiddenException.class)
// public void addingTwiceUserOnSameProfileIsForbidden() throws Exception {
// ProfileMemberItem profileMember = aProfileMemberItem().withProfileId(anEngineProfileId).withUserId(anEngineUserId).build();
//
// apiProfileMember.add(profileMember);
// apiProfileMember.add(profileMember);
// }
//
// @Test(expected = APIForbiddenException.class)
// public void addingTwiceGroupOnSameProfileIsForbidden() throws Exception {
// TestGroup rAndD = TestGroupFactory.getRAndD();
// ProfileMemberItem profileMember = aProfileMemberItem().withProfileId(anEngineProfileId).withGroupId(rAndD.getId()).build();
//
// apiProfileMember.add(profileMember);
// apiProfileMember.add(profileMember);
// }
//
// @Test(expected = APIForbiddenException.class)
// public void addingTwiceRoleOnSameProfileIsForbidden() throws Exception {
// TestRole role = TestRoleFactory.getDeveloper();
// ProfileMemberItem profileMember = aProfileMemberItem().withProfileId(anEngineProfileId).withRoleId(role.getId()).build();
//
// apiProfileMember.add(profileMember);
// apiProfileMember.add(profileMember);
// }
//
// @Test(expected = APIForbiddenException.class)
// public void addingTwiceMembershipOnSameProfileIsForbidden() throws Exception {
// TestRole role = TestRoleFactory.getDeveloper();
// TestGroup group = TestGroupFactory.getRAndD();
// ProfileMemberItem profileMember = aProfileMemberItem().withProfileId(anEngineProfileId).withRoleId(role.getId()).withGroupId(group.getId()).build();
//
// apiProfileMember.add(profileMember);
// apiProfileMember.add(profileMember);
// }
// }
