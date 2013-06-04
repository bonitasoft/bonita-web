package org.bonitasoft.web.toolkit.client.ui.component;

import java.util.LinkedList;

import org.bonitasoft.web.toolkit.client.ui.component.core.Components;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

import com.google.gwt.user.client.Element;

public class Html extends Components {

    private String html = null;

    public Html() {
        super();
        this.html = HTML.div() + HTML._div();
    }

    public Html(final String html) {
        super();
        this.html = html;
    }

    public void setHtml(final String html) {
        this.html = html;
        if (isGenerated()) {
            replace(makeElements());
        }
    }

    @Override
    protected LinkedList<Element> makeElements() {
        return XML.makeElements(this.html);
    }

}
