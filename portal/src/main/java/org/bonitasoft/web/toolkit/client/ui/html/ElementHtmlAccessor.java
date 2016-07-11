package org.bonitasoft.web.toolkit.client.ui.html;

import com.google.gwt.dom.client.Element;

/**
 * Created by Vincent Elcrin
 * Date: 11/10/13
 * Time: 16:11
 */
public class ElementHtmlAccessor implements HtmlAccessor {

    private Element el;

    public ElementHtmlAccessor(Element el) {
        this.el = el;
    }

    @Override
    public String getInnerHTML() {
        return el.getInnerHTML();
    }

    @Override
    public void setInnerHTML(String html) {
        el.setInnerHTML(html);
    }
}
