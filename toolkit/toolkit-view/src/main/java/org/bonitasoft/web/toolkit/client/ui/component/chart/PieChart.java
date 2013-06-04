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

import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.utils.Color;

/**
 * @author Séverin Moussel
 * 
 */
public final class PieChart extends Chart {

    public PieChart addSlice(final String label, final long value) {
        final PieChartSlice item = (PieChartSlice) this.getItem(label, true);
        item.setValue(value);
        setItem(label, item);
        return this;
    }

    public PieChart showLegendInSlices() {

        this.addOption("legend.show", false);

        this.removeOption("legend.labelFormatter");
        this.removeOption("legend.labelBoxBorderColor");
        this.removeOption("legend.noColumns");
        this.removeOption("legend.position");
        this.removeOption("legend.margin");
        this.removeOption("legend.backgroundColor");
        this.removeOption("legend.backgroundOpacity");
        this.removeOption("legend.container");

        // Percentage of the radius where to place the label (from 0 to 1)
        this.addOption("series.pie.label.radius", .6);
        // Color of the background (in hexadecimal)
        this.addOption("series.pie.label.background.color", "#000");
        // Opacity of the background (from 0 to 1)
        this.addOption("series.pie.label.background.opacity", 0.5);

        // // Hides the labels of any pie slice that is smaller than the specified percentage (ranging from 0 to 1)
        // this.addOption("series.pie.label.background.threshold", 0);

        return this;
    }

    public PieChart setTilt(final int tiltDegree) {
        assert tiltDegree >= 0 && tiltDegree <= 90;

        this.addOption("series.pie.tilt", ((float) 90 - (float) 45) / 90);

        return this;
    }

    public PieChart setCombineThreshold(final float percent, final String label, final Color color) {
        assert percent <= 1 && percent >= 0;

        this.addOption("series.pie.combine.threshold", percent);
        this.addOption("series.pie.combine.label", label);
        this.addOption("series.pie.combine.color", color.toHexString());

        return this;
    }

    public PieChart setCombineThreshold(final float percent, final String label, final String color) {
        return this.setCombineThreshold(percent, label, new Color(color));
    }

    public PieChart setCombineThreshold(final float percent) {
        return this.setCombineThreshold(percent, "others", new Color("#eee"));
    }

    @Override
    protected void initOptions() {
        super.initOptions();

        this.addOption("series.pie.show", true); // Default : false

        this.addOption("legend.show", true);
        this.addOption("legend.labelFormatter", null);
        this.addOption("legend.labelBoxBorderColor", "#ccc");
        this.addOption("legend.noColumns", 1);
        this.addOption("legend.position", "ne");
        this.addOption("legend.margin", 2);
        this.addOption("legend.backgroundColor", "#fff");
        this.addOption("legend.backgroundOpacity", 0.6);
        this.addOption("legend.container", null);

        // // Pixel width of the border of each slice - Default : 1
        // this.addOption("series.pie.stroke.width", 1);
        // // Color of the border of each slice. Hexadecimal - Default : "#fff"
        // this.addOption("series.pie.stroke.color", "#fff");

        // // Angle of the first slice position (1.5 for top, 1 for left, 0.5 for bottom and 0 for right) - Default : 1.5
        // this.addOption("series.pie.startAngle", 1.5);

    }

    @Override
    protected ChartItem createNewItem(final String label) {
        return new PieChartSlice(label).setIndex(this.data.size());
    }

    /**
     * @param itemDefinition
     * @param labelAttributeName
     *            The name of the attribute in the item to use as the label of a slice.
     * @param valueAttributeName
     *            The name of the attribute in the item to use as the value of a slice.
     * @return This method returns thePieChart itself to allow cascading calls.
     */
    public PieChart setFiller(final ItemDefinition itemDefinition, final String labelAttributeName, final String valueAttributeName) {
        this.setFiller(itemDefinition, labelAttributeName, valueAttributeName, -1);
        return this;
    }

    /**
     * @param itemDefinition
     * @param labelAttributeName
     *            The name of the attribute in the item to use as the label of a slice.
     * @param valueAttributeName
     *            The name of the attribute in the item to use as the value of a slice.
     * @param updateFrequency
     *            in milliseconds
     * @return This method returns thePieChart itself to allow cascading calls.
     */
    public PieChart setFiller(final ItemDefinition itemDefinition, final String labelAttributeName, final String valueAttributeName, final int updateFrequency) {
        addFiller(new PieChartFiller(itemDefinition, labelAttributeName, valueAttributeName).setRepeatEvery(updateFrequency));
        return this;
    }

    @Override
    public final int getMaxPoints() {
        return getMaxItems();
    }

}
