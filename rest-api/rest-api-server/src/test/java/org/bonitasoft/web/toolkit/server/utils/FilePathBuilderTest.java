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
package org.bonitasoft.web.toolkit.server.utils;

import static junit.framework.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

/**
 * @author Colin PUY
 * 
 */
public class FilePathBuilderTest {

    @Test
    public void testAppend() throws Exception {
        String path1 = "org", path2 = "bonita", path3 = "web/service";

        FilePathBuilder path = new FilePathBuilder(path1).append(path2).append(path3);

        assertEquals(buildExpectedPath(path1, path2, path3), path.toString());
    }

    private String buildExpectedPath(String root, String... paths) {
        StringBuilder expected = new StringBuilder(root);
        for (String path : paths) {
            addPath(expected, path);
        }
        return expected.toString();
    }

    private void addPath(StringBuilder expected, String path) {
        expected.append(File.separator);
        expected.append(path);
    }
}
