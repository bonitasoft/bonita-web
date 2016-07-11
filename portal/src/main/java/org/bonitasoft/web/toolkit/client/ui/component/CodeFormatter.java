/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.ui.component;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A code formatter component, that allows to display text using the chosen language syntax (java, html, xml, javascript)
 * 
 * @author Séverin Moussel
 */
public class CodeFormatter extends Component {

    public static final String LANGUAGE_JAVA = "java";

    public static final String LANGUAGE_HTML = "html";

    public static final String LANGUAGE_XML = "xml";

    public static final String LANGUAGE_JAVASCRIPT = "javascript";

    private String code = null;

    private String language = null;

    public CodeFormatter(final JsId id, final String code, final String language) {
        super(id);
        this.code = code;
        this.language = language;
    }

    public CodeFormatter(final JsId id, final String code) {
        super(id);
        this.code = code;
    }

    public CodeFormatter(final String code, final String language) {
        super(null);
        this.code = code;
        this.language = language;
    }

    public CodeFormatter(final String code) {
        super(null);
        this.code = code;
    }

    /**
     * Generate the DOM Element corresponding to the current CodeFormatter.
     */
    @Override
    protected Element makeElement() {
        final Element pre = DOM.createElement("pre");
        final Element code = DOM.createElement("code");
        if (this.language != null) {
            code.addClassName(this.language);
        }

        code.setInnerText(this.code);
        pre.appendChild(code);
        return pre;
    }

}
