/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.custom;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Matthieu Chaffotte
 */
public class SpringTenantBeanAccessor {

	private AbsolutePathFileSystemXmlApplicationContext context;

	public SpringTenantBeanAccessor(File directory) {
		final File restFile = new File(directory, "restAPI.xml");
		if (restFile.exists()) {
			context = new AbsolutePathFileSystemXmlApplicationContext(new String [] {restFile.getPath()}, true, null);
		}
	}

	public List<CustomResourceDescriptor> getRestConfiguration() {
		if (context == null) {
			return Collections.emptyList();
		}
		return new ArrayList<CustomResourceDescriptor>(context.getBeansOfType(CustomResourceDescriptor.class).values());
	}

}
