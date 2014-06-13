/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.client.view.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bonitasoft.forms.client.model.HeadNode;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Utility Class form DOM manipulation
 *
 * @author Anthony Birembaut
 */
public class DOMUtils {

    /**
     * ID of the element containing the static html part of the entry point
     */
    public static final String STATIC_CONTENT_ELEMENT_ID = "static_application";

    /**
     * Mandatory frame id for the forms application when the form is in a frame
     */
    public static final String DEFAULT_FORM_ELEMENT_ID = "bonita_form";

    /**
     * Id of the element of the page template in witch the title has to be injected
     */
    public static final String PAGE_LABEL_ELEMENT_ID = "bonita_form_page_label";

    /**
     * Id of the element of the form frame
     */
    public static final String FORM_FRAME_ID = "bonitaframe";

    /**
     * Instance attribute
     */
    protected static DOMUtils INSTANCE = null;

    private final String CSS_ID_PRE = "CSS_";

    /**
     * @return the view controller instance
     */
    public static DOMUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DOMUtils();
        }
        return INSTANCE;
    }

    /**
     * Private contructor to prevent instantiation
     */
    protected DOMUtils() {
        super();
    }

    /**
     * Deals with the whole page template injection
     *
     * @param headNodes
     * @param pageHTMLPanel
     * @param bodyAttributes
     * @param list
     * @param elementId
     * @return the onload attribute value if it exists, null otherwise
     */
    public String insertPageTemplate(final List<HeadNode> headNodes, final HTMLPanel pageHTMLPanel, final Map<String, String> bodyAttributes,
            final HTMLPanel processHTMLPanel, final String elementId) {

        addHeaders(headNodes);
        addPageContent(pageHTMLPanel, processHTMLPanel, elementId);
        final String onloadValue = addBodyAttributes(bodyAttributes);
        if (isPageInFrame()) {
            setBodyTransparency();
        }
        return onloadValue;
    }

    /**
     * set the body of the page transparency
     */
    protected void setBodyTransparency() {

        final String existingClassName = RootPanel.getBodyElement().getClassName();
        if (existingClassName != null && existingClassName.length() > 0) {
            RootPanel.getBodyElement().setClassName("bonita_transparent_body " + existingClassName);
        } else {
            RootPanel.getBodyElement().setClassName("bonita_transparent_body");
        }
    }

    protected Element getHeadElement() {
        Element headElement = null;
        final Element documentElement = DOM.getParent(RootPanel.getBodyElement());
        final int documentChildrenCount = DOM.getChildCount(documentElement);
        for (int i = 0; i < documentChildrenCount; i++) {
            final Element documentChildElement = DOM.getChild(documentElement, i);
            if ("head".equalsIgnoreCase(documentChildElement.getNodeName())) {
                headElement = documentChildElement;
                break;
            }
        }
        return headElement;
    }

    /**
     * @param headNodes
     */
    public void addHeaders(final List<HeadNode> headNodes) {
        final Element headElement = getHeadElement();
        if (headElement != null) {
            for (final HeadNode headNode : headNodes) {
                if (isHeadNodeUseful(headNode)) {
                    if (!isContainHeadNode(headElement, headNode)) {
                        // removeOldHeaderElementIfPresent(headElement, headNode);
                        if (headNode.getTagName() == null) {
                            //for comment nodes
                            final Node textNode = Document.get().createTextNode(headNode.getInnerHtml());
                            headElement.appendChild(textNode);
                        } else {
                            //for other nodes
                            final Element headChildElement = DOM.createElement(headNode.getTagName());
                            for (final Entry<String, String> attributeEntry : headNode.getAttributes().entrySet()) {
                                headChildElement.setAttribute(attributeEntry.getKey(), attributeEntry.getValue());
                            }
                            if (headNode.getInnerHtml() != null && headNode.getInnerHtml().length() > 0) {
                                headChildElement.setInnerText(headNode.getInnerHtml());
                            }
                            headElement.appendChild(headChildElement);
                        }
                    }
                    if ("title".equalsIgnoreCase(headNode.getTagName())) {
                        Window.setTitle(headNode.getInnerHtml());
                    }
                }
            }
        }
    }

    private boolean isContainHeadNode(final Element headElement, final HeadNode headNode) {
        boolean isContained = false;
        final Map<String, String> attributeMap = headNode.getAttributes();
        final String headInnerHtml = headNode.getInnerHtml();
        final int childrenCount = DOM.getChildCount(headElement);
        for (int i = 0; i < childrenCount; i++) {
            final Element childElement = DOM.getChild(headElement, i);

            boolean isSameNode = true;
            int theIndex = 0;
            if (headNode.getTagName() != null && headNode.getTagName().equalsIgnoreCase(childElement.getTagName())) {
                final String childElementValue = childElement.getInnerHTML();
                boolean innerHtml = false;
                if (headInnerHtml == null && childElementValue == null) {
                    innerHtml = true;
                } else if (headInnerHtml != null && childElementValue != null) {
                    if (headInnerHtml.equals(childElementValue)) {
                        innerHtml = true;
                    }
                }
                if (innerHtml) {
                    for (final Entry<String, String> attributeEntry : attributeMap.entrySet()) {
                        if (!isSameNode) {
                            break;
                        }
                        final String attributeKey = attributeEntry.getKey();
                        final String attribeteValue = attributeEntry.getValue();
                        if (childElement.hasAttribute(attributeKey)) {
                            final String childElementAttributeValue = childElement.getAttribute(attributeKey);
                            isSameNode = childElementAttributeValue.equals(attribeteValue);
                            if (isSameNode) {
                                theIndex++;
                            }
                        }

                    }
                }
                if (theIndex == attributeMap.size()) {
                    isContained = true;
                    break;
                }
            }
        }
        return isContained;
    }

    /**
     * Deals with the whole application template injection
     *
     * @param headNodes
     * @param applicationHTMLPanel
     * @param bodyAttributes
     * @return the onload attribute value if it exists, null otherwise
     */
    public String insertApplicationTemplate(final List<HeadNode> headNodes, final HTMLPanel applicationHTMLPanel, final Map<String, String> bodyAttributes) {

        addHeaders(headNodes);
        addApplicationContent(applicationHTMLPanel, STATIC_CONTENT_ELEMENT_ID);
        cleanBodyAttributes();
        return addBodyAttributes(bodyAttributes);
    }

    /**
     * Add a stylesheet link is the link does not still exist
     */
    public void addStyleSheetLink(final String cssFileName) {
        final String theme = cssFileName.substring(0, cssFileName.length() - 4); // remove ".css"
        final List<HeadNode> cssLinks = new ArrayList<HeadNode>();
        if (!isStylesheetExist(theme)) {
            final HeadNode cssLink = new HeadNode();
            cssLink.setTagName("link");
            final Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("type", "text/css");
            attributes.put("rel", "stylesheet");
            attributes.put("href", "consoleResource?location=" + cssFileName);
            attributes.put("title", theme);
            attributes.put("id", theme);
            cssLink.setAttributes(attributes);
            cssLinks.add(cssLink);
            DOMUtils.getInstance().addHeaders(cssLinks);
        }
    }

    /**
     * Add a theme stylesheet link is the link does not still exist
     */
    @Deprecated
    public void addThemeStyleSheetLink(final String themeName, final String[] cssNames, final String[] cssFileNames) {
        final List<HeadNode> cssLinks = new ArrayList<HeadNode>();
        for (int i = 0; i < cssNames.length; i++) {
            if (!isStylesheetExist(CSS_ID_PRE + cssNames[i])) {
                final HeadNode cssLink = new HeadNode();
                cssLink.setTagName("link");
                final Map<String, String> attributes = new HashMap<String, String>();
                attributes.put("type", "text/css");
                attributes.put("rel", "stylesheet");
                attributes.put("href", "themeResource?location=" + cssFileNames[i] + "&theme=" + themeName);
                attributes.put("title", cssNames[i]);
                attributes.put("id", CSS_ID_PRE + cssNames[i]);
                cssLink.setAttributes(attributes);
                cssLinks.add(cssLink);
                DOMUtils.getInstance().addHeaders(cssLinks);
            }
        }
    }

    /**
     * Find if a link of a stylesheet is in the header
     */
    protected boolean isStylesheetExist(final String cssName) {
        boolean result = false;
        final Element cssElement = DOM.getElementById(cssName);
        if (cssElement != null) {
            result = true;
        }
        return result;
    }

    /**
     * clean the body attributes
     */
    protected void cleanBodyAttributes() {
        final Element bodyElement = RootPanel.getBodyElement();
        bodyElement.removeAttribute("class");
        bodyElement.removeAttribute("style");
        bodyElement.removeAttribute("onload");
    }

    /**
     * Replace the content of the body by the template body
     *
     * @param applicationHTMLPanel
     * @param elementId
     */
    protected void addApplicationContent(final HTMLPanel applicationHTMLPanel, final String elementId) {

        RootPanel container = RootPanel.get(elementId);
        if (container == null) {
            container = RootPanel.get();
        }
        container.clear();
        container.add(applicationHTMLPanel);
        addScriptElementToDOM(applicationHTMLPanel.getElement(), container.getElement());
    }

    /**
     * Replace the content of the body by the template body
     *
     * @param pageHTMLPanel
     * @param processHTMLPanel
     * @param elementId
     */
    protected void addPageContent(final HTMLPanel pageHTMLPanel, final HTMLPanel processHTMLPanel, final String elementId) {

        if (processHTMLPanel != null) {
            processHTMLPanel.add(pageHTMLPanel, elementId);
            addScriptElementToDOM(pageHTMLPanel.getElement(), processHTMLPanel.getElement());
        } else {
            final RootPanel container = RootPanel.get(STATIC_CONTENT_ELEMENT_ID);
            container.clear();
            container.add(pageHTMLPanel);
            addScriptElementToDOM(pageHTMLPanel.getElement(), container.getElement());
        }
    }

    /**
     * Add the body attributes
     *
     * @param bodyAttributes
     * @return the onload attribute value if it exists, null otherwise
     */
    protected String addBodyAttributes(final Map<String, String> bodyAttributes) {

        String onloadValue = null;
        if (bodyAttributes != null && !bodyAttributes.isEmpty()) {
            final Element bodyElement = RootPanel.getBodyElement();
            for (final Entry<String, String> bodyAttribute : bodyAttributes.entrySet()) {
                if ("class".equalsIgnoreCase(bodyAttribute.getKey())) {
                    bodyElement.setClassName(bodyAttribute.getValue());
                } else {
                    if ("onload".equalsIgnoreCase(bodyAttribute.getKey())) {
                        onloadValue = bodyAttribute.getValue();
                    }
                    // fix for IE7 and IE8 failing to evaluate scripts in inserted HTML pages headers
                    if ("ieonload".equalsIgnoreCase(bodyAttribute.getKey()) && (isIE7() || isIE8())) {
                        onloadValue = onloadValue == null ? bodyAttribute.getValue() : onloadValue + bodyAttribute.getValue();
                    } else {
                        bodyElement.setAttribute(bodyAttribute.getKey(), bodyAttribute.getValue());
                    }
                }
            }
        }
        return onloadValue;
    }

    /**
     * @return true if the current web browser is ie7
     */
    public boolean isIE7() {
        return getUserAgent().indexOf("MSIE 7.") != -1;
    }

    /**
     * @return true if the current web browser is ie8
     */
    public boolean isIE8() {
        return getUserAgent().indexOf("MSIE 8.") != -1;
    }

    /**
     * @return the user Agent string in lower case
     */
    private String getUserAgent() {
        return Window.Navigator.getUserAgent();
    }

    /**
     * @param headNode
     * @return true if the head node present in the template is useful
     */
    protected boolean isHeadNodeUseful(final HeadNode headNode) {

        if (headNode.getTagName() != null && headNode.getTagName().equalsIgnoreCase("meta")) {
            final Map<String, String> attributes = headNode.getAttributes();
            if (attributes.get("http-equiv") != null && attributes.get("http-equiv").equalsIgnoreCase("content-type")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Remove an element of the entry point header if it's present in the template (only for certain elements)
     *
     * @param parentElement
     * @param headNode
     */
    protected void removeOldHeaderElementIfPresent(final Element parentElement, final HeadNode headNode) {

        final int headChildrenCount = DOM.getChildCount(parentElement);
        if (headNode.getTagName() != null && headNode.getTagName().equalsIgnoreCase("title")) {
            for (int i = 0; i < headChildrenCount; i++) {
                final Element headChildElement = DOM.getChild(parentElement, i);
                if (headNode.getTagName().equalsIgnoreCase(headChildElement.getTagName())) {
                    DOM.removeChild(parentElement, headChildElement);
                    break;
                }
            }
        }
    }

    /**
     * insert a content inside an element
     *
     * @param htmlPanel
     * @param pageLabelElementId
     * @param pageLabel
     */
    public void insertInElement(final HTMLPanel htmlPanel, final String elementId, final String content) {

        insertInElement(htmlPanel, elementId, content, false);
    }

    /**
     * insert a content inside an element
     *
     * @param htmlPanel
     * @param pageLabelElementId
     * @param pageLabel
     * @param allowHTML
     */
    public void insertInElement(final HTMLPanel htmlPanel, final String elementId, final String content, final boolean preventHTML) {

        if (htmlPanel != null) {
            final HTML htmlContent = new HTML();
            if (preventHTML) {
                htmlContent.setText(content);
            } else {
                htmlContent.setHTML(content);
            }
            htmlPanel.add(htmlContent, elementId);
        } else {
            final Element element = DOM.getElementById(elementId);
            if (element != null) {
                if (preventHTML) {
                    element.setInnerText(content);
                } else {
                    element.setInnerHTML(content);
                }
            }
        }
    }

    /**
     * Remove the required element from the body and remove its css classes
     *
     * @param elementToRemoveId
     */
    public void cleanBody(final String elementToRemoveId) {

        final Element elementToRemove = DOM.getElementById(elementToRemoveId);
        if (elementToRemove != null) {
            DOM.removeChild(RootPanel.getBodyElement(), elementToRemove);
        }
        RootPanel.getBodyElement().setClassName("");
    }

    /**
     * Indicates whether the page is in a frame or not this method is meant to be called in the form frame (not in the
     * application window)
     *
     * @return true if the page is in a frame
     */
    native public boolean isPageInFrame()
    /*-{
        var applicationWindow = window.parent;
        if (applicationWindow == window.top) {
            return false;
        } else {
            return true;
        }
    }-*/;

    /**
     * Override the web browser native inputs if the js to do so is present
     *
     * @return true if the Javascript was applied
     */
    native public boolean overrideBrowserNativeInputs()
    /*-{
        try {
            $wnd.overrideBrowserNativeInputs();
            return true;
        } catch(e) {
            //do nothing (the javascript to override the browser native inputs isn't in the header)
            return false;
        }
    }-*/;

    /**
     * perform a Javascript evaluation
     *
     * @param jsSourceCode
     *            the source code to execute
     */
    native public void javascriptEval(String jsSourceCode)
    /*-{
         $wnd.eval(jsSourceCode);
    }-*/;

    /**
     * To make script in scriptElements work , need to add script elements in the currentElement to parentElement
     *
     * @param currentElement
     * @param parentElement
     */
    public void addScriptElementToDOM(final Element currentElement, final Element parentElement) {
        final List<Element> list = new ArrayList<Element>();
        final NodeList<com.google.gwt.dom.client.Element> scripts = currentElement.getElementsByTagName("script");
        for (int i = 0; i < scripts.getLength(); i++) {
            list.add((Element) scripts.getItem(i));
        }

        for (int i = 0; i < list.size(); i++) {
            final Element e = list.get(i);
            e.removeFromParent();
            final Element scriptElement = DOM.createElement("script");
            final String type = e.getAttribute("type");
            if (!isEmpty(type)) {
                scriptElement.setAttribute("type", type);
            }
            final String language = e.getAttribute("language");
            if (!isEmpty(language)) {
                scriptElement.setAttribute("language", language);
            }
            final String src = e.getAttribute("src");
            if (!isEmpty(src)) {
                scriptElement.setAttribute("src", src);
            }
            scriptElement.setInnerText(e.getInnerText());
            parentElement.appendChild(scriptElement);
        }
    }

    private boolean isEmpty(final String str) {
        return str == null || str.trim().length() == 0 || str.equals("null");
    }

    public boolean isInternetExplorer() {
        return "Microsoft Internet Explorer".equals(Window.Navigator.getAppName());
    }

    /**
     * Hide loading
     */
    public void hideLoading() {
        final Element loadingElement = DOM.getElementById("loading");
        if (loadingElement != null) {
            loadingElement.getStyle().setProperty("display", "none");
        }
    }

    /**
     * Display a message to let the user know that the data are not yet
     * available.
     */
    public void displayLoading() {
        // Show the loading message and display the GUI.
        Element theElement;
        theElement = DOM.getElementById("loading");
        if (theElement != null) {
            theElement.getStyle().setProperty("display", "table");
            theElement.getStyle().setProperty("zIndex", "999");
        }
    }

}
