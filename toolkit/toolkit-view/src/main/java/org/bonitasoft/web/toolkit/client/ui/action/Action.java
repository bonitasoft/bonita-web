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
package org.bonitasoft.web.toolkit.client.ui.action;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.Callable;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;

/**
 * @author Séverin Moussel
 */
public abstract class Action extends Callable implements EventListener, Cloneable {

    protected Action onStart = null;

    protected Action onFinish = null;

    protected Action onSuccess = null;

    protected Action onError = null;

    private final List<String> errors = new LinkedList<String>();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Action() {
    }

    public Action(final TreeIndexed<String> parameters) {
        this.setParameters(parameters);
    }

    public Action(final Map<String, String> parameters) {
        this.setParameters(parameters);
    }

    public Action(final Arg... parameters) {
        this.setParameters(parameters);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ERRORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected final void addError(final String error) {
        this.errors.add(error);
    }

    public boolean hasError() {
        return this.errors.size() > 0;
    }

    /**
     * @return the errors
     */
    public List<String> getErrors() {
        return this.errors;
    }

    protected void showErrors() {
        if (!hasError()) {
            return;
        }

        String errorList = HTML.ul();
        for (final String error : this.errors) {
            errorList += HTML.li() + HTML.text(error) + HTML._li();
        }
        errorList += HTML._ul();

        Window.alert(errorList);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GWT EVENT MANAGEMENT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void onBrowserEvent(final Event event) {
        this.errors.clear();

        // Start event
        triggerStart();

        if (!hasError()) {

            // Execute
            execute();

            if (!hasError()) {
                // Success event
                triggerSuccess();
            } else {
                // Error event
                triggerError();
            }
        }

        // Finish event
        triggerFinish();

        // Error display (if needed)
        showErrors();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // EVENTS TRIGGER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected final void triggerStart() {
        // Event as parameter
        if (this.onStart != null) {
            this.onStart.setParameters(getParameters());
            this.onStart.execute();
        }
        // Overridable event
        onStart();
    }

    protected final void triggerFinish() {
        // Event as parameter
        if (this.onFinish != null) {
            this.onFinish.setParameters(getParameters());
            this.onFinish.execute();
        }
        // Overridable event
        onFinish();
    }

    protected final void triggerSuccess() {
        // Event as parameter
        if (this.onSuccess != null) {
            this.onSuccess.setParameters(getParameters());
            this.onSuccess.execute();
        }
        // Overridable event
        onSuccess();
    }

    protected final void triggerError() {
        // Event as parameter
        if (this.onError != null) {
            this.onError.setParameters(getParameters());
            this.onError.execute();
        }
        // Overridable event
        onError();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CUSTOM EVENTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param onStart
     *            the onStart to set
     */
    public void setOnStart(final Action onStart) {
        this.onStart = onStart;
    }

    /**
     * @param onFinish
     *            the onFinish to set
     */
    public void setOnFinish(final Action onFinish) {
        this.onFinish = onFinish;
    }

    /**
     * @param onSuccess
     *            the onSuccess to set
     */
    public void setOnSuccess(final Action onSuccess) {
        this.onSuccess = onSuccess;
    }

    /**
     * @param onError
     *            the onError to set
     */
    public void setOnError(final Action onError) {
        this.onError = onError;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OVERRIDABLE EVENTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Called before the execute command. If this method add any error, the execute command won't be called.
     * Optional override.
     */
    protected void onStart() {
    }

    /**
     * Called after the execute command has succeeded without adding any error.
     * Optional override.
     */
    protected void onFinish() {
    }

    /**
     * Called after the execute command has succeeded without adding any error.
     * Optional override.
     */
    protected void onSuccess() {
    }

    /**
     * Called before the execute command. If this method add any error, the execute command won't be called.
     * Optional override.
     */
    protected void onError() {
    }

    /**
     * Starts the execution of the action.
     */
    public abstract void execute();

}
