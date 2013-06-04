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
package org.bonitasoft.web.toolkit.client.ui.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;

/**
 * This class manage th filling of a component<br />
 * The filling is called while the page finish to load and can be repeated based on a defined period.
 * 
 * @author Séverin Moussel
 */
public abstract class Filler<TARGET_CLASS extends Object> {

    /**
     * @author Séverin Moussel
     * 
     */

    private Action onFinishCallback = null;

    private final class FillerCallback extends APICallback {

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            super.onSuccess(httpStatusCode, response, headers);
            Filler.this.setData(response, headers);
            if (Filler.this.showLoader) {
                Filler.this.hideLoader();
            }
            if (Filler.this.onFinishCallback != null) {
                Filler.this.onFinishCallback.execute();
            }
        }

        @Override
        public void onError(final String message, final Integer errorCode) {
            // on fail is call in the case of a resource not found with rest api
            if (errorCode.equals(HttpServletResponse.SC_NOT_FOUND) && message.isEmpty()) {
                Filler.this.onFail(errorCode, message, new HashMap<String, String>());
            } else {
                Filler.this.onError(parseException(message, errorCode));
            }

            if (Filler.this.showLoader) {
                Filler.this.hideLoader();
            }
        }
    }

    protected TARGET_CLASS target = null;

    public Filler() {
        this(null, NO_REPEAT);
    }

    public Filler(final TARGET_CLASS target) {
        this(target, NO_REPEAT);
    }

    public Filler(final int repeatEvery) {
        this(null, repeatEvery);
    }

    @SuppressWarnings("unchecked")
    public Filler(final Object target, final int repeatEvery) {
        super();
        this.target = (TARGET_CLASS) target;
        this.repeatEvery = repeatEvery;
    }

    public void setOnFinishCallback(final Action onFinishCallback) {
        this.onFinishCallback = onFinishCallback;
    }

    protected abstract void getData(APICallback callback);

    protected abstract void setData(final String json, final Map<String, String> headers);

    protected void onFail(final int httpStatusCode, final String response, final Map<String, String> headers) {
        // do nothing on purpose
    }

    protected void onError(final RuntimeException e) {
        throw e;
    }

    public final void run() {
        if (this.repeatEvery > 0) {
            Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {

                @Override
                public boolean execute() {
                    final AbstractComponent target = (AbstractComponent) Filler.this.target;

                    if (!target.isInDom()) {
                        return false;
                    }

                    Filler.this._run();
                    return true;
                }
            }, this.repeatEvery);
        }
        this._run();
    }

    protected final void _run() {
        // if (this.showLoader) {
        // this.showLoader();
        // }
        try {
            this.getData(new FillerCallback());
        } catch (final RuntimeException e) {
            // Call onError asynchronously as if it was a server side error.
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                @Override
                public void execute() {
                    onError(e);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    public final void setTarget(final Object target) {
        this.target = (TARGET_CLASS) target;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OPTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final int NO_REPEAT = -1;

    public static final int ONE_SECOND = 1000;

    public static final int ONE_MINUTE = 60000;

    public static final int ONE_HOUR = 3600000;

    /**
     * Repeat time in milliseconds
     */
    private int repeatEvery = -1;

    public final int getRepeatEvery() {
        return this.repeatEvery;
    }

    /**
     * Set a repeatDelay. if the value is > -1, the filler will repeat itself
     * 
     * @param milliseconds
     *            Time in milliseconds to wait before repeating the filler. If this value is < 0, the filler won't repeat
     * @return This methid returns the filler itself to allow cascading calls.
     */
    public final Filler<TARGET_CLASS> setRepeatEvery(final int milliseconds) {
        this.repeatEvery = milliseconds;
        return this;
    }

    private boolean showLoader = true;

    private Loader.POSITION loaderPosition = null;

    public final boolean isShowLoader() {
        return this.showLoader;
    }

    /**
     * Define that a loader must be displayed to the user during the update process.<br />
     * The loader will appear from before the getData method to after the setData method.<br />
     * 
     * @param showLoader
     *            True to show a loader
     */
    public final void setShowLoader(final boolean showLoader) {
        this.showLoader = showLoader;
    }

    public final Loader.POSITION getLoaderPosition() {
        return this.loaderPosition;
    }

    /**
     * Define where the loader will be displayed<br />
     * Changing this value will set showLoader to true;
     * 
     * @param loaderPosition
     *            Where to display the loader.<br />
     *            Can be one of org.bonitasoft.web.toolkit.client.ui.utils.Loader.POSITION:
     *            <dl>
     *            <dd>MAIN_LOADER</dd>
     *            <dt>Display a loader in the #loader element of the UI</dt>
     *            <dd>FULL_OVERLAY</dd>
     *            <dt>Display a loader with an overlay over the whole UI. This way, the UI is disabled during loading</dt>
     *            <dd>null</dd>
     *            <dt>Display a loader with an overlay over the target of this filler if the target is a component otherwise, MAIN_LOADER will be used.</dt>
     *            </dl>
     */
    public final void setLoaderPosition(final Loader.POSITION loaderPosition) {
        this.loaderPosition = loaderPosition;
        this.setShowLoader(true);
    }

    protected final void showLoader() {
        if (this.loaderPosition == null) {
            if (this.target instanceof AbstractComponent) {
                Loader.showLoader((AbstractComponent) this.target);
            } else if (this.target instanceof Element) {
                Loader.showLoader((Element) this.target);
            } else {
                Loader.showLoader(Loader.POSITION.MAIN_LOADER);
            }
        } else {
            Loader.showLoader(this.loaderPosition);
        }
    }

    protected final void hideLoader() {
        if (this.loaderPosition == null) {
            if (this.target instanceof AbstractComponent) {
                Loader.hideLoader((AbstractComponent) this.target);
            } else if (this.target instanceof Element) {
                Loader.hideLoader((Element) this.target);
            } else {
                Loader.hideLoader(Loader.POSITION.MAIN_LOADER);
            }
        } else {
            Loader.hideLoader(this.loaderPosition);
        }
    }

}
