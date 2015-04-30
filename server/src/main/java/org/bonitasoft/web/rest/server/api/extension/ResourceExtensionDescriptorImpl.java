/**
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.extension;

/**
 * @author Matthieu Chaffotte
 */
public class ResourceExtensionDescriptorImpl implements ResourceExtensionDescriptor {

    private String pathTemplate;

    private String method;

    private String pageName;

    private final String classFileName;

    public ResourceExtensionDescriptorImpl(String pathTemplate, String method, String pageName, String classFileName) {
        super();
        this.pathTemplate = pathTemplate;
        this.method = method;
        this.pageName = pageName;
        this.classFileName = classFileName;
    }

    @Override
    public String getPathTemplate() {
        return pathTemplate;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPageName() {
        return pageName;
    }

    @Override
    public String getClassFileName() {
        return classFileName;
    }

}
