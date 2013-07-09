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

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.utils.CommandAPIAccessor;
import org.bonitasoft.test.toolkit.utils.CommandCaller;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestProfileFactory {

    private String name = "profile";

    private String description = "description";

    private String icon = "icon";

    private APISession session;

    private TestProfileFactory(APISession session) {
        this.session = session;
    }

    public TestProfileFactory withName(String name) {
        this.name = name;
        return this;
    }

    public TestProfileFactory withDescription(String description) {
        this.description = description;
        return this;
    }

    public TestProfileFactory withIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public static TestProfileFactory newProfile(APISession session) {
        return new TestProfileFactory(session);
    }

    public TestProfile create() {
        return new TestProfile(createCommandCaller()).create(name, description, icon);
    }

    private CommandCaller createCommandCaller() {
        return new CommandCaller(getCommandAPI());
    }

    protected CommandAPI getCommandAPI() {
        return new CommandAPIAccessor(session).getCommandAPI();
    }
}
