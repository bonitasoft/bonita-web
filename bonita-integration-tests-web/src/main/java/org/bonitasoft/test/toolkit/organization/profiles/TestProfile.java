/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.test.toolkit.organization.profiles;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.bonitasoft.test.toolkit.organization.TestGroup;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.utils.CommandCaller;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestProfile {

    /**
     * Item fields
     */
    public static final String PROFILE_ID = "id";

    private static final String PROFILE_NAME = "name";

    private static final String PROFILE_DESCRITION = "description";

    private static final String PROFILE_ICON = "iconPath";

    /**
     * Commands
     */
    private static final String COMMAND_ADD = "addProfile";

    private static final String COMMAND_GET = "getProfile";

    private long profileId = -1L;

    private final CommandCaller commandCaller;

    TestProfile(CommandCaller commandCaller) {
        this.commandCaller = commandCaller;
    }

    public TestProfile create(String name, String description, String icon) {
        profileId = MapUtils.getLongValue(createProfile(name, description, icon), PROFILE_ID);
        return this;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Serializable> createProfile(String name, String description, String icon) {
        return (Map<String, Serializable>) commandCaller.run(COMMAND_ADD, createAddCommandParameters(name, description, icon));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Serializable> createAddCommandParameters(String name, String description, String icon) {
        Map parameters = new HashMap<>();
        MapUtils.safeAddToMap(parameters, PROFILE_NAME, name);
        MapUtils.safeAddToMap(parameters, PROFILE_DESCRITION, description);
        MapUtils.safeAddToMap(parameters, PROFILE_ICON, icon);
        return parameters;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Serializable> fetchItem() {
        return (Map<String, Serializable>) commandCaller.run(COMMAND_GET, createGetCommandParameters());
    }

    private Map<String, Serializable> createGetCommandParameters() {
        return Collections.singletonMap(PROFILE_ID, profileId);
    }

    public long getId() {
        return profileId;
    }

    public TestProfileMember addMember(TestUser user) {
        return new TestProfileMember(commandCaller, this)
                .setUserId(user.getId())
                .create();
    }

    public TestProfileMember addMember(TestGroup group) {
        return new TestProfileMember(commandCaller, this)
                .setGroupId(group.getId())
                .create();
    }

}
