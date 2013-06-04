package org.bonitasoft.reporting.utils.connection;

import java.io.File;
import java.sql.Connection;

import org.bonitasoft.reporting.exception.BonitaReportException;

public class ConnectionHelper {

    private static final String USER_REPORT_CONNECTION_FILE = "connection.properties";

    public static Connection getConnection(String systemReportPath)
            throws BonitaReportException
    {

        if (!(systemReportPath.lastIndexOf(System.getProperty("file.separator")) == systemReportPath.length() - 1)) {
            systemReportPath += System.getProperty("file.separator");
        }
        final File propsFile = new File(systemReportPath + USER_REPORT_CONNECTION_FILE);
        if (propsFile.exists()) {
            return ConnectionHelper.userConnection(systemReportPath);
        } else {
            return ConnectionHelper.systemConnection();
        }

    }

    private static Connection systemConnection() {

        return new BonitaConnection();
    }

    private static Connection userConnection(final String systemReportPath)
            throws BonitaReportException
    {
        final UserDefinedConnection urc = new UserDefinedConnection(systemReportPath);
        return urc.getUserConnection();
    }

}
