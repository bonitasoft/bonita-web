package org.bonitasoft.console.client.uib.databinder;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

public class InsertFactory {

    public static Insert after(final int index) {
        return new Insert() {

            @Override
            public Node into(Element parent, Node child) {
                return child.insertAfter(child, parent.getChild(index));
            }
        };
    }

    public static Insert before(final int index) {
        return new Insert() {

            @Override
            public Node into(Element parent, Node child) {
                return child.insertBefore(child, parent.getChild(index));
            }
        };
    }

    public static Insert first() {
        return new Insert() {

            @Override
            public Node into(Element parent, Node child) {
                return parent.insertFirst(child);
            }
        };
    }

    public static Insert last() {
        return new Insert() {

            @Override
            public Node into(Element parent, Node child) {
                return parent.appendChild(child);
            }
        };
    }
}
