package org.bonitasoft.reporting.exporters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.bonitasoft.reporting.exception.BonitaReportException;
import org.bonitasoft.reporting.utils.tools.ReportHtmlFormat;

public class Exporter {

    public static void exportReport(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final ServletContext context,
            final String reportName,
            final String reportFormat,
            final JasperReport report,
            final JasperPrint jasperPrint)
            throws BonitaReportException
    {

        try {

            IExporter exporter = new HTMLExporter();
            JRExporter jrExporter;

            if ("HTML".equals(reportFormat)) {

                exporter = new HTMLExporter();
                ((HTMLExporter) exporter).setBonitaHtmlExporter(ReportHtmlFormat.isBonitaHtmlEngine(report));

                jrExporter = exporter.getJrExport();

                response.setCharacterEncoding("UTF-8");

                response.setContentType(exporter.getMineType());

                final PrintWriter out = response.getWriter();

                request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
                jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                jrExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, context.getContextPath() + "/console/images?image=");
                jrExporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);

                jrExporter.exportReport();

            } else if ("PDF".equals(reportFormat)) {

                exporter = new PDFExporter();
                response.setHeader("Content-Disposition", " inline; filename=" + reportName + ".pdf");

                final FileBufferedOutputStream fbos = new FileBufferedOutputStream();

                jrExporter = exporter.getJrExport();
                request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
                jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);

                jrExporter.exportReport();

                fbos.close();

                if (fbos.size() > 0)
                {
                    response.setContentType("application/force-download");
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".pdf");// fileName);

                    response.setContentLength(fbos.size());
                    final ServletOutputStream ouputStream = response.getOutputStream();

                    try
                    {
                        fbos.writeData(ouputStream);
                        fbos.dispose();
                        ouputStream.flush();
                    } finally
                    {
                        if (ouputStream != null)
                        {
                            try
                            {
                                ouputStream.close();
                            } catch (final IOException e)
                            {
                                throw new BonitaReportException("Error during report export (jasperreports)", e);
                            }
                        }
                    }
                }
            }

        } catch (final JRException e) {
            throw new BonitaReportException("Error during report export (jasperreports)", e);
        } catch (final IOException e) {
            throw new BonitaReportException("Error during report export (io)", e);
        }
    }

}
