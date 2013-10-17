package org.bonitasoft.web.toolkit.client.ui.component;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Element;
import org.bonitasoft.web.toolkit.client.ui.component.core.Components;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

import java.util.LinkedList;

public class Html extends Components {

    private String html = null;

    public Html(final String html) {
        this.html = html;
    }

    public Html(final SafeHtml html) {
        this.html = html.asString();
    }

    @Override
    protected LinkedList<Element> makeElements() {
        return XML.makeElements(this.html);
    }

}
