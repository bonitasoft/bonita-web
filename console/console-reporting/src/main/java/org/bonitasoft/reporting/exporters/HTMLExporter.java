package org.bonitasoft.reporting.exporters;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import org.bonitasoft.jasperreports.engine.export.BonitaHtmlExporter;

public class HTMLExporter implements IExporter {

    private boolean isBonitaHtmlExporter;

    @Override
    public String getType() {
        return "HTML";
    }

    @Override
    public String getMineType() {
        return "text/html";
    }

    @Override
    public JRExporter getJrExport() {

        JRHtmlExporter exporter = new JRHtmlExporter();

        // use the Bonita Html Exporter only if needed
        if (isBonitaHtmlExporter) {
            exporter = new BonitaHtmlExporter();
        }

        exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.TRUE);
        exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "<div class=\"report\">");
        exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "</div>");

        return exporter;
    }

    public boolean isBonitaHtmlExporter() {
        return isBonitaHtmlExporter;
    }

    public void setBonitaHtmlExporter(final boolean isBonitaHtmlExporter) {
        this.isBonitaHtmlExporter = isBonitaHtmlExporter;
    }
}
