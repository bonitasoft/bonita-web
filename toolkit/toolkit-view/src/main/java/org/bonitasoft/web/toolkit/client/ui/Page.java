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
package org.bonitasoft.web.toolkit.client.ui;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.List;

import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.Title;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

import com.google.gwt.user.client.Element;

/**
 * This class define a Page, which is a specific type of view.
 * 
 * @author Julien Mege, Séverin Moussel, Paul Amar
 */
public abstract class Page extends View {

    private Container<Clickable> toolbar = null;

    private Title title = null;

    private final Container<Link> actions = new Container<Link>(new JsId("actions"));

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Page() {
        super();

        // FIXME dont set footer when it is a quickdetails page
        final Text footerTxt = new Text("Bonitasoft © 2014 "+ _("All rights reserved") + ".");
        footerTxt.getElement().setAttribute("id", "footer");
        super.setFooter(footerTxt);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // HTML GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Generate the DOM Element corresponding to the current Page.
     */
    @Override
    protected void _fillWidget(final Element rootElement) {
        this.rootClassName = "page";

        defineTitle();
        assert this.title != null : "Missing page title on " + this.getClass().toString().replaceAll(".*\\.", "");

        super._fillWidget(rootElement);

        // BACK
        // if (this instanceof BackablePage) {
        // rootElement.addClassName("backpage");
        //
        // $(rootElement).prepend(
        // $(HTML.div(new HTMLClass("back"))).append(
        // new Link(new JsId("back"), _("Back"), _("Go back to previous view"), new HistoryBackAction()).getElement()
        // )
        // );
        // }

    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected Page addAction(final JsId jsid, final String label, final String tooltip, final Action action) {
        this.actions.append(new Link(jsid, label, tooltip, action));
        return this;
    }

    protected Page addAction(final String label, final String tooltip, final Action action) {
        return this.addAction(null, label, tooltip, action);
    }

    /**
     * Add a list of Components in the current view.
     * 
     * @param components
     *            The list of Components to add in the current view.
     */
    @Override
    protected void setHeader(final AbstractComponent... components) {
        addHeader(components);
    }

    /**
     * @param title
     *            the title to set
     */
    protected void setTitle(final String title) {
        this.setTitle(title, (Component[]) null);
    }

    /**
     * @return the title
     */
    public Title getTitle() {
        return this.title;
    }

    /**
     * Set the page title. If the page is already displayed, the displayed title will be updated.<br />
     * The title format works as a Text component using the "%%" template model
     * 
     * @param title
     * @param components
     */
    protected void setTitle(final String title, final AbstractComponent... components) {
        if (this.title == null) {
            this.title = new Title(title, components);
        } else {
            this.title.setText(title, components);
        }
    }

    private void initToolbar() {
        if (this.toolbar == null) {
            this.toolbar = new ContainerStyled<Clickable>();
            this.toolbar.setRootTagClass("toolbar");

            // add toolbar to the header
            addHeader(this.toolbar);
        }
    }

    /**
     * Set the toolbar, empty it if already declared, and add all the links
     * 
     * @param links
     */
    protected void setToolbar(final Link... links) {
        initToolbar();

        this.toolbar.empty();
        this.toolbar.append(links);
    }

    /**
     * add a toolbar Button with the following arguments :
     * 
     * @param label
     *            label of the button
     * @param tooltip
     *            tooltip for the button
     * @param action
     *            action for the button
     * @deprecated Use {@code addToolbarLink(Clickable link)}
     */
    @Deprecated()
    protected void addToolbarButton(final String label, final String tooltip, final Action action) {
        initToolbar();

        this.toolbar.append(new Button(new JsId(label), label, tooltip, action));
    }

    /**
     * add a toolbar Link with the following arguments :
     * 
     * @param label
     *            label for the link
     * @param tooltip
     *            tooltip for the link
     * @param action
     *            action for the link
     * @deprecated Use {@code addToolbarLink(Clickable link)}
     */
    @Deprecated()
    protected void addToolbarLink(final JsId id, final String label, final String tooltip, final Action action) {
        initToolbar();
        this.toolbar.append(new Link(id, label, tooltip, action));
    }

    protected void addToolbarLink(final Clickable link) {
        initToolbar();
        this.toolbar.append(link);
    }

    @Override
    protected List<Element> makeHeaderElements(final Container<AbstractComponent> header) {
        if (!"popup".equals(this.parentElement.getId())) {
            header.prepend(this.actions);
        }

        header.prepend(this.title);

        // MOVED TO TOOLBAR INITIALIZATION
        // add the toolbar to the header if not null
        // if (this.toolbar != null) {
        // header.prepend(this.toolbar);
        // }

        return super.makeHeaderElements(header);
    }

    @Override
    protected List<Element> makeFooterElements(final Container<AbstractComponent> footer) {
        if ("popup".equals(this.parentElement.getId())) {
            footer.prepend(this.actions);
        }
        return super.makeFooterElements(footer);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ABSTRACTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This function must be override to define the title to display for the current view.<br />
     * <br />
     * <strong style="color:red">The rules to write a title :</strong>
     * <ul>
     * <li>Title mustn't be empty</li>
     * <li>Title mustn't be NULL</li>
     * <li>Title must be explicit for an end user</li>
     * <li>Title must be clear even if it will be changed by an API call</li>
     * </ul>
     */
    public abstract void defineTitle();

    @Override
    protected void refreshAll() {
        if (this.toolbar != null) {
            this.toolbar.empty();
        }
        super.refreshAll();
    }
}
