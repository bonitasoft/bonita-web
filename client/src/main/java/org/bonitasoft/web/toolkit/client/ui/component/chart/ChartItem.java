/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.ui.component.chart;

import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.json.JsonSerializable;
import org.bonitasoft.web.toolkit.client.ui.utils.Color;

/**
 * @author Séverin Moussel
 * 
 */
abstract class ChartItem implements JsonSerializable {

    protected List<ChartPoint> points = new LinkedList<ChartPoint>();

    private final TreeIndexed<Object> options = new TreeIndexed<Object>();

    protected String label = null;

    protected int maxLength = 0;

    protected void reduce() {
        final int size = this.points.size();
        if (this.maxLength > 0 && size > this.maxLength) {
            this.points = this.points.subList(size - this.maxLength, size);
        }
    }

    public ChartItem(final String label) {
        super();
        this.label = label;
        initOptions();
    }

    public ChartItem setColor(final String color) {
        return this.setColor(new Color(color));
    }

    public ChartItem setColor(final Color color) {
        this.addOption("color", color.toHexString());
        return this;
    }

    public ChartItem setLabel(final String label) {
        this.addOption("label", label);
        return this;
    }

    protected void addPoint(final ChartPoint point) {
        this.points.add(point);
        reduce();
    }

    protected void addOption(final String path, final Object value) {
        this.addOption(this.options, path.split("\\."), value);
    }

    private void addOption(final TreeIndexed<Object> root, final String[] path, final Object value) {
        int i = 0;
        while (i < path.length && path[i] == null) {
            i++;
        }
        if (i >= path.length) {
            return;
        }

        AbstractTreeNode<Object> node = root.get(path[i]);
        if (node == null) {
            node = new TreeIndexed<Object>();
            root.addNode(path[i], node);
        }
        if (i == path.length - 1 || !(node instanceof TreeIndexed<?>)) {
            root.addValue(path[i], value);
            return;
        }

        path[i] = null;

        this.addOption((TreeIndexed<Object>) node, path, value);
    }

    protected abstract void initOptions();

    @Override
    public String toJson() {

        this.options.addValue("label", this.label);
        this.options.addValue("data", this.points);

        return JSonSerializer.serialize(this.options);
    }

    public ChartItem setMaxLength(final int maxLength) {
        this.maxLength = maxLength;
        reduce();
        return this;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

}
