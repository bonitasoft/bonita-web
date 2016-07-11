package org.bonitasoft.web.toolkit.client.ui.component.filler;

import static com.google.gwt.query.client.GQuery.$;

import java.util.Map;
import java.util.Map.Entry;

import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

import com.google.gwt.dom.client.Element;

public abstract class SelectFiller extends Filler<Component> {

    protected String selectedValue = null;

    public SelectFiller() {
        super();
    }

    public SelectFiller(final String selectedValue) {
        super();
        this.selectedValue = selectedValue;
    }

    @Override
    protected void setData(final String json, final Map<String, String> headers) {
        Element e = this.target.getElement();
        if (!"select".equals(e.getNodeName())) {
            e = $("select", e).get(0);
        }

        final Map<String, String> options = JSonItemReader.parseMap(json);

        final StringBuilder html = new StringBuilder();

        for (final Entry<String, String> entry : options.entrySet()) {

            String selected = "";
            if (entry.getValue().equals(this.selectedValue)) {
                selected = " selected=\"selected\"";
            }
            html.append("<option value=\"").append(entry.getValue()).append("\"").append(selected).append(">").append(entry.getKey()).append("</option>");
        }

        e.setInnerHTML(html.toString());
    }
}
