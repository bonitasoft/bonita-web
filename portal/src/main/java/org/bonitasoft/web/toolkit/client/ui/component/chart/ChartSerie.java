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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Séverin Moussel
 * 
 */
abstract class ChartSerie extends ChartItem {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ChartSerie(final String label) {
        super(label);
    }

    public ChartSerie(final String label, final List<ChartPoint> points) {
        this(label);
        this.points = points;
    }

    public ChartSerie(final String label, final Map<Long, Long> points) {
        this(label);
        this.addPoints(points);
    }

    public ChartSerie(final String label, final ChartPoint... points) {
        this(label);
        for (int i = 0; i < points.length; i++) {
            this.addPoint(points[i]);
        }
    }

    public List<ChartPoint> getPoints() {
        return this.points;
    }

    public void addPoint(final long x, final long y) {
        this.addPoint(new ChartPoint(x, y));
    }

    @Override
    public void addPoint(final ChartPoint point) {
        super.addPoint(point);
    }

    public Map<Long, Long> getPointsAsMap() {
        final Map<Long, Long> result = new LinkedHashMap<Long, Long>();
        for (final ChartPoint point : this.points) {
            result.put(point.getxValue(), point.getyValue());
        }
        return result;
    }

    public int size() {
        return this.points.size();
    }

    public void addPoints(final List<ChartPoint> points) {
        for (final ChartPoint point : points) {
            this.points.add(point);
        }
        reduce();
    }

    public void addPoints(final Map<Long, Long> points) {
        for (final Entry<Long, Long> point : points.entrySet()) {
            this.points.add(new ChartPoint(point.getKey(), point.getValue()));
        }
        reduce();
    }

    public void setPoints(final List<ChartPoint> points) {
        this.points.clear();
        this.addPoints(points);
    }

    public void setPoints(final Map<Long, Long> points) {
        this.points.clear();
        this.addPoints(points);
    }

    public void clear() {
        this.points.clear();
    }

}
