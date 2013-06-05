package org.bonitasoft.web.toolkit.client.ui.component.table;

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.html.HTML;

import com.google.gwt.user.client.Element;

public class TableFilterHidden extends TableFilter {

    public TableFilterHidden(final Table table, final String name, final String value) {
        super(table, null, null, name, value);
    }

    @Override
    protected Element makeElement() {
        return (Element) $(HTML.inputHidden(this.name, this.value)).get(0);
    }

    @Override
    public String getValue() {
        return this.value;
    }

}
