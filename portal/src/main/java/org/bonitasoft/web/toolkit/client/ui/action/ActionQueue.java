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
package org.bonitasoft.web.toolkit.client.ui.action;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Séverin Moussel
 * 
 */
public class ActionQueue extends Action {

    private final List<String> errors = new LinkedList<String>();

    private boolean breakOnError = false;

    private final Queue<Action> actions = new LinkedList<Action>();

    public ActionQueue() {
        this(false, (Action) null);
    }

    public ActionQueue(final boolean breakOnError) {
        this(breakOnError, (Action) null);
    }

    public ActionQueue(final Action... actions) {
        this(false, actions);
    }

    public ActionQueue(final boolean breakOnError, final Action... actions) {
        super();
        this.breakOnError = breakOnError;
        if (actions != null) {
            for (final Action action : actions) {
                addAction(action);
            }
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONTENT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ActionQueue addAction(final Action action) {
        this.actions.add(action);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void execute() {
        Action action = null;

        // Loop on actions
        while ((action = this.actions.poll()) != null) {

            // Run the next action
            action.setParameters(getParameters());
            action.execute();

            // Error managment
            if (action.hasError()) {
                this.errors.addAll(action.getErrors());

                // Exit on error
                if (this.breakOnError) {
                    break;
                }
            }
        }
    }

}
