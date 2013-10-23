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
package org.bonitasoft.web.toolkit.client.ui.component;

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Define a complex component of the userXP toolkit. <br>
 * A section must have a title, and is composed by a header, a body and by a footer.
 * 
 * @author Julien Mege
 */
public class Section extends Component {

    private final Container<AbstractComponent> header = new Container<AbstractComponent>();

    private boolean headerIncluded = false;

    private final Container<AbstractComponent> body = new Container<AbstractComponent>();

    private boolean bodyIncluded = false;

    private final Container<AbstractComponent> footer = new Container<AbstractComponent>();

    private boolean footerIncluded = false;

    private String className;

    private Container<Paragraph> descriptionContainer = null;

    public Section(final JsId jsId) {
        this(jsId, (String) null, (AbstractComponent) null);
    }

    public Section(final JsId jsId, final String title) {
        this(jsId, title, (AbstractComponent) null);
    }

    public Section(final JsId jsId, final AbstractComponent... body) {
        this(jsId, null, body);
    }

    public Section(final String title) {
        this(null, title, (AbstractComponent) null);
    }

    public Section(final String title, final AbstractComponent... body) {
        this(null, title, body);
    }

    public Section(final JsId jsId, final String title, final AbstractComponent... body) {
        super(jsId);
        if (title != null) {
            this.header.append(new Title(title));
        }
        if (body != null && body.length > 0) {
            this.body.append(body);
        }
        className = CssClass.SECTION;
    }

    /**
     * Generate the DOM Element corresponding to the current Section.
     */
    @Override
    protected final Element makeElement() {

        this.element = DOM.createDiv();
        this.element.addClassName(getClassName());

        if (getJsId() != null) {
            this.element.addClassName(getJsId().toString("section"));
        }

        this.header.setRootTag("div", "header");
        if (this.header.size() > 0) {
            this.element.appendChild(this.header.getElement());
            this.headerIncluded = true;
        }
        this.body.setRootTag("div", "body");
        if (this.body.size() > 0) {
            if (this.descriptionContainer != null) {
                this.element.appendChild(descriptionContainer.getElement());
            }
            this.element.appendChild(this.body.getElement());
            this.bodyIncluded = true;
        }
        this.footer.setRootTag("div", "footer");
        if (this.footer.size() > 0) {
            this.element.appendChild(this.footer.getElement());
            this.footerIncluded = true;
        }

        return this.element;
    }

    /**
     * @return
     */
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public final Section empty() {
        return emptyHeader()
                .emptyBody()
                .emptyFooter();
    }

    /**
     * Add a list of Components in the Header part of the section.
     * 
     * @param components
     *            The list of Components to add in the header part of the section.
     */
    public final Section addHeader(final AbstractComponent... components) {
        this.header.append(components);

        if (isGenerated() && !this.headerIncluded) {
            HTML.prepend(this.element, this.header.getElement());
            this.headerIncluded = true;
        }

        return this;
    }

    public final Section emptyHeader() {
        this.header.empty();
        return this;
    }

    /**
     * Add a list of Components in the Body part of the section.
     * 
     * @param components
     *            The list of Components to add in the Body part of the section.
     */
    public final Section addBody(final AbstractComponent... components) {
        this.body.append(components);
 
        if (isGenerated() && !this.bodyIncluded) {
            if (!this.headerIncluded) {
                HTML.prepend(this.element, this.body.getElement());
            } else {
                $(this.element).children(".header").after(this.body.getElement());
            }

            this.bodyIncluded = true;
        }

        return this;
    }

    public final Section emptyBody() {
        this.body.empty();
        return this;
    }

    /**
     * Add a list of Components in the Footer part of the section.
     * 
     * @param components
     *            The list of Components to add in the Footer part of the section.
     */
    public final Section addFooter(final AbstractComponent... components) {
        this.footer.append(components);

        if (isGenerated() && !this.footerIncluded) {
            HTML.append(this.element, this.footer.getElement());
            this.footerIncluded = true;
        }

        return this;
    }

    public final Section emptyFooter() {
        this.footer.empty();
        return this;
    }

    public final void addDescription(final String description) {
        if (this.descriptionContainer == null) {
            this.descriptionContainer = new Container<Paragraph>(new JsId("section_description"));
        }

        this.descriptionContainer.append(new Paragraph(description));
    }

}
