package org.bonitasoft.reporting.utils;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.io.IOException;
import java.util.Properties;

import org.bonitasoft.reporting.exception.BonitaReportException;

public class BonitaReportEngineContext {

    private Properties ctx;

    public final static String BONITA_REPORT_SERVLET_NAME = "runreport";

    public final static String BONITA_REPORT_SERVLET_URL = "/console/" + BONITA_REPORT_SERVLET_NAME;

    /*
     * mame of a url parameter to get the name of the report to compute
     */
    public final static String BONITA_REPORT_SERVLET_PARAM_REPORT_NAME = "reportName";

    /*
     * mame of a url parameter to define the type of format expected
     * -> HTML
     * -> PDF
     */
    public final static String BONITA_REPORT_SERVLET_PARAM_REPORT_FORMAT = "reportFormat";

    /*
     * mame of a url parameter to define the define if it's needed to generate all the
     * HTML or not
     */
    public final static String BONITA_REPORT_SERVLET_PARAM_REPORT_HTML_PARTS = "htmlParts";

    public final static String BONITA_REPORT_SERVLET_REPORT_FORMAT_PDF = "PDF";

    public final static String BONITA_REPORT_SERVLET_REPORT_FORMAT_HTML = "HTML";

    private final static int BONITA_REPORT_HTML_MAX_REPORT_COUNT = 100;

    public Properties getContextProperties()
            throws BonitaReportException {
        try {

            if (ctx == null) {
                ctx = new Properties();
            }
            ctx.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("bonitareports.properties"));

            return ctx;

        } catch (final IOException e) {
            throw new BonitaReportException("Could not load bonitareports.properties from the classpath", e);
        } catch (final Exception e) {
            throw new BonitaReportException("Could not load bonitareports.properties from the classpath", e);
        }
    }

    public final Integer getHtmlMaxReportCount() throws BonitaReportException {

        final String mrcnt = getContextProperties().getProperty("bonitareport.html_max_report_count");

        if (mrcnt == null) {
            return new Integer(BonitaReportEngineContext.BONITA_REPORT_HTML_MAX_REPORT_COUNT);
        } else {
            return new Integer(mrcnt);
        }
    }

    public final String getDateFormat() throws BonitaReportException {
        return _("mm/dd/yy");
    }

    public final String getDateJsFormat() throws BonitaReportException {
        return _("mm/dd/yy");
    }

    public final String getTimeFormat() throws BonitaReportException {
        final String timeFormat = getContextProperties().getProperty("bonitareport.date.timeFormat");
        return timeFormat;
    }

    public final String getTimeZoneId() throws BonitaReportException {
        final String timezoneId = getContextProperties().getProperty("bonitareport.timezone.id");
        return timezoneId;
    }

}
