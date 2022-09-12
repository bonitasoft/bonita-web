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
package org.bonitasoft.test.toolkit.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bonitasoft.engine.identity.GroupCreator;

/**
 * @author Séverin Moussel
 * 
 */
public class TestGroupFactory {

    private static final String NAME_R_AND_D = "RetD";

    private static final String DESCRIPTION_R_AND_D = "The team that drinks all the coffee";

    private static final String NAME_WEB = "Web";

    private static final String DESCRIPTION_WEB = "The team that also drinks all the beer";

    private final Map<String, TestGroup> groupList;

    private static TestGroupFactory instance;

    /**
     * Default Constructor.
     */
    public TestGroupFactory() {
        this.groupList = new HashMap<String, TestGroup>();
    }

    public static TestGroupFactory getInstance() {
        if (instance == null) {
            instance = new TestGroupFactory();
        }
        return instance;
    }

    public void clear() throws Exception {
        for (TestGroup testGroup : this.groupList.values()) {
            testGroup.delete();
        }
        this.groupList.clear();
    }

    /**
     * @return the userList
     */
    private Map<String, TestGroup> getGroupList() {
        return this.groupList;
    }

    public static List<TestGroup> createRandomGroups(final int nbOfGroups) {
        final List<TestGroup> results = new ArrayList<TestGroup>();

        for (int i = 0; i < nbOfGroups; i++) {
            results.add(createGroup(getRandomString(), getRandomString()));
        }

        return results;
    }

    private static String getRandomString() {
        return String.valueOf(new Random().nextLong());
    }

    // ////////////////////////////////////////////////////////////////////////////////
    // / Group's creation
    // ////////////////////////////////////////////////////////////////////////////////

    /**
     * @param name
     * @param description
     * @return
     */
    public static TestGroup createGroup(final String name, final String description) {
        if (getInstance().getGroupList().get(name) == null) {
            final GroupCreator groupBuilder = new GroupCreator(name).setDescription(description);

            getInstance().getGroupList().put(name, new TestGroup(groupBuilder));
        }

        return getInstance().getGroupList().get(name);
    }

    public static TestGroup getRAndD() {
        return createGroup(NAME_R_AND_D, DESCRIPTION_R_AND_D);
    }

    public static TestGroup getWeb() {
        return createGroup(NAME_WEB, DESCRIPTION_WEB);
    }
    public void check() {
        if (!getGroupList().isEmpty()) {
            throw new RuntimeException(this.getClass().getName() + " cannot be reset because the list is not empty: " + getGroupList());
        }
    }
}
