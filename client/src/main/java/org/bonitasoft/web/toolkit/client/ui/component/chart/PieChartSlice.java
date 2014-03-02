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

/**
 * @author Séverin Moussel
 * 
 */
final class PieChartSlice extends ChartItem {

    long index = 0;

    public PieChartSlice(final String label) {
        super(label);
        this.maxLength = 1;
    }

    public PieChartSlice(final String label, final long index, final long value) {
        this(label);
        set(index, value);
    }

    public PieChartSlice set(final long index, final long value) {
        setIndex(index);
        setValue(value);
        return this;
    }

    public PieChartSlice setIndex(final long index) {
        this.index = index;
        return this;
    }

    public PieChartSlice setValue(final long value) {
        addPoint(new ChartPoint(this.index, value));
        return this;
    }

    @Override
    protected void initOptions() {
    }

}
