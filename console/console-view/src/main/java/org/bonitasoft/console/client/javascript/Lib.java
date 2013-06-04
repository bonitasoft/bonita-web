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
package org.bonitasoft.console.client.javascript;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Vincent Elcrin
 * 
 */
public class Lib {

    private List<JsResource> resources = new LinkedList<JsResource>();
    
    public Lib(JsResource... resources) {
        addResources(resources);
    }

    public void inject() {
        for (JsResource resource : getResources()) {
            injectResource(resource);
        }
    }

    private void addResources(JsResource... resources) {
        for (JsResource resource : resources) {
            this.resources.add(resource);
        }
    }

    private List<JsResource> getResources() {
        return this.resources;
    }

    private void injectResource(JsResource resource) {
        resource.inject();
    }

}
