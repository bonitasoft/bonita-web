package org.bonitasoft.reporting.exporters;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;

public class PDFExporter implements IExporter{

	@Override
	public String getType() {
		return "PDF";
	}
	
	@Override
	public String getMineType() {
		return "application/pdf";
	}

	@Override
	public JRExporter getJrExport() {
		JRPdfExporter exporter = new JRPdfExporter();
		
		return exporter;
	}
}
