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

import java.util.List;
import java.util.Map;

/**
 * @author Séverin Moussel
 * 
 */
final class BarChartSerie extends ChartSerie {

    public BarChartSerie(final String label) {
        super(label);
    }

    public BarChartSerie(final String label, final List<ChartPoint> points) {
        super(label, points);
    }

    public BarChartSerie(final String label, final Map<Long, Long> points) {
        super(label, points);
    }

    public BarChartSerie(final String label, final ChartPoint... points) {
        super(label, points);
    }

    @Override
    protected void initOptions() {
        addOption("bars.show", true);
        addOption("bars.fill", true);
    }

}
