package org.bonitasoft.console.client.mvp;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellList;

/**
 * @author Vincent Elcrin
 */
public class TemplateList<T> extends CellList<T> {

    public TemplateList(final LineTemplate<T> line, final String style) {
        super(line, new Resources() {

            @Override
            public ImageResource cellListSelectedBackground() {
                return new EmptyImageResource();
            }

            @Override
            public Style cellListStyle() {
                return new LineStyle(style);
            }
        });
    }
}
