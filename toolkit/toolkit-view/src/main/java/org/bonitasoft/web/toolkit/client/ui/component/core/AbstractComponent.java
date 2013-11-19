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
package org.bonitasoft.web.toolkit.client.ui.component.core;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Refreshable;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;
import org.bonitasoft.web.toolkit.client.ui.utils.Loader;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class AbstractComponent extends Widget implements Node {

    /**
     * Indicate if the HTML of the VisualElement has already been generated.
     */
    protected boolean generated = false;

    /**
     * List of fillers to apply to this component.<br />
     * A filler is a definition of how to fill the component with data.
     */
    protected final List<Filler<? extends Object>> fillers = new ArrayList<Filler<? extends Object>>();

    /**
     * An action to perform on load.<br />
     * Load is triggered while the component is inserted in the DOM.
     */
    private Action onLoadAction = null;

    /**
     * A tooltip to set for the whole component.<br />
     * Tooltips are created by inserting a "title" attribute on root DOM elements.
     */
    protected String tooltip;

    /**
     * Indicate if the onLoad event must run the fillers.
     */
    private boolean fillOnLoad = true;

    /**
     * Indicate if the onLoad event must run the fillers.
     */
    private boolean fillOnRefresh = false;

    /**
     * Default constructor.
     */
    public AbstractComponent() {
        super();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERATE HTML
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Called before generating the DOM.<br />
     * This method is meant to be overridden but it's not mandatory.<br />
     * In this method you can only modify member variables.
     */
    protected void preProcessHtml() {
    }

    /**
     * Called after generating the DOM.<br />
     * This method is meant to be overridden but it's not mandatory.<br />
     * In this method you can modify the DOM generated.
     */
    protected void postProcessHtml() {
    }

    /**
     * Check if the component has already been generated.
     * 
     * @return This method returns TRUE if the component has already been generated, otherwise FALSE
     */

    public final boolean isGenerated() {
        return generated;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Indicate if the component must run fillers on load.
     * 
     * @param fillOnLoad
     *            TRUE to fill the component on load, otherwise FALSE.
     * @return This method returns the component itself to allow cascading calls.
     */
    public AbstractComponent setFillOnLoad(final boolean fillOnLoad) {
        this.fillOnLoad = fillOnLoad;

        if (!this.fillOnLoad) {
            ViewController.getInstance().unregisterOnLoadEvent(this);
        } else if (fillers.size() > 0) {
            ViewController.getInstance().registerOnLoadEvent(this);
        }

        return this;
    }

    /**
     * Indicate if the component must run fillers on page refresh.
     * 
     * @param fillOnRefresh
     *            TRUE to fill the component on page refresh, otherwise FALSE.
     * @return This method returns the component itself to allow cascading calls.
     */
    public AbstractComponent setFillOnRefresh(final boolean fillOnRefresh) {

        if (!(this instanceof Refreshable)) {
            throw new ComponentException(this, "Can't register a non Refreshable component for automatic page refresh");
        }

        this.fillOnRefresh = fillOnRefresh;

        if (!this.fillOnRefresh) {
            ViewController.getInstance().unregisterOnPageRefreshEvent((Refreshable) this);
        } else if (fillers.size() > 0) {
            ViewController.getInstance().registerOnPageRefreshEvent((Refreshable) this);
        }

        return this;
    }

    /**
     * Register an action to run on onLoad event.<br />
     * The onLoad event is triggered while the component is inserted in the document.
     * 
     * @param action
     *            The action to run on load
     */
    public final void onLoad(final Action action) {
        onLoadAction = action;
        ViewController.getInstance().registerOnLoadEvent(this);
    }

    /**
     * Override of onLoad method to run fillers if needed.
     */
    @Override
    protected void onLoad() {
        if (fillOnLoad) {
            runFillers();
        }
    }

    /**
     * Trigger the onLoad event.
     */
    public final void triggerLoad() {
        this.onLoad();
        if (onLoadAction != null) {
            onLoadAction.execute();
        }
    }

    /**
     * Display a loader over the component.
     * 
     * @return This method returns the component itself to allow cascading calls.
     */
    public final AbstractComponent startLoading() {
        Loader.showLoader(this);
        return this;
    }

    /**
     * Hide the loader over the component.
     * 
     * @return This method returns the component itself to allow cascading calls.
     */
    public final AbstractComponent stopLoading() {
        Loader.hideLoader(this);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILLERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a filler on this component.<br />
     * Fillers are definitions on how to read and write data.
     * 
     * @param filler
     *            The filler to add
     * @return This method returns the component itself to allow cascading calls.
     */
    public final AbstractComponent addFiller(final Filler<?> filler) {
        if (filler != null) {
            fillers.add(filler);
        }

        if (fillOnLoad) {
            ViewController.getInstance().registerOnLoadEvent(this);
        }
        if (fillOnRefresh) {
            ViewController.getInstance().registerOnPageRefreshEvent((Refreshable) this);
        }

        return this;
    }

    /**
     * Remove all fillers.
     * 
     * @return This method returns "this" to allow cascading calls.
     */
    public final AbstractComponent resetFillers() {
        fillers.clear();
        return this;
    }

    /**
     * Remove all fillers and add the defined ones.
     * 
     * @param fillers
     *            The fillers to set.
     * @return This method returns "this" to allow cascading calls.
     */
    public final AbstractComponent setFillers(final List<Filler<? extends Object>> fillers) {
        this.fillers.clear();
        this.fillers.addAll(fillers);
        ViewController.getInstance().registerOnLoadEvent(this);

        if (fillOnLoad) {
            ViewController.getInstance().registerOnLoadEvent(this);
        }
        if (fillOnRefresh) {
            ViewController.getInstance().registerOnPageRefreshEvent((Refreshable) this);
        }

        return this;
    }

    /**
     * Remove all fillers and add the defined one.
     * 
     * @param filler
     *            The filler to set
     * @return This method returns "this" to allow cascading calls.
     */
    public final AbstractComponent setFiller(final Filler<?> filler) {
        resetFillers();
        addFiller(filler);
        return this;
    }

    /**
     * Execute the fillers of this component.
     */
    protected final void runFillers() {
        runFillers(null);
    }

    public final List<Filler<? extends Object>> getFillers() {
        return fillers;
    }

    /**
     * Execute the fillers of this component, and call the action at the end.
     */
    protected final void runFillers(final Action callback) {
        if (!generated) {
            return;
        }
        for (final Filler<? extends Object> filler : fillers) {
            filler.setTarget(this);
            filler.setOnFinishCallback(callback);
            filler.run();
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GET ELEMENTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Construct the DOM tree using a set of com.google.gwt.user.client.Element.
     * 
     * @return This function returns the root Elements of this VisualElement.
     */
    @Override
    public final List<Element> getElements() {
        final List<Element> elements = _getElements();

        if (tooltip != null) {
            for (final Element e : elements) {
                e.setAttribute("title", tooltip);
            }
        }

        return elements;
    }

    /**
     * Construct the DOM tree using a set of com.google.gwt.user.client.Element.
     * 
     * @return This function returns the root Elements of this VisualElement.
     */
    protected abstract List<Element> _getElements();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DOM TOOLS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Check if the component is already inserted in the main Document DOM tree.
     * 
     * @return This method return TRUE if the component is in the Document DOM, otherwise FALSE.
     */
    public final boolean isInDom() {
        for (final Element e : getElements()) {
            if (!this.isInDom(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the passed element is already inserted in the main Document DOM tree.
     * 
     * @param e
     *            The element to check
     * @return This method return TRUE if the element is in the Document DOM, otherwise FALSE.
     */
    protected boolean isInDom(final Element e) {
        if (e == null) {
            return false;
        }
        if ("html".equalsIgnoreCase(e.getTagName())) {
            return true;
        }

        return $(e).closest("html").length() > 0;
        // return this.isInDom((Element) e.getParentElement());
    }

    /**
     * Append a component in a root element.
     * 
     * @param rootElement
     *            The root element where to append the component.
     * @param component
     *            The component to append.
     */

    protected static final void appendComponentToHtml(final Element rootElement, final AbstractComponent component) {
        if (component != null) {
            HTML.append(rootElement, component.getElements());
        }
    }

    /**
     * Prepend a component in a root element.
     * 
     * @param rootElement
     *            The root element where to prepend the component.
     * @param component
     *            The component to prepend.
     */
    protected static final void prependComponentToHtml(final Element rootElement, final AbstractComponent component) {
        if (component != null) {
            HTML.prepend(rootElement, component.getElements());
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OPTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Set the tooltip to display on this component.
     * 
     * @return This method returns "this" to allow cascading calls.
     */
    public final String getTooltip() {
        return tooltip;
    }

    /**
     * Define the tooltip to display on mouse over this component.
     * 
     * @param tooltip
     *            The tooltip to display.
     * @return This method returns "this" to allow cascading calls.
     */
    public final AbstractComponent setTooltip(final String tooltip) {
        this.tooltip = tooltip;

        if (isGenerated()) {
            if (tooltip == null) {
                for (final Element e : getElements()) {
                    e.removeAttribute("title");
                }
            } else {
                for (final Element e : getElements()) {
                    e.setAttribute("title", tooltip);
                }
            }
        }

        return this;
    }

}
