/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component;

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.dropdown.DropDownItem;
import org.bonitasoft.web.toolkit.client.ui.component.dropdown.DropDownPanel;

import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class Select extends Component {

    private int selectedIndex = 0;

    private Link label = null;

    private final DropDownPanel dropdown = new DropDownPanel();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Select() {
        this(null, null);
    }

    public Select(final JsId jsid) {
        this(jsid, null);
    }

    public Select(final String defaultLabel) {
        this(null, defaultLabel);
    }

    public Select(final JsId jsid, final String defaultLabel) {
        super(jsid);
        this.label = new Link(defaultLabel, defaultLabel, new Action() {

            @Override
            public void execute() {
                Select.this.dropdown.toggle();
            }
        });
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DROPDOWN DELEGATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param label
     * @param tooltip
     * @param action
     * @return This method returns the Select itself to allow cascading calls
     * @see org.bonitasoft.web.toolkit.client.ui.component.dropdown.DropDownPanel#add(java.lang.String, java.lang.String,
     *      org.bonitasoft.web.toolkit.client.ui.action.Action)
     */
    public Select addOption(final String label, final String tooltip, final Action action) {
        this.dropdown.add(label, tooltip, action);
        return this;
    }

    /**
     * @param label
     * @param tooltip
     * @param token
     * @return This method returns the Select itself to allow cascading calls
     * @see org.bonitasoft.web.toolkit.client.ui.component.dropdown.DropDownPanel#add(java.lang.String, java.lang.String, java.lang.String)
     */
    public Select addOption(final String label, final String tooltip, final String token) {
        this.dropdown.add(label, tooltip, token);
        return this;
    }

    /**
     * @param label
     * @param tooltip
     * @param token
     * @param parameters
     * @return This method returns the Select itself to allow cascading calls
     * @see org.bonitasoft.web.toolkit.client.ui.component.dropdown.DropDownPanel#add(java.lang.String, java.lang.String, java.lang.String,
     *      org.bonitasoft.web.toolkit.client.common.TreeIndexed)
     */
    public Select addOption(final String label, final String tooltip, final String token, final TreeIndexed<String> parameters) {
        this.dropdown.add(label, tooltip, token, parameters);
        return this;
    }

    public Select addOption(final String label, final String tooltip, final Action action, final boolean selected) {
        this.addOption(label, tooltip, action);
        if (selected) {
            this.selectedIndex = this.dropdown.size() - 1;
        }
        return this;
    }

    public Select addOption(final String label, final String tooltip, final String token, final boolean selected) {
        this.addOption(label, tooltip, token);
        if (selected) {
            this.selectedIndex = this.dropdown.size() - 1;
        }
        return this;
    }

    public Select addOption(final String label, final String tooltip, final String token, final TreeIndexed<String> parameters, final boolean selected) {
        this.addOption(label, tooltip, token, parameters);
        if (selected) {
            this.selectedIndex = this.dropdown.size() - 1;
        }
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ITEM SELECTION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Select setSelectedIndex(final int index) {

        this.selectedIndex = index;

        if (isGenerated()) {
            updateLabel();
        }

        return this;
    }

    /**
     * @return
     */
    private String getSelectedLabel() {
        return this.dropdown.size() > this.selectedIndex ? this.dropdown.get(this.selectedIndex).getLabel() : "...";
    }

    private void setLabel(final String label) {
        this.label.setLabel(label);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DOM GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected Element makeElement() {

        // Prepare root element
        addClass("select");
        this.element = DOM.createDiv();

        // Prepare label
        updateLabel();

        // Prepare dropdown
        for (int i = 0; i < this.dropdown.size(); i++) {

            final int index = i;
            final DropDownItem item = this.dropdown.getComponents().get(i);

            $(item.getElement()).click(new Function() {

                @Override
                public void f() {
                    super.f();
                    Select.this.setSelectedIndex(index);
                }

            });
        }

        // Construct element hierarchy
        this.element.appendChild(this.label.getElement());
        this.element.appendChild(this.dropdown.getElement());

        return this.element;
    }

    private void updateLabel() {
        setLabel(getSelectedLabel());
    }

}
