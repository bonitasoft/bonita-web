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
package org.bonitasoft.web.toolkit.client.ui.component.containers;

import static com.google.gwt.query.client.GQuery.$;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.DoubleSection;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.core.Node;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.HTMLClass;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 */
public class Container<T extends Node> extends Component {

    private final HashMap<String, T> componentsIndexes = new HashMap<String, T>();

    protected final LinkedList<T> components = new LinkedList<T>();

    protected String rootTagName = "div";

    protected String rootTagClass = null;

    protected String wrapTagName = null;

    protected String wrapTagClass = null;

    private Integer incrementDefaultId = 0;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Container() {
        super(null);
        this.initTagNames();
    }

    public Container(final JsId jsid) {
        super(jsid);
        this.initTagNames();
    }

    public Container(final T... components) {
        this(null, components);
    }

    public Container(final JsId jsid, final T... components) {
        this(jsid);
        this.append(components);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // MANAGE CONTENT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Container<T> empty() {
        for (final T item : this.components) {
            if (item instanceof KnowsContainer) {
                ((KnowsContainer) item).setContainer(null);
            }
        }

        this.components.clear();
        if (isGenerated()) {
            $(element).empty();
            this.incrementDefaultId = 0;
        }
        return this;
    }

    public Container<T> append(final T... components) {
        int i = 0;
        for (final T component : components) {
            if (component == null) {
                continue;
            }

            if (component instanceof DoubleSection) {
                i = 0;
            } else if (component instanceof Section) {
                if (i % 2 == 0) {
                    ((Section) component).addClass("even");
                } else {
                    ((Section) component).addClass("odd");
                }
                i++;
            }

            if (component.getJsId() == null) {
                this.componentsIndexes.put("componentUID" + this.incrementDefaultId.toString(), component);
                this.incrementDefaultId++;
            } else {
                this.componentsIndexes.put(component.getJsId().toString(), component);
            }
            this.components.add(component);

            if (component instanceof KnowsContainer) {
                ((KnowsContainer) component).setContainer(this);
            }

            if (isGenerated()) {
                this.appendItemToHtml(component);
                removeClass("empty");
            }
        }
        return this;

    }

    public Container<T> prepend(final T... components) {
        for (final T component : components) {
            if (component.getJsId() == null) {
                this.componentsIndexes.put("componentUID" + this.incrementDefaultId.toString(), component);
                this.incrementDefaultId++;
            } else {
                this.componentsIndexes.put(component.getJsId().toString(), component);
            }
            this.components.addFirst(component);

            if (component instanceof KnowsContainer) {
                ((KnowsContainer) component).setContainer(this);
            }

            if (isGenerated()) {
                this.prependItemToHtml(component);
            }
        }
        return this;
    }

    public Container<T> insert(final int index, final T... components) {
        if (index <= 0) {
            return this.prepend(components);
        }

        int nextIndex = index;
        Element prev = null;
        if (isGenerated()) {
            prev = (Element) $(element).children().get(index - 1);
        }

        for (final T component : components) {
            if (component == null) {
                continue;
            }

            if (component.getJsId() == null) {
                this.componentsIndexes.put("componentUID" + this.incrementDefaultId.toString(), component);
                this.incrementDefaultId++;
            } else {
                this.componentsIndexes.put(component.getJsId().toString(), component);
            }
            this.components.add(nextIndex++, component);

            if (component instanceof KnowsContainer) {
                ((KnowsContainer) component).setContainer(this);
            }

            if (isGenerated()) {
                this.insertItemAfter(component, prev);
            }
        }

        return this;
    }

    public final LinkedList<T> getComponents() {
        return this.components;
    }

    public final T getLast() {
        return this.components.get(this.components.size() - 1);
    }

    public final int size() {
        return this.components.size();
    }

    public final T get(final JsId jsid) {
        return this.componentsIndexes.get(jsid.toString());
    }

    public final T get(final int index) {
        return this.components.get(index);
    }

    @Override
    public void resetGeneration() {
        super.resetGeneration();
        for (final T component : this.components) {
            component.resetGeneration();
        }
    }

    public Container<T> move(final JsId jsid, final int index) {
        final T item = this.get(jsid);
        int oldPos = this.components.indexOf(item);
        if (oldPos >= index) {
            oldPos++;
        }
        this.components.add(index, item);
        this.components.remove(oldPos);

        if (isGenerated()) {
            // TODO Move corresponding wrapper
        }

        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PRESENTATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public final void setRootTag(final String name, final String classes) {
        this.rootTagName = name;
        this.rootTagClass = classes;
    }

    public final void setRootTagName(final String rootTagName) {
        this.rootTagName = rootTagName;
    }

    public final void setRootTagClass(final String rootTagClass) {
        this.rootTagClass = rootTagClass;
    }

    public final void setWrapTag(final String name, final String classes) {
        this.wrapTagName = name;
        this.wrapTagClass = classes;
    }

    public final void setWrapTagName(final String wrapTagName) {
        this.wrapTagName = wrapTagName;
    }

    public final void setWrapTagClass(final String wrapTagClass) {
        this.wrapTagClass = wrapTagClass;
    }

    public final String getRootTagName() {
        return this.rootTagName;
    }

    public final String getRootTagClass() {
        return this.rootTagClass;
    }

    public final String getWrapTagName() {
        return this.wrapTagName;
    }

    public final String getWrapTagClass() {
        return this.wrapTagClass;
    }

    protected void initTagNames() {
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DISPLAY
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * /**
     * Add the wrap around the item if necessary
     * 
     * @return This method returns the element itself if no wrap is needed, or the Wrapper element.
     */
    protected List<Element> prepareItem(final List<Element> elements) {
        if (this.wrapTagName != null) {
            final LinkedList<Element> res = new LinkedList<Element>();
            final Element wrap = XML.makeElement(XML.openTag(this.wrapTagName, new HTMLClass(this.wrapTagClass)));
            for (final Element e : elements) {
                wrap.appendChild(e);
            }
            res.add(wrap);
            return res;
        }
        return elements;
    }

    /*
     * Add an item directly in the HTML before the first existing item
     * @param item
     * The item to prepend
     */
    protected final void prependItemToHtml(final T item) {
        // this.element.removeClassName("empty");
        HTML.prepend(element, this.prepareItem(item.getElements()));
    }

    /**
     * Add an item directly in the HTML after the last existing item
     * 
     * @param item
     *            The item to append
     */
    protected final void appendItemToHtml(final T item) {
        // this.element.removeClassName("empty");
        HTML.append(element, this.prepareItem(item.getElements()));
    }

    /**
     * Add an item directly in the HTML after the last existing item
     * 
     * @param item
     *            The item to append
     */
    protected final void insertItemAfter(final T item, final Element e) {
        // TODO Insert element
        // for (final Element e2 : this.prepareItem(item.getElements())) {
        // $(e).after(e2);
        // }
    }

    @Override
    protected Element makeElement() {
        element = DOM.createElement(this.rootTagName);
        if (this.rootTagClass != null) {
            element.addClassName(this.rootTagClass);
        }
        if (getJsId() != null) {
            element.addClassName(getJsId().toString());
        }

        if (this.components.size() == 0) {
            element.addClassName("empty");
        } else {
            for (final T next : this.components) {
                this.appendItemToHtml(next);
            }
        }

        return element;
    }

    @Override
    public String toString() {
        return this.getClass().toString().replaceAll(".*\\.", "") + this.components.toString();
    }

}
