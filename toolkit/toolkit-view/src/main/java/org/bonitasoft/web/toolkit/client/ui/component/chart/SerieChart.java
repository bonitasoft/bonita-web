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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Date;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.json.JSonUtil;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.utils.Color;

/**
 * @author Séverin Moussel
 * 
 */
public class SerieChart extends Chart {

    protected AXIS_FORMAT xAxisFormat = AXIS_FORMAT.AUTO;

    protected String xAxisFormatter = null;

    protected AXIS_FORMAT yAxisFormat = AXIS_FORMAT.AUTO;

    protected String yAxisFormatter = null;

    public static enum TYPE {
        LINE,
        BARS
    };

    public static enum AXIS_FORMAT {
        // default (float)
        AUTO,

        // Integer
        ROUND,

        // Custom function
        PREFIX,
        SUFFIX,
        WRAP,

        // "dd/mm/yyyy"
        DATE,
        // Custom date format
        DATE_CUSTOM,

        // "dd"
        DATE_DAY,
        // "ddd" : Mon, Tue, Wed, Thu, Fri, Sat, Sun
        DATE_DAY_NAME_SHORT,
        // "dddd" : Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
        DATE_DAY_NAME,

        // "m"
        DATE_MONTH,
        // "mmm" : Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
        DATE_MONTH_NAME_SHORT,
        // "mmmm" : January, February, March, April, May, June, July, August, September, October, November, December
        DATE_MONTH_NAME,

        // "yy"
        DATE_YEAR_TWO_DIGITS,
        // "yyyy"
        DATE_YEAR,

        // "mm/dd/YYYY hh:MM:ss"
        DATETIME,

        // "hh:MM"
        TIME,
        // "hh:MM:ss"
        TIME_WITH_SECONDS,
        // "hh"
        TIME_HOUR,
        // "MM"
        TIME_MINUTE,
        // "ss"
        TIME_SECOND
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SERIES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final SerieChart setSerieType(final String label, final TYPE drawingType) {
        ChartSerie serie = null;
        switch (drawingType) {
            case LINE:
                serie = new LineChartSerie(label);
                break;
            case BARS:
                serie = new BarChartSerie(label);
                break;
        }
        setItem(label, serie);
        return this;
    }

    public final SerieChart clearSerie(final String label) {
        final ChartSerie serie = (ChartSerie) this.getItem(label);
        serie.clear();
        return this;
    }

    @Override
    protected final SerieChart setItem(final String label, final ChartItem item) {
        ((ChartSerie) item).setMaxLength(this.maxPoints);
        super.setItem(label, item);
        return this;
    }

    @Override
    protected ChartSerie createNewItem(final String label) {
        return new LineChartSerie(label);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // POINTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final SerieChart addPoint(final String label, final ChartPoint point) {
        this.getItem(label, true).addPoint(point);

        if (this.autoRedraw) {
            refresh();
        }

        return this;
    }

    public final SerieChart addPoints(final String label, final Map<Long, Long> data) {
        final LineChartSerie serie = (LineChartSerie) this.getItem(label, true);
        serie.addPoints(data);
        return this;
    }

    public final SerieChart setPoints(final String label, final Map<Long, Long> data) {
        setItem(label, new LineChartSerie(label, data));
        return this;
    }

    public final SerieChart addPoint(final String serieTitle, final long x, final long y) {
        return this.addPoint(serieTitle, new ChartPoint(x, y));
    }

    public final SerieChart addPoint(final String serieTitle, final long y) {
        return this.addPoint(serieTitle, y, new Date().getTime());
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OPTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void initOptions() {
        super.initOptions();

        this.addOption("grid.show", true);
        this.addOption("grid.aboveData", false);
        this.addOption("grid.color", "#bbb");
        this.addOption("grid.backgroundColor", "#fff");
        this.addOption("grid.labelMargin", 5);
        this.addOption("grid.axisMargin", 50);
        this.addOption("grid.borderWidth", 0);
        this.addOption("grid.borderColor", "#aaa");
        this.addOption("grid.minBorderMargin", 5);
        this.addOption("grid.clickable", false);
        this.addOption("grid.hoverable", true);
        this.addOption("grid.autoHighlight", true);
        this.addOption("grid.mouseActiveRadius", 30);

        this.addOption("legend.show", true);
        this.addOption("legend.labelFormatter", null);
        this.addOption("legend.labelBoxBorderColor", "#ccc");
        this.addOption("legend.noColumns", 8);
        this.addOption("legend.position", "nw");
        this.addOption("legend.margin", 2);
        this.addOption("legend.backgroundColor", "#fff");
        this.addOption("legend.backgroundOpacity", 0.6);
        this.addOption("legend.container", null);

        this.addOption("series.shadowSize", 0);

    }

    public final Chart setGridDisplay(final boolean display) {
        this.addOption("grid.show", display);
        return this;
    }

    public final SerieChart setGridAboveData(final boolean above) {
        this.addOption("grid.aboveData", above);
        return this;
    }

    public final SerieChart setGridColor(final String color) {
        return this.setGridColor(new Color(color));
    }

    public final SerieChart setGridColor(final Color color) {
        this.addOption("grid.color", color.toHexString());
        return this;
    }

    public final SerieChart setGridBackgroundColor(final String color) {
        return this.setGridBackgroundColor(new Color(color));
    }

    public final SerieChart setGridBackgroundColor(final Color color) {
        this.addOption("grid.backgroundColor", color.toHexString());
        return this;
    }

    public final SerieChart setGridLabelMargin(final int margin) {
        this.addOption("grid.labelMargin", margin);
        return this;
    }

    public final SerieChart setGridAxisMargin(final int margin) {
        this.addOption("grid.axisMargin", margin);
        return this;
    }

    public final SerieChart setGridBorderWidth(final int width) {
        this.addOption("grid.borderWidth", width);
        return this;
    }

    public final SerieChart setGridBorderColor(final String color) {
        return this.setGridBorderColor(new Color(color));
    }

    public final SerieChart setGridBorderColor(final Color color) {
        this.addOption("grid.borderColor", color.toHexString());
        return this;
    }

    public final SerieChart setGridBorderMargin(final int margin) {
        this.addOption("grid.minBorderMargin", margin);
        return this;
    }

    public SerieChart setXAxisTickSize(final long tickSize) {
        this.addOption("xaxis.tickSize", tickSize);
        return this;
    }

    public SerieChart setYAxisTickSize(final long tickSize) {
        this.addOption("yaxis.tickSize", tickSize);
        return this;
    }

    public SerieChart setBarsWidth(final int barsWidth) {
        this.addOption("bars.barWidth", barsWidth);
        this.addOption("bars.align", "center");
        return this;
    }

    public final SerieChart setXAxisFormatter(final AXIS_FORMAT xAxisFormat) {
        return this.setXAxisFormatter(xAxisFormat, null);
    }

    public final SerieChart setXAxisFormatter(final AXIS_FORMAT xAxisFormat, final String custom) {
        setAxisFormatter(true, xAxisFormat, custom);
        return this;
    }

    public final SerieChart setYAxisFormatter(final AXIS_FORMAT yAxisFormat) {
        return this.setYAxisFormatter(yAxisFormat, null);
    }

    public final SerieChart setYAxisFormatter(final AXIS_FORMAT yAxisFormat, final String custom) {
        setAxisFormatter(false, yAxisFormat, custom);
        return this;
    }

    private final void setAxisFormatter(final boolean xAxis, final AXIS_FORMAT axisFormat, final String custom) {
        AXIS_FORMAT format = axisFormat;
        String formatter = custom;

        switch (axisFormat) {
            case AUTO:
                formatter = null;

            case ROUND:
                formatter = "ROUND";
                break;

            case PREFIX:
                format = AXIS_FORMAT.WRAP;
                formatter = "WRAP{before:" + JSonUtil.quote(formatter) + ",after:\"\")}";
                break;

            case SUFFIX:
                format = AXIS_FORMAT.WRAP;
                formatter = "WRAP{before:\"\",after:" + JSonUtil.quote(formatter) + "}";
                break;

            case WRAP:
                format = AXIS_FORMAT.WRAP;
                final String[] parts = formatter.split("%%");
                formatter = "WRAP{before:" + JSonUtil.quote(parts[0]) + ",after:" + JSonUtil.quote(parts[1]) + "}";
                break;

            case DATE:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = _("mm/dd/yy");
                break;

            case DATE_DAY:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "dd";
                break;
            case DATE_DAY_NAME_SHORT:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "ddd";
                break;
            case DATE_DAY_NAME:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "dddd";
                break;
            case DATE_MONTH:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "m";
                break;
            case DATE_MONTH_NAME_SHORT:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "mmm";
                break;
            case DATE_MONTH_NAME:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "mmmm";
                break;
            case DATE_YEAR_TWO_DIGITS:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "yy";
                break;
            case DATE_YEAR:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "yyyy";
                break;

            case DATETIME:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = _("mm/dd/yyyy hh:MM:ss");
                break;

            case TIME:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "hh:MM";
                break;
            case TIME_WITH_SECONDS:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "hh:MM:ss";
                break;
            case TIME_HOUR:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "hh";
                break;
            case TIME_MINUTE:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "MM";
                break;
            case TIME_SECOND:
                format = AXIS_FORMAT.DATE_CUSTOM;
                formatter = "ss";
                break;

        }

        if (xAxis) {
            this.xAxisFormat = format;
            this.xAxisFormatter = formatter;
        } else {
            this.yAxisFormat = format;
            this.yAxisFormatter = formatter;
        }

    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILLER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public SerieChart addFiller(final String label, final ItemDefinition itemDefinition, final String xAxisValueAttributeName,
            final String yAxisValueAttributeName) {
        return this.addFiller(label, itemDefinition, xAxisValueAttributeName, yAxisValueAttributeName, -1);
    }

    public SerieChart addFiller(final String label, final ItemDefinition itemDefinition, final String xAxisValueAttributeName,
            final String yAxisValueAttributeName, final int updateFrequency) {
        setFiller(new SerieChartFiller(label, itemDefinition, xAxisValueAttributeName, yAxisValueAttributeName).setRepeatEvery(updateFrequency));
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OUTPUT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected String prepareOptions() {
        final TreeIndexed<Object> options = this.options;

        if (this.xAxisFormat != AXIS_FORMAT.AUTO) {
            this.addOption(options, "xaxis.tickFormatter", this.xAxisFormatter);
        }
        if (this.yAxisFormat != AXIS_FORMAT.AUTO) {
            this.addOption(options, "yaxis.tickFormatter", this.yAxisFormatter);
        }

        return JSonSerializer.serialize(options);
    }
}
