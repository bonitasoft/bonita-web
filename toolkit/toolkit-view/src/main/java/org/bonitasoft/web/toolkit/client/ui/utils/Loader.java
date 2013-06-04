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

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

/**
 * @author Séverin Moussel
 * 
 */
public final class Loader {

    public static final String MAIN_LOADER_ID = "loader";

    public static final String MAIN_LOADER_MESSAGE_ID = "loader_message";

    public static enum POSITION {
        MAIN_LOADER, // Display in the #loader AbstractComponent
        // POPUP,
        MAIN_VIEW,
        FULL_OVERLAY // Display in an overlay over the whole page.
    };

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LIFE CYCLE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final Map<Element, Integer> activeLoaders = new HashMap<Element, Integer>();

    private static Loader loader = new Loader();

    public void show(final POSITION position, final Text message) {
        this.show(getTarget(position), message);
    }

    public void show(final Element target, final Text message) {
        incrementActiveLoaders(target);

        if (message == null) {
            this.jQueryShow(target);
        } else {
            this.jQueryShow(target, message.getElement());
        }
    }

    public void hide(final POSITION position) {
        this.hide(position, false);
    }

    public void hide(final Element target) {
        this.hide(target, false);
    }

    public void hide(final POSITION position, final boolean force) {
        this.hide(getTarget(position), force);
    }

    public void hide(final Element target, final boolean force) {
        if (force || decrementActiveLoaders(target) == 0) {
            jQueryHide(target);
        }
    }

    private void incrementActiveLoaders(final Element target) {
        boolean found = false;
        for (final Element key : this.activeLoaders.keySet()) {
            if (key == target) {
                found = true;
                break;
            }
        }

        if (!found) {
            this.activeLoaders.put(target, 1);
        } else {
            this.activeLoaders.put(target, this.activeLoaders.get(target) + 1);
        }
    }

    private int decrementActiveLoaders(final Element target) {
        boolean found = false;
        for (final Element key : this.activeLoaders.keySet()) {
            if (key == target) {
                found = true;
                break;
            }
        }

        if (!found) {
            return 0;
        }

        final int newValue = this.activeLoaders.get(target) - 1;
        if (newValue <= 0) {
            resetActiveLoaders(target);
            return 0;
        }

        this.activeLoaders.put(target, newValue);

        return newValue;
    }

    private void resetActiveLoaders(final Element target) {
        this.activeLoaders.remove(target);
    }

    private Element getTarget(final POSITION position) {
        switch (position) {
            default:
            case MAIN_LOADER:
                return DOM.getElementById(MAIN_LOADER_ID);
            case FULL_OVERLAY:
                return getBody();
            case MAIN_VIEW:
                return DOM.getElementById(ViewController.ROOT_DIV_ID);
        }
    }

    private native Element getBody() /*-{ return $doc.body; }-*/;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // STATICS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void hideLoader(final POSITION position) {
        hideLoader(position, false);
    }

    public static void hideLoader(final AbstractComponent target) {
        hideLoader(target, false);
    }

    public static void hideLoader(final Element target) {
        hideLoader(target, false);
    }

    public static void hideLoader(final POSITION position, final boolean force) {
        loader.hide(position, force);
    }

    public static void hideLoader(final AbstractComponent target, final boolean force) {
        for (final Element e : target.getElements()) {
            loader.hide(e, force);
        }
    }

    public static void hideLoader(final Element target, final boolean force) {
        loader.hide(target, force);
    }

    public static void showLoader(final POSITION position) {
        loader.show(position, null);
    }

    public static void showLoader(final AbstractComponent target) {
        loader.show(target.getElements().get(0), null);
    }

    public static void showLoader(final Element target) {
        loader.show(target, null);
    }

    public static void showLoader(final POSITION position, final String message) {
        loader.show(position, new Text(message));
    }

    public static void showLoader(final AbstractComponent target, final String message) {
        loader.show(target.getElements().get(0), new Text(message));
    }

    public static void showLoader(final Element target, final String message) {
        loader.show(target, new Text(message));
    }

    public static void showLoader(final POSITION position, final Text message) {
        loader.show(position, message);
    }

    public static void showLoader(final AbstractComponent target, final Text message) {
        loader.show(target.getElements().get(0), message);
    }

    public static void showLoader(final Element target, final Text message) {
        loader.show(target, message);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // JQUERY+ MAPPING
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    native private void jQueryShow(Element e)
    /*-{
        $wnd.$(e).loader('start');
    }-*/;

    native private void jQueryShow(Element e, Element message)
    /*-{
        $wnd.$(e).loader('start', {message:message,messageContainer:"#" + @org.bonitasoft.web.toolkit.client.ui.utils.Loader::MAIN_LOADER_MESSAGE_ID});
    }-*/;

    native private void jQueryHide(Element e)
    /*-{
        $wnd.$(e).loader('stop');
    }-*/;
}
