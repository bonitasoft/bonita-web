package org.bonitasoft.reporting.utils.tools;

import net.sf.jasperreports.engine.JasperReport;

public class ReportHtmlFormat {
	
	public final static String BONITA_REPORT_HTML_ENGINE_PROP = "BONITA_HTML_ENGINE";
	public final static String BONITA_REPORT_HTML_ENGINE_VALUE_TRUE = "true";
	
	public final static boolean isBonitaHtmlEngine(JasperReport report){
		
		String property = report.getProperty(ReportHtmlFormat.BONITA_REPORT_HTML_ENGINE_PROP);
		
		if(ReportHtmlFormat.BONITA_REPORT_HTML_ENGINE_VALUE_TRUE.equalsIgnoreCase(property)){
			return true;
		}
		
		return false;
	}

}
