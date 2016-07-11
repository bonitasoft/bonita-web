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
package org.bonitasoft.web.toolkit.client.ui;

import static com.google.gwt.query.client.GQuery.$;

import java.util.List;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.ToolkitException;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;

import com.google.gwt.user.client.Element;

/**
 * This class is the super class of all different view that we have to display. A view is a group of component, with a state defined by a list of parameters.
 * When a view is loaded, it call automatically the refresh of the components that are waiting for reload in the view controller.<br>
 * A view implementation need to define a public static String parameter, named TOKEN.<br>
 * This TOKEN will be used in the URL, to access the view.
 * 
 * @author Séverin Moussel
 */
public abstract class View extends RawView {

    private final Container<AbstractComponent> header = new Container<AbstractComponent>();

    private final Container<AbstractComponent> footer = new Container<AbstractComponent>();

    protected String rootClassName = "view";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public View() {
        final String token = defineToken();
        if (token == null || token.isEmpty()) {
            throw new ToolkitException("Token is missing for [" + this.getClass().getName() + "]");
        }
        setToken(token);

        final String jsid = defineJsId();
        if (jsid == null || jsid.isEmpty()) {
            throw new ToolkitException("JsId is missing for [" + this.getClass().getName() + "]");
        }
        setJsId(jsid);

        this.body.setRootTag("div", "body");
        this.header.setRootTag("div", "header");
        this.footer.setRootTag("div", "footer");
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DOM GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Generate the DOM Element corresponding to the current View.
     */
    @Override
    protected void _fillWidget(final Element rootElement) {
        rootElement.addClassName(this.rootClassName);
        rootElement.addClassName(getJsId().toString(this.rootClassName));

        HTML.append(rootElement, makeHeaderElements(this.header));

        HTML.append(rootElement, this.body.getElement());

        HTML.append(rootElement, makeFooterElements(this.footer));

        // Build the view
        buildView();
    }

    @Override
    protected void refreshAll() {

        ViewController.showView(getToken(), this.parentElement, getParameters());

        // this.header.empty();
        // this.body.empty();
        // this.footer.empty();
        // fillWidget();
    }

    protected List<Element> makeHeaderElements(final Container<AbstractComponent> header) {
        return header.getElements();
    }

    protected List<Element> makeFooterElements(final Container<AbstractComponent> footer) {
        return footer.getElements();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // BODY AND HEADER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a list of Components in the Header of the current view.
     * 
     * @param components
     *            The list of Components to add to the Header of the current view.
     */
    protected final void addHeader(final AbstractComponent... components) {
        this.header.append(components);
        generateHeader();
    }

    /**
     * Add a list of Components in the current view.
     * 
     * @param components
     *            The list of Components to add in the current view.
     */
    protected void setHeader(final AbstractComponent... components) {
        this.header.empty().append(components);
        generateHeader();
    }

    /**
     * Generate header only if header hasn't been generated before
     */
    private void generateHeader() {
        if (this.generated && !this.header.isGenerated()) {
            final Element headerElement = this.header.getElement();
            $(this.body.getElement()).before(headerElement);
        }
    }

    /**
     * Add a list of Components in the Header of the current view.
     * 
     * @param components
     *            The list of Components to add to the Header of the current view.
     */
    protected final void addFoot(final AbstractComponent... components) {
        this.footer.append(components);
        if (this.generated && !this.footer.isGenerated()) {
            final Element footerElement = this.footer.getElement();
            $(this.body.getElement()).after(footerElement);
        }
    }

    /**
     * Add a list of Components in the current view.
     * 
     * @param components
     *            The list of Components to add in the current view.
     */
    protected void setFooter(final AbstractComponent... components) {
        this.footer.empty().append(components);
        generateFooter();
    }

    /**
     * Generate footer only if footer hasn't been generated before
     */
    private void generateFooter() {
        if (this.generated && !this.footer.isGenerated()) {
            final Element footerElement = this.footer.getElement();
            footerElement.setAttribute("id", "footer");
            $(this.body.getElement()).after(footerElement);
        }
    }

}
