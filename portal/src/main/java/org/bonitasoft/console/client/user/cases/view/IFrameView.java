/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.console.client.user.cases.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.bonitasoft.console.client.user.task.action.PostMessageEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vincent Elcrin, Anthony Birembaut
 */
public class IFrameView extends Composite {

    interface Binder extends UiBinder<HTMLPanel, IFrameView> {
    }

    private static Binder binder = GWT.create(Binder.class);

    private PostMessageEventListener[] messageEventListeners = null;

    private static Map<String, JavaScriptObject> postMessageListenerFunctions = new HashMap<String, JavaScriptObject>();

    @UiField
    IFrameElement frame;

    @UiField
    FlowPanel toolbar;

    public IFrameView() {
        initWidget(binder.createAndBindUi(this));
        frame.setId("bonitaframe");
        ScriptInjector.fromString(getScriptToFixIEFocus()).setWindow(ScriptInjector.TOP_WINDOW).inject();
    }

    public IFrameView(final String url) {
        this();
        setUrl(url);
    }

    public IFrameView(final PostMessageEventListener... messageEventListeners) {
        this();
        this.messageEventListeners = messageEventListeners;
    }

    public IFrameView(final String url, final PostMessageEventListener... messageEventListeners) {
        this(url);
        this.messageEventListeners = messageEventListeners;
    }

    public void setUrl(final String url) {
        frame.setSrc(url);
    }

    public void setLocation(final String urlLocation) {
        setLocation(frame, urlLocation);
    }

    private native void setLocation(IFrameElement iframe, String urlLocation) /*-{
        if(iframe.contentWindow) {
            iframe.contentWindow.location.replace(urlLocation);
        } else {
            iframe.src = urlLocation;
        }
    }-*/;

    /**
     * Need to inject this script into UIbinder template.
     * It allows to set focus on the "bonitaframe" if the current browser is IE.
     * That fix the ticket BS-16350 "Bug IE, IFrame removal causes loss of the ability to focus input elements".
     */
    protected String getScriptToFixIEFocus(){

        return  "   function isIE() {\n" +
                "        var ua =  window.navigator.userAgent;\n" +
                "        // MSIE: ie <= 10,  Trident: ie 11\n" +
                "        return ua.indexOf('Trident/') > 0 || ua.indexOf('MSIE ') > 0;  \n" +
                "   };\n" +
                "   \n" +
                "   function setFocusOnIframe(){\n" +
                "        if(isIE()) {\n" +
                "           var bonitaIframe = window.document.getElementById('bonitaframe');\n" +
                "           if (bonitaIframe) {\n" +
                "               bonitaIframe.contentWindow.focus();   \n" +
                "           }\n" +
                "        }   \n" +
                "   };";
    }


    public void addTool(final Widget widget) {
        toolbar.removeStyleName("empty");
        toolbar.add(widget);
    }

    @Override
    protected void onAttach() {
        if (messageEventListeners != null) {
            for (final PostMessageEventListener messageEventListener : messageEventListeners) {
                if (!postMessageListenerFunctions.containsKey(messageEventListener.getActionToWatch())) {
                    postMessageListenerFunctions.put(messageEventListener.getActionToWatch(), addFrameNotificationListener(messageEventListener));
                }

            }
        }
        super.onAttach();
    }

    @Override
    protected void onDetach() {
        if (messageEventListeners != null) {
            for (final PostMessageEventListener messageEventListener : messageEventListeners) {
                final JavaScriptObject postMessageListenerFunction = postMessageListenerFunctions.remove(messageEventListener.getActionToWatch());
                removeFrameNotificationListener(postMessageListenerFunction);
            }
        }
        super.onDetach();
    }

    /**
     * Indicates to the parent frame that a form was submitted (and the response was successful)
     *
     * @param eventListener
     */
    native public JavaScriptObject addFrameNotificationListener(PostMessageEventListener postMessageEventListener)
    /*-{
        var postMessageListener = function(e) {
            var eventData = e.data || null;
            postMessageEventListener.@org.bonitasoft.console.client.user.task.action.PostMessageEventListener::onMessageEvent(Ljava/lang/String;)(eventData);
        };
        // Listen to message from child window
        if ($wnd.addEventListener) {
            $wnd.addEventListener("message", postMessageListener, false);
        } else if ($wnd.attachEvent) {
            //For IE
            $wnd.attachEvent("onmessage", postMessageListener, false);
        }
        return postMessageListener;
    }-*/;

    native public void removeFrameNotificationListener(JavaScriptObject postMessageListenerFunction)
    /*-{
        if ($wnd.removeEventListener) {
            $wnd.removeEventListener("message", postMessageListenerFunction);
        } else if ($wnd.detachEvent) {
            //For IE
            $wnd.detachEvent("onmessage", postMessageListenerFunction);
        }
    }-*/;

}
