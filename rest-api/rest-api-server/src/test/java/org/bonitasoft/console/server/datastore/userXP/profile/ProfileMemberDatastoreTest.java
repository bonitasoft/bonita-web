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
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileMemberEngineDatastoreUtils.COMMAND_DELETE;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileMemberEngineDatastoreUtils.GROUP_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileMemberEngineDatastoreUtils.PROFILE_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileMemberEngineDatastoreUtils.ROLE_ID;
// import static org.bonitasoft.console.server.datastore.userXP.profile.ProfileMemberEngineDatastoreUtils.USER_ID;
// import static org.bonitasoft.console.server.model.builder.userxp.profile.MemberItemBuilder.aProfileMemberItem;
// import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
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
// import org.bonitasoft.console.client.model.userxp.profile.ProfileMemberItem;
// import org.bonitasoft.console.server.APITestWithMock;
// import org.bonitasoft.engine.api.CommandAPI;
// import org.bonitasoft.engine.command.CommandExecutionException;
// import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
// import org.bonitasoft.web.toolkit.client.data.APIID;
// import org.junit.Before;
// import org.junit.Test;
// import org.mockito.Mock;
//
// /**
// * @author Colin PUY
// *
// */
// // TODO to be finished
// @SuppressWarnings("unchecked")
// public class ProfileMemberDatastoreTest extends APITestWithMock {
//
// @Mock
// private CommandAPI commandAPI;
//
// private ProfileMemberDatastore datastore;
//
// // FIXME : make a composite id when id in engine is deleted
// private static final APIID anAPIID = makeAPIID("1");
//
// @Before
// public void initializeMocks() throws Exception {
// initMocks(this);
//
// datastore = spy(new ProfileMemberDatastore(null));
//
// doReturn(commandAPI).when(datastore).getCommandAPI();
// }
//
// private HashMap<String, Serializable> buildDefaultProfileMember() {
// HashMap<String, Serializable> item = new HashMap<String, Serializable>();
// item.put(PROFILE_ID, "1");
// item.put(USER_ID, "2");
// item.put(ROLE_ID, "3");
// item.put(GROUP_ID, "4");
// return item;
// }
//
// @Test
// public void testConvertEngineToConsoleItem() throws Exception {
// HashMap<String, Serializable> profileMember = buildDefaultProfileMember();
// ProfileMemberItem expectedItem = aProfileMemberItem().from(profileMember).build();
//
// ProfileMemberItem convertedItem = datastore.convertEngineToConsoleItem(profileMember);
//
// // FIXME : to be deleted after engine modifications
// expectedItem.setAttribute("id", "null");
// assertEquals(expectedItem.getAttributes(), convertedItem.getAttributes());
// }
//
// @Test(expected = APIException.class)
// public void deleteAnUnknownProcessSupervisorThrowException() throws Exception {
// Map<String, Serializable> expectedParameters = datastore.buildDeleteCommandParameters(anAPIID);
// when(commandAPI.execute(COMMAND_DELETE, expectedParameters)).thenThrow(CommandExecutionException.class);
//
// datastore.delete(asList(makeAPIID(1L)));
// }
//
// @Test
// public void testDelete() throws Exception {
// Map<String, Serializable> expectedParameters = datastore.buildDeleteCommandParameters(anAPIID);
//
// datastore.delete(asList(anAPIID));
//
// verify(commandAPI).execute(COMMAND_DELETE, expectedParameters);
// }
// }
