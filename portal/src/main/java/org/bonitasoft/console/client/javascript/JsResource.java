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

import org.bonitasoft.console.client.javascript.injector.JsInjector;

import com.google.gwt.resources.client.TextResource;

/**
 * @author Vincent Elcrin
 * 
 */
public class JsResource {

    private final TextResource resource;

    private final JsInjector injector;

    public JsResource(JsInjector injector, TextResource resource) {
        this.injector = injector;
        this.resource = resource;
    }

    public JsResource(TextResource resource) {
        this(new JsInjector(), resource);
    }

    public String getScript() {
        return resource.getText();
    }

    public void inject() {
        injector.inject(getScript());
    }
}
