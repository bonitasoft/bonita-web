package org.bonitasoft.reporting.utils;

import java.io.File;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.reporting.exception.BonitaReportException;

public class ReportDataStore {

    private final static Logger LOGGER = Logger.getLogger(ReportDataStore.class.getName());

    /*
     * Get the report from the database
     * and create the report on the disk
     */
    public static void cacheReport(final String reportName) {

        // read the database record in witch the report is

        // test if the report already exist on the disk

        // compare file report date and record date

        // if record date is newer than file report date
        // or file report does not exists
        // uncompress archive
        // compile report

        // otherwise do nothing

    }

    /*
     * return the system path where the report is deployed
     */
    public static String getReportPath(final long tenantId, final String reportName)
            throws BonitaReportException
    {
        final String reportPath = WebBonitaConstantsUtils.getInstance(tenantId).getReportFolder().getPath();

        // ReportDataStore.SYSTEM_REPORT_PATH ="/home/ccharly/Dev/servers/apache-tomcat-6.0.29/webapps/BonitaSoftWebJasperReports/reports";
        return reportPath + File.separator + reportName;
    }

    /*
     * check if the report already in cache on the file system
     */
    public static boolean isExists(final String reportName) {

        // TODO to be implemented
        return true;
    }

}
