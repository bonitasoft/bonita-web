package org.bonitasoft.report.chart.customizer;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.awt.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.util.JRColorUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;


public class BonitaBarChartCustomizer implements JRChartCustomizer {
    
    private static Log log = LogFactory.getLog(BonitaBarChartCustomizer.class);
    Random numGen = new Random();

    /**
     *
     */
    public BonitaBarChartCustomizer() {
        // TODO Auto-generated constructor stub
    }

    private Color newUniqueColor() {
        return new Color(numGen.nextInt(256), numGen.nextInt(256), numGen.nextInt(256));
    }

    @Override
    public void customize(JFreeChart chart, JRChart jasperChart) {
        //initialize String to be splitted to prevent NPE
        String predefinedColors = "empty:color";
        JRPropertiesMap pm = jasperChart.getPropertiesMap();
        if (pm != null && pm.getProperty("PredefinedColors")!=null) {
            predefinedColors = pm.getProperty("PredefinedColors");
        }

        if( chart == null){
        	return;
        }
        
        CategoryPlot ctgPlot = (CategoryPlot) chart.getCategoryPlot();
        if(ctgPlot == null){
        	return;
        }
        
        // to avoid border around the chart
        //ctgPlot.setOutlineVisible(false);
        ctgPlot.setBackgroundPaint(Color.white);
        
        // set only int value on axis
		NumberAxis  rangeAxis = (NumberAxis) ctgPlot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits() );
		
		// No border on legend
		LegendTitle lgd = chart.getLegend();
		if(lgd != null){
			lgd.setBorder(0.0, 0.0, 0.0, 0.0);
		}
        
        // This chart customizer requires that the PredefinedColors string is well formatted.
        // If it is not... we hope that things don't blow up. Hopefully we gracefully ignore badly formatted strings.
        // First split the string into an array of strings of the form "Pie Piece Key:Color"
        String[] entries = predefinedColors.split(";");
        Map<String, Color> categorySections = new HashMap<String, Color>();
        for (int i = 0; i < entries.length; i++) {
            String value = entries[i];
            if (value != null) {
                // For each value we split it into its 2 constituent parts. The first is only required to be String, so there is no risk.
                // The second part is the color. We rely on JRColorUtil to deal with any badly defined colors.
                String[] pair = entries[i].split(":");
                if (pair[0] != null && pair[1] != null) {
                    categorySections.put(pair[0], JRColorUtil.getColor(pair[1], null));
                }
            }
        }
        // BarRenderer: create bar charts from data in a category dataset
        BarRenderer renderer = (BarRenderer)ctgPlot.getRenderer();
    
        // Method required for reading the dataset.
        // Table of values that can be accessed using row and column keys.
        CategoryDataset cd = ctgPlot.getDataset();
        // Row gives the series expression
        if(cd != null){
	        int rc = cd.getRowCount();
	        for (int i = 0; i < rc; i++) {
	            String egName = cd.getRowKey(i).toString();
	
	            Color color = categorySections.get(egName);
	//            if (color == null) {
	//                color = newUniqueColor();
	//                categorySections.put(egName, color);
	//            }
	            if (color!=null){
	                renderer.setSeriesPaint(i, color);
	            }
	            
	            if (pm != null && pm.getProperty("MaximumBarWidth")!=null) {
	                renderer.setMaximumBarWidth(Double.valueOf(pm.getProperty("MaximumBarWidth")));
	            }
	        }
        }
        
        Binding binding = new Binding();
        binding.setVariable("foo", new Integer(2));
        GroovyShell shell = new GroovyShell(binding);
        
        //System.out.println(shell.evaluate("println 'Hello World!'; x = 123; return foo * 10"));
        String gvyScipt1 = pm.getProperty("Script1");
        if(gvyScipt1 != null && gvyScipt1.length() > 0){
        	shell.evaluate(gvyScipt1);
        }
        
        
        Binding binding2 = new Binding();
        binding2.setVariable("renderer",renderer);
        GroovyShell shell2 = new GroovyShell(binding2);
        String gvySciptRendrer = pm.getProperty("ScriptRenderer");
        if(gvySciptRendrer != null && gvySciptRendrer.length() > 0){
        	shell2.evaluate(gvySciptRendrer);
        }
    }
}