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
package org.bonitasoft.web.toolkit.client.data.api.request;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.ui.action.Action;

/**
 * @author Séverin Moussel
 */
public class RequestQueue {

    private boolean breakOnError = false;

    private final List<QueuableRequest> requests = new LinkedList<QueuableRequest>();

    private final Map<String, String> errors = new LinkedHashMap<String, String>();

    private Action onFinish = null;

    private Action onError = null;

    public RequestQueue() {
    }

    public RequestQueue(final boolean breakOnError) {
        this.breakOnError = breakOnError;
    }

    public RequestQueue(final boolean breakOnError, final Action onFinish) {
        this.breakOnError = breakOnError;
        this.onFinish = onFinish;
    }

    public RequestQueue(final boolean breakOnError, final QueuableRequest... request) {
        this(breakOnError);
        for (final QueuableRequest req : request) {
            addRequest(req);
        }
    }

    public void _next(final boolean success) {
        if (success || !this.breakOnError) {
            if (this.requests.size() > 0) {

                final QueuableRequest request = this.requests.get(0);
                this.requests.remove(0);
                request.run();
            } else {
                if (this.onError != null && this.errors.size() > 0) {
                    this.onError.addParameter("errors", this.errors);
                    this.onError.execute();
                } else if (this.onFinish != null) {
                    this.onFinish.addParameter("errors", this.errors);
                    this.onFinish.execute();
                }
            }
        }
    }

    public RequestQueue addRequest(final QueuableRequest request) {
        request.setStack(this);
        this.requests.add(request);
        return this;
    }

    public RequestQueue onFinish(final Action action) {
        this.onFinish = action;
        return this;
    }

    public RequestQueue onError(final Action action) {
        this.onError = action;
        return this;
    }

    public void run(final Action onFinish, final Action onError) {
        onError(onError);
        run(onFinish);
    }

    public void run(final Action onFinish) {
        onFinish(onFinish);
        this.run();
    }

    public void run() {
        _next(true);
    }

    public void addError(final Integer httpErrorCode, final String message) {
        this.errors.put(httpErrorCode.toString(), message);
    }
}
