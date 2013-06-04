package org.bonitasoft.reporting.utils.widget;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JasperReport;

public class Parser {
	
	
	//ALL Widget
	public final static String BONITA_JR_PROPERTY_FORM_PREFIX = "BONITA_FORM_";
	public final static String BONITA_JR_PROPERTY_FORM_ID = "_ID";
	public final static String BONITA_JR_PROPERTY_FORM_WIDGET = "_WIDGET";
	public final static String BONITA_JR_PROPERTY_FORM_LABEL = "_LABEL";
	public final static String BONITA_JR_PROPERTY_FORM_QUERY = "_QUERY";
	public final static String BONITA_JR_PROPERTY_FORM_AVAILABLE_VALUES = "_AVAILABLE_VALUES";
	public final static String BONITA_JR_PROPERTY_FORM_INITIAL_VALUE = "_INITIAL_VALUE";
	public final static String BONITA_JR_PROPERTY_FORM_HAS_ALL = "_HAS_ALL";
	public final static String BONITA_JR_PROPERTY_FORM_HAS_ALL_VALUE = "_HAS_ALL_VALUE";
	
	
	public final static String BONITA_JR_PROPERTY_FORM_WIDGET_TYPE_SELECT = "SELECT";
	public final static String BONITA_JR_PROPERTY_FORM_WIDGET_TYPE_DATE = "DATE";
	public final static String BONITA_JR_PROPERTY_FORM_WIDGET_TYPE_DATE_RANGE = "DATE_RANGE";

	public final static String BONITA_JR_PROPERTY_WIDGET_HAS_ALL_TRUE = "TRUE";
	public final static String BONITA_JR_PROPERTY_WIDGET_HAS_ALL_FALSE = "FALSE";
	
	private static ArrayList<Widget> getWidgetsFromProperties(JasperReport report){
		
		// TODO test what is mandatory !! and throw exceptions
		
		
		
		
		ArrayList<Widget> widgetsForm = new ArrayList<Widget>();
		
		int indice = 1;
		String property = report.getProperty(BONITA_JR_PROPERTY_FORM_PREFIX + indice + BONITA_JR_PROPERTY_FORM_ID);
		
		Widget wdgt;
		
		while(property != null){
			
			wdgt = new Widget();
			
			// WIDGET Id
			wdgt.setId(property);
			
			// WIDGET type
			property = report.getProperty(BONITA_JR_PROPERTY_FORM_PREFIX + indice + BONITA_JR_PROPERTY_FORM_WIDGET);
			wdgt.setWidget(property);
			
			// WIDGET LABEL
			property = report.getProperty(BONITA_JR_PROPERTY_FORM_PREFIX + indice + BONITA_JR_PROPERTY_FORM_LABEL);
			wdgt.setLabel(property);
			
			// QUERY
			property = report.getProperty(BONITA_JR_PROPERTY_FORM_PREFIX + indice + BONITA_JR_PROPERTY_FORM_QUERY);
			wdgt.setQuery(property);
			
			// AVAILABLE_VALUES
			property = report.getProperty(BONITA_JR_PROPERTY_FORM_PREFIX + indice + BONITA_JR_PROPERTY_FORM_AVAILABLE_VALUES);
			wdgt.setAvailableValues(property);
			
			// INITIAL_VALUE
			property = report.getProperty(BONITA_JR_PROPERTY_FORM_PREFIX + indice + BONITA_JR_PROPERTY_FORM_INITIAL_VALUE);
			wdgt.setInitialValue(property);
			
			// HAS_ALL
			property = report.getProperty(BONITA_JR_PROPERTY_FORM_PREFIX + indice + BONITA_JR_PROPERTY_FORM_HAS_ALL);
			wdgt.setHasAll(property);
			
			// HAS_ALL_VALUE
			property = report.getProperty(BONITA_JR_PROPERTY_FORM_PREFIX + indice + BONITA_JR_PROPERTY_FORM_HAS_ALL_VALUE);
			wdgt.setHasAllValue(property);
			
			widgetsForm.add(wdgt);
			
			// Verify if there's other widget
			indice++;
			property = report.getProperty(BONITA_JR_PROPERTY_FORM_PREFIX + indice + BONITA_JR_PROPERTY_FORM_ID);
		}
		
		return widgetsForm;
		
	}
	
	public static ArrayList<Widget> getWidgets(JasperReport report){
		
		return Parser.getWidgetsFromProperties(report);
	}
	
}
