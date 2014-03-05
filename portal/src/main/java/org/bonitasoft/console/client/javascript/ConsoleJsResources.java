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

import org.bonitasoft.console.client.javascript.libs.DateFormat;
import org.bonitasoft.console.client.javascript.libs.DatePicker;
import org.bonitasoft.console.client.javascript.libs.Highlight;
import org.bonitasoft.console.client.javascript.libs.JQuery;
import org.bonitasoft.console.client.javascript.libs.JQueryPlus;
import org.bonitasoft.console.client.javascript.libs.Noty;
import org.bonitasoft.console.client.javascript.libs.PLUpload;
import org.bonitasoft.console.client.javascript.libs.Portal;
import org.bonitasoft.console.client.javascript.libs.jqueryplugins.ColorAnimation;
import org.bonitasoft.console.client.javascript.libs.jqueryplugins.CustomInput;
import org.bonitasoft.console.client.javascript.libs.jqueryplugins.JQueryFlot;
import org.bonitasoft.console.client.javascript.libs.jqueryplugins.JQueryForm;
import org.bonitasoft.console.client.javascript.libs.jqueryplugins.JQueryPseudo;
import org.bonitasoft.console.client.javascript.libs.jqueryplugins.Placeholder;

/**
 * @author Vincent Elcrin
 * 
 */
public class ConsoleJsResources {

    public static List<Lib> asList() {
        return Arrays.<Lib> asList(
                // new Less(),
                new JQuery(),
                new PLUpload(),
                new Lib(new JsResource(DateFormat.INSTANCE.js())),
                new Lib(new JsResource(DatePicker.INSTANCE.js())),
                new Lib(new JsResource(ColorAnimation.INSTANCE.js())),
                new Lib(new JsResource(JQueryForm.INSTANCE.js())),
                new Lib(new JsResource(JQueryPseudo.INSTANCE.js())),
                new JQueryPlus(),
                new Lib(new JsResource(CustomInput.INSTANCE.js())),
                new Portal(),
                new Lib(new JsResource(Highlight.INSTANCE.js())),
                new Lib(new JsResource(Noty.INSTANCE.js())),
                new JQueryFlot(),
                new Lib(new JsResource(Placeholder.INSTANCE.js()))
                );
    }
}
