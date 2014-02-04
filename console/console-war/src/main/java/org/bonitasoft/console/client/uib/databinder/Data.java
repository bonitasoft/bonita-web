package org.bonitasoft.console.client.uib.databinder;

import com.google.gwt.dom.client.Node;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;

public class Data {

    private Node node;

    private class Wrapper extends Composite {

        Wrapper(Widget widget) {
            initWidget(widget);
            onAttach();
        }
    }

    public Data(String value) {
        if(!StringUtil.isBlank(value)) {
            node = getNode(SafeHtmlUtils.fromString(value));
        }
    }

    public Data(Object value) {
        this(String.valueOf(value));
    }

    public Data(SafeHtml html) {
        node = getNode(html);
    }

    public Data or(String value) {
        if(node == null) {
            node = getNode(SafeHtmlUtils.fromString(value));
        }
        return this;
    }

    public Data(Widget widget) {
        Wrapper wrapper = new Wrapper(widget);
        // ensure we wont leak events
        RootPanel.detachOnWindowClose(wrapper);
        node = wrapper.getElement();
    }

    public Node getNode() {
        return node;
    }

    private Node getNode(SafeHtml html) {
        return SafeHtmlParser.parse(html).getItem(0);
    }
}
