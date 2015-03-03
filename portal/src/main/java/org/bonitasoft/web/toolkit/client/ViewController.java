/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.toolkit.client;

import static com.google.gwt.query.client.GQuery.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.angular.AngularIFrameView;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.eventbus.MainEventBus;
import org.bonitasoft.web.toolkit.client.eventbus.events.ChangeViewEvent;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Refreshable;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.CustomPanel;
import org.bonitasoft.web.toolkit.client.ui.component.form.view.DeleteItemPage;
import org.bonitasoft.web.toolkit.client.ui.component.form.view.EditItemPage;
import org.bonitasoft.web.toolkit.client.ui.page.ChangeLangPage;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * This Class defines the main controller of the entire GWT application. It is responsible for the interaction between the
 * different components spread all over the window.
 *
 * @author Julien Mege
 */
public class ViewController {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIG
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String ROOT_DIV_ID = "body";

    public static final String POPUP_DIV_ID = "popup";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FIELDS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected static final ViewController INSTANCE = new ViewController();

    private String currentPageToken = null;

    private static boolean isAngularFrameDisplayed = false;

    private final List<AbstractComponent> componentsWaitingForLoad = new LinkedList<AbstractComponent>();

    private final List<Refreshable> componentsWaitingForRefresh = new LinkedList<Refreshable>();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // INIT AND CONSTRUCTOR
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get the ViewController instance.
     *
     * @return the unique instance of the ViewController.
     */
    public static ViewController getInstance() {
        return INSTANCE;
    }

    protected ViewController() {
        GWT.setUncaughtExceptionHandler(new CatchAllExceptionHandler());
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SHOW VIEWS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private RawView currentPage = null;

    public String getCurrentPageToken() {
        return currentPageToken;
    }

    public void setCurrentPageToken(final String currentPageToken) {
        this.currentPageToken = currentPageToken;
    }

    public static RawView showView(final String token) {
        return showView(token, ROOT_DIV_ID);
    }

    public static RawView showView(final String token, final String parentId) {
        return showView(token, parentId, new TreeIndexed<String>());
    }

    public static RawView showView(final String token, final Map<String, String> params) {
        return showView(token, null, params);
    }

    public static RawView showView(final String token, final TreeIndexed<String> params) {
        return showView(token, (String) null, params);
    }

    public static RawView showView(final RawView view) {
        return showView(view, ROOT_DIV_ID, view.getParameters());
    }

    public static RawView showView(final RawView view, final String parentId) {
        return showView(view, parentId, view.getParameters());
    }

    public static RawView showView(final RawView view, final Map<String, String> params) {
        return showView(view, ROOT_DIV_ID, new TreeIndexed<String>(params));
    }

    public static RawView showView(final String token, final String parentId, final Map<String, String> params) {
        return showView(token, parentId, new TreeIndexed<String>(params));
    }

    public static RawView showView(final RawView view, final String parentId, final Map<String, String> params) {
        return showView(view, parentId, new TreeIndexed<String>(params));
    }

    public static RawView showView(final String token, final String parentId, final TreeIndexed<String> params) {
        Element rootElement;

        if (parentId == null) {
            rootElement = DOM.getElementById("body");
        } else {
            rootElement = DOM.getElementById(parentId);
        }

        if (rootElement != null) {
            return showView(token, rootElement, params);
        }
        return null;
    }

    public static RawView showView(final RawView view, final String parentId, final TreeIndexed<String> params) {
        assert parentId != null;

        final Element rootElement = DOM.getElementById(parentId);
        if (rootElement != null) {
            return showView(view, rootElement, params);
        }

        return view;
    }

    public static RawView showView(final String token, final Element rootElement, final HashMap<String, String> params) {
        return showView(token, rootElement, new TreeIndexed<String>(params));
    }

    public static RawView showView(final RawView view, final Element rootElement, final HashMap<String, String> params) {
        return showView(view, rootElement, new TreeIndexed<String>(params));
    }

    public static RawView showView(final String token, final Element rootElement, final TreeIndexed<String> params) {
        return showView(ViewController.getInstance().createView(token, params), rootElement, params);
    }


    public static RawView showView(final RawView view, final Element rootElement, final TreeIndexed<String> params) {
        // Set the parent Element to the view that will be displayed
        view.setParentElement(rootElement);

        if (ViewController.ROOT_DIV_ID.equals(rootElement.getId())) {

            // Reset useless elements
            ViewController.closePopup();
            // getInstance().componentsWaitingForRefresh.clear();
            getInstance().currentPage = view;
            getInstance().setCurrentPageToken(view.getToken());

            // Set the URL
            // if (!BlankPage.TOKEN.equals(view.getToken())) {
            ClientApplicationURL.setPageToken(view.getToken(), false);
            // }
            ClientApplicationURL.setPageAttributes(params);
            ClientApplicationURL.refreshUrl(false);
        }

        final CustomPanel widget = view.toWidget();
        final Element widgetElement = widget.getElement();

        if (view instanceof AngularIFrameView) {
            if (!isAngularFrameDisplayed) {
                $(rootElement).empty();
                rootElement.appendChild(widgetElement);
            }
            ((AngularIFrameView) view).display(params);
            isAngularFrameDisplayed = true;
        } else {
            if (view.getToken() != null && !view.getToken().trim().equals("")) {
                isAngularFrameDisplayed = false;
            }
            if (view instanceof PageOnItem<?>) {
                $(widgetElement).hide();
            } else {
                $(rootElement).empty();
            }
            rootElement.appendChild(widgetElement);
        }

        ViewController.updateUI(rootElement, true);
        widget.onLoad();

        MainEventBus.getInstance().fireEventFromSource(new ChangeViewEvent(view), getInstance());

        return view;
    }

    public static void showPopup(final String token) {
        showPopup(token, new TreeIndexed<String>());
    }

    public static void showPopup(final String token, final Map<String, String> params) {
        ViewController.showPopup(token, new TreeIndexed<String>(params));
    }

    public static void showPopup(final String token, final TreeIndexed<String> params) {
        showPopup(ViewController.getInstance().createView(token, params));
    }

    public static void showPopup(final RawView view) {
        DOM.getElementById(ViewController.POPUP_DIV_ID).setInnerHTML("");
        ViewController.openPopup();
        addClosePopupAction(view);
        ViewController.showView(view, ViewController.POPUP_DIV_ID);
    }

    private static void addClosePopupAction(final RawView view) {
        final Element popupHeader = DOM.getElementById("popupcontainerheader");
        popupHeader.setInnerHTML(""); // To remove button repetition after popup re-opening
        popupHeader.appendChild(new Link(new JsId("close_popup"), "Close popup", "Close this popup", view.getClosePopupAction()).getElement());
    }

    private RawView createView(final String token, final TreeIndexed<String> params) {
        RawView page;

        if (token == ClientApplicationURL.TOKEN_ADD) {
            page = new EditItemPage();
        } else if (token == ClientApplicationURL.TOKEN_EDIT) {
            page = new EditItemPage();
        } else if (token == ClientApplicationURL.TOKEN_DELETE) {
            page = new DeleteItemPage();
        } else if (token == ChangeLangPage.TOKEN) {
            page = new ChangeLangPage();
        } else {
            page = ApplicationFactoryClient.getDefaultFactory().defineViewTokens(token);
        }

        page.setParameters(params);
        return page;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // POPUP
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static native void openPopup() /*-{
                                             $wnd.$.popup.open();
                                             }-*/;

    public static native void closePopup() /*-{
                                              $wnd.$.popup.close();
                                              }-*/;

    public static native boolean hasOpenedPopup() /*-{
                                                     return $wnd.$.popup.isOpen();
                                                     }-*/;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // JQUERY+ MAPPER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static native void updateUI(Element element, boolean force)
    /*-{
        $wnd.$(element).updateUI(force);
    }-*/;

    public static native void updateUI(Element element)/*-{
                                                       $wnd.$(element).updateUI();
                                                       }-*/;

    public static native void updateUI(String selector, boolean force)/*-{
                                                                      $wnd.$(selector).updateUI(force);
                                                                      }-*/;

    public static native void updateUI(String selector)/*-{
                                                       $wnd.$(selector).updateUI();
                                                       }-*/;

    public static native void back()/*-{
                                    $wnd.historyBack();
                                    }-*/;

    public void historyBack() {
        if (hasOpenedPopup()) {
            closePopup();
            triggerRefresh();
        } else {
            back();
        }

    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PAGE REFRESH
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void refreshCurrentPage() {
        ViewController.getInstance().triggerRefresh();
    }

    public void registerOnLoadEvent(final AbstractComponent... components) {
        for (final AbstractComponent component : components) {
            if (!componentsWaitingForLoad.contains(component)) {
                componentsWaitingForLoad.add(component);
            }
        }
    }

    public void unregisterOnLoadEvent(final AbstractComponent... components) {
        for (final AbstractComponent component : components) {
            componentsWaitingForLoad.remove(component);
        }
    }

    public void registerOnPageRefreshEvent(final Refreshable... components) {
        for (final Refreshable component : components) {
            if (!componentsWaitingForRefresh.contains(component)) {
                componentsWaitingForRefresh.add(component);
            }
        }
    }

    public void unregisterOnPageRefreshEvent(final Refreshable... components) {
        for (final Refreshable component : components) {
            componentsWaitingForRefresh.remove(component);
        }
    }

    public void triggerLoad() {
        for (final AbstractComponent component : componentsWaitingForLoad) {
            // if (component instanceof AbstractComponent && !component.isInDom()) {
            // this.componentsWaitingForLoad.remove(component);
            // continue;
            // }

            component.triggerLoad();
        }
        componentsWaitingForLoad.clear();
    }

    public void triggerRefresh() {

        // Clean components that are no longer in the DOM tree
        for (int i = 0; i < componentsWaitingForRefresh.size(); i++) {
            final Refreshable component = componentsWaitingForRefresh.get(i);
            if (component instanceof AbstractComponent && !((AbstractComponent) component).isInDom()) {
                componentsWaitingForRefresh.remove(component);
                i--;
            }
        }

        // Refresh automatically registered components
        if (currentPage.getAllowAutomatedUpdate()) {
            for (final Refreshable component : componentsWaitingForRefresh) {
                component.refresh();
            }
        }

        // Custom page refresh
        currentPage.refresh();

    }

}
