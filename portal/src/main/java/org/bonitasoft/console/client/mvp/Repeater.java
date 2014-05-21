package org.bonitasoft.console.client.mvp;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellList;

/**
 * @author Vincent Elcrin
 */
public class Repeater<T> extends CellList<T> {

    public Repeater(final TemplateRepeat<T> template) {
        super(template, new Resources() {

            @Override
            public ImageResource cellListSelectedBackground() {
                return new EmptyImageResource();
            }

            @Override
            public Style cellListStyle() {
                return new CellStyle(template.getStyle());
            }
        });
    }
}
