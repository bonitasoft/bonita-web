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

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.console.client.javascript.injector.JsInjector;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.gwt.resources.client.TextResource;

/**
 * @author Vincent Elcrin
 * 
 */
public class JavaScriptsTest {

    @Mock
    JsInjector injector = Mockito.mock(JsInjector.class);

    @Mock
    TextResource resource = Mockito.mock(TextResource.class);

    @Test
    @Ignore("Code is commented in class")
    public void testAllResorceFormLibAreInjected() {
        List<Lib> libs = aBunchOfNotVariantLibs();
        JavaScripts javascripts = new JavaScripts(libs);

        javascripts.inject();

        Mockito.verify(injector, Mockito.times(libs.size())).inject(Mockito.anyString());
    }

    private List<Lib> aBunchOfNotVariantLibs() {
        return Arrays.asList(new Lib(new JsResource(injector, resource)),
                new Lib(new JsResource(injector, resource)),
                new Lib(new JsResource(injector, resource)));
    }
}
