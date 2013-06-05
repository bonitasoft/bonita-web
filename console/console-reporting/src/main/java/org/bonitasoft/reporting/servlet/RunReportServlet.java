package org.bonitasoft.reporting.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.reporting.exception.BonitaReportException;
import org.bonitasoft.reporting.exporters.Exporter;
import org.bonitasoft.reporting.form.HtmlRenderer;
import org.bonitasoft.reporting.utils.BonitaReportEngineContext;
import org.bonitasoft.reporting.utils.BonitaSystem;
import org.bonitasoft.reporting.utils.ReportDataStore;
import org.bonitasoft.reporting.utils.connection.ConnectionHelper;
import org.bonitasoft.reporting.utils.tools.ReportDynamicLoader;
import org.bonitasoft.reporting.utils.widget.ConverterToComponents;
import org.bonitasoft.reporting.utils.widget.ConverterToReportParameters;
import org.bonitasoft.reporting.utils.widget.Parser;
import org.bonitasoft.reporting.utils.widget.Widget;

public class RunReportServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = -4217020871249201709L;
    
    private static final Log log = LogFactory.getLog(RunReportServlet.class);

    /**
     * the engine API session param key name
     */
    public static final String API_SESSION_PARAM_KEY = "apiSession";
    
    public static final String PROFILE_PARAM_KEY = "_pf";

    // Constante
    // REPORT_PATH

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {

        final ServletContext context = getServletConfig().getServletContext();

        try
        {

            final BonitaReportEngineContext bonitaReportEngineCtx = new BonitaReportEngineContext();
            final BonitaSystem bonitaSystem = new BonitaSystem();
            bonitaSystem.setBonitaReportCtx(bonitaReportEngineCtx);
            final HttpSession session = request.getSession();
            bonitaSystem.setAPISession((APISession) session.getAttribute(API_SESSION_PARAM_KEY));

            final String reportName = request.getParameter(BonitaReportEngineContext.BONITA_REPORT_SERVLET_PARAM_REPORT_NAME);
            final String reportFormat = request.getParameter(BonitaReportEngineContext.BONITA_REPORT_SERVLET_PARAM_REPORT_FORMAT);

            final String servletReportName = BonitaReportEngineContext.BONITA_REPORT_SERVLET_URL;

            // build the query string
            final String queryString = "?" + BonitaReportEngineContext.BONITA_REPORT_SERVLET_PARAM_REPORT_NAME + "=" + reportName + "&"
                    + BonitaReportEngineContext.BONITA_REPORT_SERVLET_PARAM_REPORT_FORMAT + "=" + reportFormat;
            // build the query string for the pdf export
            String pdfQueryString = "?" + BonitaReportEngineContext.BONITA_REPORT_SERVLET_PARAM_REPORT_NAME + "=" + reportName
                    + "&" + BonitaReportEngineContext.BONITA_REPORT_SERVLET_PARAM_REPORT_FORMAT + "="
                    + BonitaReportEngineContext.BONITA_REPORT_SERVLET_REPORT_FORMAT_PDF;

            // check if the report is alreay on the file system
            if (!ReportDataStore.isExists(reportName)) {
                // compile and cache the report
                ReportDataStore.cacheReport(reportName);

                // if the report is not present download it from the databases
                // and cache it on the file system
                // so get its parent directory
            }
            final String reportFolder = ReportDataStore.getReportPath(bonitaSystem.getCurrentTenant(), reportName);

            // configure my report engine

            // load resource Bundle in classpath (localisation / .properties files)
            final ClassLoader rcl = ReportDynamicLoader.loadMyResourceBundle(reportFolder, reportName);
            ResourceBundle rs = null;
            if (rcl != null) {
                // TODO si la locale n'existe pas prendre la default

                rs = PropertyResourceBundle.getBundle(reportName, BonitaSystem.getCurrentLocale(request), rcl);
            }

            // load in classpath need jar
            ReportDynamicLoader.updateClassLoader(
                    BonitaSystem.getLibSystemPath(context),
                    reportFolder
                    );

            // manage connection
            // test if the connection.properties is present
            // otherwise use the default Bonita connection
            // TODO perhaps the best thing is to get here a JRDataSource not only connection
            Connection conn = ConnectionHelper.getConnection(reportFolder);

            // load report
            final String reportSystemPath = reportFolder + System.getProperty("file.separator") + reportName + ".jasper";
            final File reportFile = new File(reportSystemPath);
            if (!reportFile.exists()) {
                throw new BonitaReportException("File " + reportSystemPath + " not found. The report design must be compiled first.");
            }

            JasperReport jasperReport = null;

            try {
                jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
            } catch (final JRException e) {
                throw new BonitaReportException("File " + reportSystemPath + " could not be load as a JasperReports template.", e);
            }

            if (jasperReport == null) {
                throw new BonitaReportException("the template for File " + reportSystemPath + " could not be null.");
            }

            final String actionUrlForm = BonitaSystem.getBaseUrl(request) + servletReportName;

            // create the parameters of the report
            final Map<String, Object> parameters = new HashMap<String, Object>();
            // parse report properties and build the Forms
            final ArrayList<Widget> widgets = Parser.getWidgets(jasperReport);
            pdfQueryString = ConverterToReportParameters.getReportParamValues(
                    request,
                    bonitaSystem,
                    parameters,
                    widgets,
                    pdfQueryString);

            final HashMap<String, Object> formParams = new HashMap<String, Object>();
            formParams.put("reportFormat", reportFormat);
            final HtmlRenderer htmlr = new HtmlRenderer(reportName + "Form", actionUrlForm, reportName, formParams);
            htmlr.init(ConverterToComponents.getComponents(
                    request,
                    bonitaSystem,
                    widgets,
                    conn,
                    rs)
                    );

            // parameters.put("REPORT_CLASS_LOADER",Thread.currentThread().getContextClassLoader());
            // Add all Bonita System element needed as jasperReports Parameter
            parameters.put("BONITA_API_SESSION", bonitaSystem.getAPISession());
            parameters.put("BONITA_TENANT_ID", bonitaSystem.getCurrentTenant());
            parameters.put("BONITA_BASE_URL", BonitaSystem.getBaseUrl(request));
            parameters.put("BONITA_REPORT_URL", actionUrlForm + queryString);
            parameters.put("BONITA_REPORT_FOLDER", reportFolder);
            parameters.put("BONITA_EXPORT_FORMAT", reportFormat);
            // localized date and datetime format
            parameters.put("BONITA_TIME_ZONE", bonitaSystem.getCurrentTimeZone());
            parameters.put("BONITA_LOCALE_DATE_FORMAT", bonitaSystem.getCurrentLocaleDateFormat());
            parameters.put("BONITA_LOCALE_TIME_FORMAT", bonitaSystem.getCurrentLocaleTimeFormat());
            parameters.put("BONITA_LOCALE_DATETIME_FORMAT", bonitaSystem.getCurrentLocaleDatetimeFormat());
            // url to generate PDF
            parameters.put("BONITA_EXPORT_TO_PDF", actionUrlForm + pdfQueryString);
            // user
            parameters.put("BONITA_LOGGED_USER_ID",  request.getParameter(PROFILE_PARAM_KEY));
            parameters.put("BONITA_LOGGED_USER", bonitaSystem.getCurrentUser());
            // locale
            parameters.put("REPORT_LOCALE", BonitaSystem.getCurrentLocale(request));
            // Build inputs control
            parameters.put("BONITA_HTML_FORM", htmlr.getHtmlForm());
            // maximum of lines to display in HTML
            parameters.put("BONITA_HTML_MAX_REPORT_COUNT", bonitaReportEngineCtx.getHtmlMaxReportCount());

            // RESOURCE_BUNDLE
            if (rs != null) {
                parameters.put("REPORT_RESOURCE_BUNDLE", rs);
            }

            // if html format export no pagination
            if ("HTML".equals(reportFormat)) {
                parameters.put("IS_IGNORE_PAGINATION", Boolean.TRUE);
            }

            // Fill the report
            JasperPrint jasperPrint = null;

            try {
                jasperPrint =
                        JasperFillManager.fillReport(
                                jasperReport,
                                parameters,
                                conn
                                );
            } catch (final JRException e) {
                throw new BonitaReportException("the template for File " + reportSystemPath + " could not be fill.", e);
            }

            Exporter.exportReport(request, response, context, reportName,
                    reportFormat,
                    jasperReport,
                    jasperPrint);

            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                } catch (final SQLException e) {
                    throw new BonitaReportException("could not close connection", e);
                }

            }

        } catch (final BonitaReportException e)
        {
            final PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>BonitaReports - RunReport Servlet</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
            out.println("</head>");

            out.println("<body bgcolor=\"white\">");

            out.println("<span class=\"bnew\">Bonita Report Exception encountered this error :</span>");
            out.println("<pre>");

            e.printStackTrace(out);

            out.println("</pre>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}
