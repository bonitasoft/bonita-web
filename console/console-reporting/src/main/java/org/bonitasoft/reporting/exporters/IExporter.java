package org.bonitasoft.reporting.exporters;

import net.sf.jasperreports.engine.JRExporter;

public interface IExporter {

	public String getType();
	public String getMineType();
	public JRExporter getJrExport();	
	
}
