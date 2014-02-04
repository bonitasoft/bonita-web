package org.bonitasoft.console.client.uib.databinder;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.Composite;

import java.util.ArrayList;
import java.util.List;

import static org.bonitasoft.console.client.uib.databinder.InsertFactory.last;

public class DataBinder {

    private List<Node> nodes = new ArrayList<Node>();

    public DataBinder(Data... data) {
        for (Data aData : data) {
            nodes.add(aData.getNode());
        }
    }

    public static DataBinder bind(Data... data) {
        return new DataBinder(data);
    }

    public void to(final Element element, final Insert insert) {
        append(element, insert.into(element, nodes.remove(0)), nodes);
    }

    public void to(Element element) {
        to(element, last());
    }

    /**
     * Recursively append all nodes to parent.
     *
     * @param parent
     * @param pivot
     * @param nodes
     */
    private void append(Element parent, Node pivot, List<Node> nodes) {
        if(nodes.size() > 0) {
            append(
                    parent,
                    parent.insertAfter(nodes.remove(0), pivot),
                    nodes);
        }
    }
}
