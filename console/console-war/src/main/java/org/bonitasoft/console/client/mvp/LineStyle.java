package org.bonitasoft.console.client.mvp;

import com.google.gwt.user.cellview.client.CellList;

/**
 * @author Vincent Elcrin
 */
public class LineStyle implements CellList.Style {

    private final String style;

    public LineStyle(String style) {
        this.style = style;
    }

    @Override
    public String cellListEvenItem() {
        return "even " + style;
    }

    @Override
    public String cellListKeyboardSelectedItem() {
        return "selected";
    }

    @Override
    public String cellListOddItem() {
        return "odd " + style;
    }

    @Override
    public String cellListSelectedItem() {
        return "selected";
    }

    @Override
    public String cellListWidget() {
        return null;
    }

    @Override
    public boolean ensureInjected() {
        return false;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
