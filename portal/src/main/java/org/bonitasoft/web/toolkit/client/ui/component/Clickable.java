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

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Event;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.event.ActionEvent;
import org.bonitasoft.web.toolkit.client.ui.component.event.ActionHandler;
import org.bonitasoft.web.toolkit.client.ui.utils.TypedString;

/**
 * @author Séverin Moussel
 */
public abstract class Clickable extends Component {

    protected Action action = null;

    protected String tooltip;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // With Action
    // ///////////
    public Clickable(final JsId jsid, final String tooltip, final Action action) {
        super(jsid);
        this.tooltip = tooltip;
        this.action = action;
    }

    public Clickable(final String tooltip, final Action action) {
        this(null, tooltip, action);
    }

    public HandlerRegistration addActionHandler(ActionHandler handler) {
        this.action = createUiBinderAction();
        return addHandler(handler, ActionEvent.TYPE);
    }

    private Action createUiBinderAction() {
        return new Action() {

            @Override
            public void execute() {
                Clickable.this.fireEvent(new ActionEvent());
            }
        };
    }

    // With TypedString
    // ////////////////

    /**
     * @deprecated Create redirection action yourself!
     */
    public Clickable(final JsId jsid, final String tooltip, final TypedString token) {
        this(jsid, tooltip, new RedirectionAction(token));
    }

    /**
     * @deprecated Create redirection action yourself!
     */
    public Clickable(final String tooltip, final TypedString token) {
        this(null, tooltip, token);
    }

    // With Token and optional parameters
    // //////////////////////////////////

    /**
     * @deprecated Create redirection action yourself!
     */
    public Clickable(final JsId jsid, final String tooltip, final String token, final TreeIndexed<String> parameters) {
        this(jsid, tooltip, new RedirectionAction(token, parameters));
    }

    /**
     * @deprecated Create redirection action yourself!
     */
    public Clickable(final String tooltip, final String token, final TreeIndexed<String> parameters) {
        this(null, tooltip, token, parameters);
    }

    /**
     * @deprecated Create redirection action yourself!
     */
    public Clickable(final JsId jsid, final String tooltip, final String token) {
        this(jsid, tooltip, token, null);
    }

    /**
     * @deprecated Create redirection action yourself!
     */
    public Clickable(final String tooltip, final String token) {
        this(null, tooltip, token, null);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PARAMETERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final Clickable addParameter(final String name, final String... value) {
        this.action.addParameter(name, value);
        return this;
    }

    public final Clickable addParameter(final String name, final String value) {
        this.action.addParameter(name, value);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERATE HTML
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void postProcessHtml() {
        if (isEnabled()) {
            $(this.element).click(new Function() {
                @Override
                public boolean f(final Event e) {
                    if (!Clickable.this.action.isStarted) {
                        if (Clickable.this.action != null && isEnabled()) {
                            Clickable.this.action.execute();
                        }
                        e.stopPropagation();
                        try {
                            super.f(e);
                        } catch (final Exception gqueryDefaultBehaviourException) {
                            // do nothing if there is no default behaviour
                        }
                    }
                    return false;
                }
            });
            addDomHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (!Clickable.this.action.isStarted) {
                        if (Clickable.this.action != null && isEnabled()) {
                            Clickable.this.action.execute();
                        }
                    }
                    event.stopPropagation();
                    event.preventDefault();
                }
            }, ClickEvent.getType());
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTER AND SETTER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param action the action to set
     */
    public void setAction(final Action action) {
        this.action = action;
    }

    /**
     * @return the action
     */
    public Action getAction() {
        return this.action;
    }

}
