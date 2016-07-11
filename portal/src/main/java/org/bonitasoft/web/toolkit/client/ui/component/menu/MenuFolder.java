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
package org.bonitasoft.web.toolkit.client.ui.component.menu;

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Image;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 */
public class MenuFolder extends Component implements MenuItem {

    private String label = null;

    private Image image = null;

    protected final ContainerStyled<MenuItem> items = new ContainerStyled<MenuItem>();

    public MenuFolder(final String label) {
        this(null, label, null, (MenuItem[]) null);
    }

    public MenuFolder(final String label, final Image image) {
        this(null, label, image, (MenuItem[]) null);
    }

    public MenuFolder(final JsId jsid, final String label) {
        this(jsid, label, null, (MenuItem[]) null);
    }

    public MenuFolder(final JsId jsid, final String label, final Image image) {
        this(jsid, label, image, (MenuItem[]) null);
    }

    public MenuFolder(final String label, final MenuItem... menuItems) {
        this(null, label, null, menuItems);
    }

    public MenuFolder(final JsId jsid, final String label, final MenuItem... menuItems) {
        this(jsid, label, null, menuItems);
    }

    public MenuFolder(final String label, final Image image, final MenuItem... menuItems) {
        this(null, label, image, menuItems);
    }

    public MenuFolder(final JsId jsid, final String label, final Image image, final MenuItem... menuItems) {
        super(jsid);
        this.label = label;
        this.image = image;

        if (menuItems != null) {
            addMenuItem(menuItems);
        }
    }

    public MenuFolder addMenuItem(final MenuItem... menuItems) {
        this.items.append(menuItems);

        return this;
    }

    public MenuFolder addFolder(final MenuFolder... folers) {
        return addMenuItem(folers);
    }

    public MenuFolder addLink(final MenuLink... links) {
        return addMenuItem(links);
    }

    @Override
    protected Element makeElement() {
        this.items.setRootTagName("ul");
        this.items.setWrapTag(null, null);

        final GQuery root = $("<li>");
        root.addClass("menuitem");
        if (getJsId() != null) {
            root.addClass(getJsId().toString("menuitem"));
        }

        GQuery link = null;;
        if (this.image != null || this.label != null && this.label.length() > 0) {
            link = $("<a href=\"#\">" + this.label + "</a>");
            root.append(link);

            if (this.label != null && this.label.length() > 0) {
                link.text(this.label);
            }

            if (this.image != null) {
                link.prepend(this.image.getElement());
            }

        }

        appendComponentToHtml((Element) root.get(0), this.items);

        return (Element) root.get(0);
    }

    public void clear() {
        this.items.empty();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setLabel(final String label) {
        this.label = label;

        if (isGenerated()) {
            final GQuery link = $(this.element).children("a");
            final GQuery image = link.find("img");

            link.text(label);
            if (image.length() > 0) {
                link.prepend(image);
            }
        }
    }

    public void setImage(final Image image) {
        this.image = image;

        if (isGenerated()) {

            final GQuery link = $(this.element).children("a");
            final GQuery img = link.find("img");

            if (image == null) {
                img.remove();
            } else if (img.length() > 0) {
                img.replaceWith(image.getElement());
            } else {
                link.prepend(image.getElement());
            }
        }
    }

}
