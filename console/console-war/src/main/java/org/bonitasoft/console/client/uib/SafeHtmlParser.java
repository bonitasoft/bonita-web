package org.bonitasoft.console.client.uib;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.DOM;

public class SafeHtmlParser {

    public static NodeList<Node> parse(final SafeHtml html) {
        Element element = DOM.createDiv();
        element.setInnerHTML(html.asString());
        return element.getChildNodes();
    }

    public static Node parseFirst(final SafeHtml html) {
        Element element = DOM.createDiv();
        element.setInnerHTML(html.asString());
        return element.getChild(0);
    }
}
