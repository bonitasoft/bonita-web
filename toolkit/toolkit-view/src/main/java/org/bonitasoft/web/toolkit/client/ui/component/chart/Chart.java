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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.HTMLClass;
import org.bonitasoft.web.toolkit.client.ui.html.XML;
import org.bonitasoft.web.toolkit.client.ui.utils.Color;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class Chart extends Component {

    public static enum LEGEND_POSITION {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    };

    private JavaScriptObject flotObject = null;

    protected int maxPoints = -1;

    protected int maxItems = 10;

    protected final TreeIndexed<Object> options = new TreeIndexed<Object>();

    private final List<Color> defaultColors = new LinkedList<Color>();

    protected final Map<String, ChartItem> data = new LinkedHashMap<String, ChartItem>();

    protected boolean autoRedraw = true;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Chart() {
        this(null);
    }

    public Chart(final JsId jsid) {
        super(jsid);

        initOptions();

    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OPTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected final void addOption(final String path, final Object value) {
        this.addOption(this.options, path.split("\\."), value);
    }

    protected final void addOption(final TreeIndexed<Object> options, final String path, final Object value) {
        this.addOption(options, path.split("\\."), value);
    }

    protected final void addOption(final TreeIndexed<Object> root, final String[] path, final Object value) {
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

    protected final void removeOption(final String path) {
        this.removeOption(this.options, path.split("\\."), 0);
    }

    protected final boolean removeOption(final TreeIndexed<Object> root, final String[] path, final int pos) {
        final String key = path[pos];

        final AbstractTreeNode<Object> node = root.get(key);

        if (node == null) {
            // The path lead to a non existent node
            return false;
        }

        // The path lead to
        if (pos == path.length - 1) {
            root.removeNode(key);
            return true;
        }

        // The path can't be traveled to its end
        if (!(node instanceof TreeIndexed<?>)) {
            return false;
        }

        // Continue to the end of the path
        final boolean result = this.removeOption((TreeIndexed<Object>) node, path, pos + 1);

        // If deletion has been done, we delete empty nodes
        if (result && ((TreeIndexed<Object>) node).size() == 0) {
            root.removeNode(key);
        }

        return result;
    }

    protected void initOptions() {

        // this.setDefaultColors(
        // new Color("#c00"),
        // new Color("#f99"),
        // new Color("#b88"),
        // new Color("#711"),
        // new Color("#f00"),
        // new Color("#ebb"),
        // new Color("#755"),
        // new Color("#422")
        // );
    }

    public Chart setAutoRedraw(final boolean autoRedraw) {
        this.autoRedraw = autoRedraw;
        return this;
    }

    public boolean isAutoRedraw() {
        return this.autoRedraw;
    }

    public final Chart setDefaultColors(final List<Color> colors) {
        this.defaultColors.clear();
        this.defaultColors.addAll(colors);
        return this;
    }

    public final Chart setDefaultColors(final Color... colors) {
        this.defaultColors.clear();
        for (int i = 0; i < colors.length; i++) {
            this.defaultColors.add(colors[i]);
        }
        return this;
    }

    public final Chart setDefaultColors(final String... colors) {
        this.defaultColors.clear();
        for (int i = 0; i < colors.length; i++) {
            this.defaultColors.add(new Color(colors[i]));
        }
        return this;
    }

    public final Chart setMaxPoints(final int maxPoints) {
        this.maxPoints = maxPoints;

        for (final ChartItem item : this.data.values()) {
            if (item instanceof ChartSerie) {
                ((ChartSerie) item).setMaxLength(maxPoints);
            }
        }
        return this;
    }

    public int getMaxPoints() {
        return this.maxPoints;
    }

    public final Chart setMaxItems(final int maxItems) {
        this.maxItems = maxItems;
        return this;
    }

    public int getMaxItems() {
        return this.maxItems;
    }

    public final Chart setLegendDisplay(final boolean display) {
        this.addOption("legend.show", display);
        return this;
    }

    public final Chart setLegendLabelBorderColor(final String color) {
        return this.setLegendLabelBorderColor(new Color(color));
    }

    public final Chart setLegendLabelBorderColor(final Color color) {
        this.addOption("legend.labelBoxBorderColor", color.toHexString());
        return this;
    }

    public final Chart setLegendColumnsNumber(final int columns) {
        this.addOption("legend.noColumns", columns);
        return this;
    }

    public final Chart setLegendPosition(final LEGEND_POSITION position) {
        String pos = null;
        switch (position) {
            default:
            case TOP_LEFT:
                pos = "ne";
                break;
            case TOP_RIGHT:
                pos = "nw";
                break;
            case BOTTOM_LEFT:
                pos = "se";
                break;
            case BOTTOM_RIGHT:
                pos = "sw";
                break;
        }
        this.addOption("legend.position", pos);
        return this;
    }

    public final Chart setLegendMargin(final int margin) {
        this.addOption("legend.margin", margin);
        return this;
    }

    public final Chart setLegendBackgroundColor(final String color) {
        return this.setLegendBackgroundColor(new Color(color));
    }

    public final Chart setLegendBackgroundColor(final Color color) {
        this.addOption("legend.labelBoxBorderColor",
                "#" + Integer.toHexString(color.getRed()) + Integer.toHexString(color.getGreen()) + Integer.toHexString(color.getBlue())
                );
        this.addOption("legend.backgroundOpacity", color.getAlpha() / 255);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DATA
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected Chart setItem(final String label, final ChartItem item) {
        if (!this.data.containsKey(label) && this.defaultColors.size() > this.data.size()) {
            item.setColor(this.defaultColors.get(this.data.size()));
        }

        this.data.put(label, item);
        reduce();

        return this;
    }

    protected final ChartItem getItem(final String label) {
        return this.getItem(label, false);
    }

    protected final ChartItem getItem(final String label, final boolean createIfNotExists) {
        if (this.data.get(label) == null && createIfNotExists) {
            setItem(label, createNewItem(label));
        }

        return this.data.get(label);
    }

    protected abstract ChartItem createNewItem(String label);

    private void reduce() {
        if (this.data.size() > this.maxItems) {
            for (final String label : this.data.keySet()) {
                if (this.data.size() <= this.maxItems) {
                    return;
                }
                this.data.remove(label);
            }
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OUTPUT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String prepareData() {
        return JSonSerializer.serialize(new LinkedList(this.data.values()));
    }

    protected String prepareOptions() {
        return JSonSerializer.serialize(this.options);
    }

    public final Chart refresh() {
        // For debug purpose only
        this.element.setAttribute("title", JSonSerializer.serialize(prepareOptions()));

        this.flotObject = this.updateFlot(this.element, prepareData(), prepareOptions());
        return this;
    }

    @Override
    protected final Element makeElement() {
        // Add a placeholder
        return XML.makeElement(HTML.div(new HTMLClass("chart")));
    }

    @Override
    protected final void onLoad() {
        this.flotObject = initFlot(this.element, prepareData(), prepareOptions());
        super.onLoad();
    }

    private native JavaScriptObject initFlot(final Element placeholder, final String data, String options)
    /*-{
         options = $wnd.$.extend(true, eval('(' + options + ')'), {
            series:{
                pie:{
                    label:{
                        formatter: function(label, series){
                            return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">' + label + '</div>';}
                        }
                }
            }
         });
      
        return $wnd.$.plot($wnd.$(placeholder), eval('(' + data + ')'), options);
    }-*/;

    private native JavaScriptObject updateFlot(JavaScriptObject flotObject, String data)
    /*-{
           flotObject.setData(eval('(' + data + ')'));
           flotObject.draw();
           
           return flotObject;
    }-*/;

    private native JavaScriptObject updateFlot(final Element placeholder, String data, String options)
    /*-{
      
        options = eval('(' + options + ')');
       
        // Set the pie label design
        options = $wnd.$.extend(
            true,
            {    
                xaxis:{tickFormatter:undefined},
                yaxis:{tickFormatter:undefined}
            },
            options,
            {    
                 series:{
                     pie:{
                         label:{
                             formatter: function(label, series){
                                 return '<div style="font-size:8pt;text-align:center;padding:2px;color:white">' + label + '</div>';
                             }
                         }
                     }
                 }
             }
         );
         

         // Set the axis formatters
         if (options.xaxis.tickFormatter) {
             if (options.xaxis.tickFormatter == "ROUND") {
                 options.xaxis.tickFormatter = function(val, axis) {
                     return Math.round(val);
                 };
             } else if (options.xaxis.tickFormatter.substring(0,5) == "WRAP{") {
                 options.xaxis['tickFormat'] = eval('(' + options.xaxis.tickFormatter.substring(4) + ')');
                 options.xaxis.tickFormatter = function(val, axis) {
                     return options.xaxis.tickFormat.before + val + options.xaxis.tickFormat.after;
                 };
             } else if (options.xaxis.tickFormatter != undefined){
                     options.xaxis['tickFormat'] = options.xaxis.tickFormatter; 
                     options.xaxis.tickFormatter = function(val, axis) {
                         return $wnd.dateFormat(new Date(val), options.xaxis.tickFormat);
                     };
             }
         }
         if (options.yaxis.tickFormatter) {
             if (options.yaxis.tickFormatter == "ROUND") {
                 options.yaxis.tickFormatter = function(val, axis) {
                     return Math.round(val);
                 };
             } else if (options.yaxis.tickFormatter.substring(0,5) == "WRAP{") {
                 options.yaxis['tickFormat'] = eval('(' + options.yaxis.tickFormatter.substring(4) + ')');
                 options.yaxis.tickFormatter = function(val, axis) {
                     return options.yaxis.tickFormat.before + val + options.yaxis.tickFormat.after;
                 };
             } else if (options.yaxis.tickFormatter == undefined){
                     options.yaxis['tickFormat'] = options.yaxis.tickFormatter; 
                     options.yaxis.tickFormatter = function(val, axis) {
                         return $wnd.dateFormat(new Date(val), options.yaxis.tickFormat);
                     };
             }
         }


         return $wnd.$.plot($wnd.$(placeholder), eval('(' + data + ')'), options);
    }-*/;

}
