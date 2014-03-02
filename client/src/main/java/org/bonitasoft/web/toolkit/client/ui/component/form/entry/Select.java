/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.XML;
import org.bonitasoft.web.toolkit.client.ui.html.XMLAttributes;

import com.google.gwt.user.client.Element;

/**
 * A standard HTML Select component of the USerXP toolkit.<br>
 * Display Select Element.<br>
 * Will generate an "Select" HTML tag, that will received the corresponding BOS "css" style.
 * 
 * @author Julien Mege
 */
public class Select extends FormEntry {

    private final ArrayList<Option> options = new ArrayList<Option>();

    protected String rootElement = "SELECT";

    private Element select;

    public Select(final JsId jsid, final String label, final String tooltip) {
        this(jsid, label, tooltip, null, (Option[]) null);
    }

    public Select(final JsId jsid, final String label, final String tooltip, final String description) {
        this(jsid, label, tooltip, description, (Option[]) null);
    }

    public Select(final JsId jsid, final String label, final String tooltip, final String description, final Option... options) {
        super(jsid, label, tooltip, description);
        addClass("select");
        if (options != null) {
            for (final Option option : options) {
                this.options.add(option);
            }
        }
    }

    /**
     * Generate the DOM Element corresponding to the current Select Element.
     */
    @Override
    protected Element makeInput(final String uid) {
        this.select = XML.makeElement(getHtml());
        return this.select;
    }

    private String getHtml() {
        String html = HTML.select(getJsId().toString(), new XMLAttributes("title", this.tooltip).add("id", this.uid));

        for (final Option option : this.options) {
            html += option.getHtml();
        }

        html += HTML._select();

        return html;
    }

    public void refreshOptions(final List<Option> options) {
        if (this.select != null) {
            while (this.select.hasChildNodes()) {
                this.select.getFirstChild().removeFromParent();
            }

            for (final Option option : options) {
                this.select.appendChild(option.getElement());
            }
        }
    }

    @Override
    public String _getValue() {
        return __getValue(this.select);
    }

    @Override
    public void _setValue(final String value) {
        selectOption(this.select, value);
    }

    private native String __getValue(Element e)
    /*-{
        return $wnd.$(e).val();
    }-*/;

    private native void selectOption(final Element select, final String value)
    /*-{
         $wnd.$('[selected]', select).removeAttr('selected');
         $wnd.$('option[value="' + value + '"]', select).attr('selected', 'selected');
    }-*/;

}
