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
package org.bonitasoft.console.server.model.builder.profile;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.impl.ProfileImpl;

/**
 * @author Vincent Elcrin
 * 
 */
public class EngineProfileBuilder {

    private String name;
    private String description;
    private String iconPath;

    public static EngineProfileBuilder anEngineProfile() {
        return new EngineProfileBuilder();
    }

    public EngineProfileBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EngineProfileBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public EngineProfileBuilder withIconPath(String path) {
        this.iconPath = path;
        return this;
    }

    public Profile build() {
        ProfileImpl profile = new ProfileImpl(name);
        profile.setDescription(description);
        profile.setIconPath(iconPath);
        return profile;
    }

}
