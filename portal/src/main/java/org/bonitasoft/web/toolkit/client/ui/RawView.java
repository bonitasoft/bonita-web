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
package org.bonitasoft.web.toolkit.client.ui;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;
import org.bonitasoft.web.toolkit.client.ui.component.DoubleSection;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.CustomPanel;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;

import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class RawView extends Callable {

    protected JsId jsId = null;

    protected String token = null;

    private CustomPanel rootPanel;

    protected final Container<AbstractComponent> body = new Container<AbstractComponent>();

    protected boolean generated = false;

    protected Element parentElement;

    public RawView() {
        super();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This function must be override to define the JsId of a view.
     */
    public String defineJsId() {
        return defineToken();
    };

    protected final void setJsId(final JsId jsId) {
        this.jsId = jsId;
    }

    protected final void setJsId(final String jsId) {
        this.jsId = new JsId(jsId);
    }

    protected final JsId getJsId() {
        return this.jsId;
    }

    /**
     * This function must be override to define the token to use to access to the current view.
     */
    public abstract String defineToken();

    protected final void setToken(final String token) {
        this.token = token;
    }

    public final String getToken() {
        return this.token;
    }

    /**
     * @return the parentElement
     */
    public Element getParentElement() {
        return this.parentElement;
    }

    /**
     * @param parentElement
     *            the parentElement to set
     */
    public void setParentElement(final Element parentElement) {
        this.parentElement = parentElement;
    }

    /**
     * Add a list of Components in the current view.
     * 
     * @param components
     *            The list of Components to add in the current view.
     */
    protected void addBody(final AbstractComponent... components) {
        List<AbstractComponent> finalComponents = new ArrayList<AbstractComponent>();
        for (AbstractComponent comp : components) {
            finalComponents.add(comp);
            if (isDoubleSection(comp)) {
                Section hiddenSection = createHiddenSection();
                finalComponents.add(hiddenSection);
            }
        }

        AbstractComponent[] arrayComp = new AbstractComponent[finalComponents.size()];
        arrayComp = finalComponents.toArray(arrayComp);

        this.body.append(arrayComp);

        if (this.generated) {
            ViewController.getInstance().triggerLoad();
        }

    }

    private Section createHiddenSection() {
        Section emptySection = new Section(new JsId("doubleSectionShadow"));
        emptySection.addClass("displayNone");
        return emptySection;
    }

    private boolean isDoubleSection(AbstractComponent comp) {
        return comp instanceof DoubleSection;
    }

    /**
     * Add a list of Components in the current view.
     * 
     * @param components
     *            The list of Components to add in the current view.
     */
    protected void setBody(final AbstractComponent... components) {
        this.body.empty().append(components);

        if (this.generated) {
            ViewController.getInstance().triggerLoad();
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CLASS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final Set<String> classes = new TreeSet<String>();

    public void addClass(final String className) {
        if (className != null) {
            final String cName = className.trim();
            if (cName.length() > 0) {
                final String[] classes = cName.split(" ");
                for (int i = 0; i < classes.length; i++) {
                    this.classes.add(classes[i]);

                    if (this.generated) {
                        this.rootPanel.getElement().addClassName(classes[i]);
                    }
                }
            }
        }
    }

    public final void removeClass(final String className) {
        final String[] classes = className.split(" ");
        for (int i = 0; i < classes.length; i++) {
            this.classes.remove(classes[i]);

            if (this.generated) {
                this.rootPanel.getElement().removeClassName(classes[i]);
            }

        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // EVENTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void onLoad() {
        // Optional override
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DOM GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected Element getRootElement() {
        return this.rootPanel.getElement();
    }

    /**
     * Allows to get the current view as a GWT Widget.
     */
    public final CustomPanel toWidget() {
        if (!this.generated) {
            // Create a root widget
            this.rootPanel = new CustomPanel(this);

            fillWidget();
        }

        // Return the widget
        return this.rootPanel;
    }

    protected void refreshAll() {
        $(getRootElement()).empty();
        fillWidget();

    }

    protected void fillWidget() {
        // Build the widget HTML
        _fillWidget(getRootElement());

        // Apply class
        for (final String className : this.classes) {
            this.rootPanel.getElement().addClassName(className);
        }

        this.generated = true;
    }

    private boolean allowAutomatedUpdate = true;

    public void setAllowAutomatedUpdate(final boolean allow) {
        this.allowAutomatedUpdate = allow;
    }

    public boolean getAllowAutomatedUpdate() {
        return this.allowAutomatedUpdate;
    }

    public void refresh() {
        // OVERRIDE IF NEED TO DO SOMETHING
    }

    // Insert the view content in the widget
    protected void _fillWidget(final Element rootElement) {
        // Build the view
        buildView();

        for (final AbstractComponent component : this.body.getComponents()) {
            HTML.append(rootElement, component.getElements());
        }
    }

    /**
     * This function must be override to define the content of the view.<br>
     * <br>
     */
    public abstract void buildView();

    public Element getElement() {
        return toWidget().getElement();
    }

    public void updateUI() {
        if (this.generated) {
            ViewController.updateUI(getElement());
        }
    }

    public Action getClosePopupAction() {
        return new HistoryBackAction();
    }

}
