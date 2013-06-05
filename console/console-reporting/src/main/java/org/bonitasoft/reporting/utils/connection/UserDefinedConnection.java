package org.bonitasoft.reporting.utils.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.bonitasoft.reporting.exception.BonitaReportException;

public class UserDefinedConnection {

    private static final String USER_REPORT_CONNECTION_FILE = "connection.properties";

    private static final String USER_REPORT_CONNECTION_PROPERTY_NAME_URL = "dbUrl";

    private static final String USER_REPORT_CONNECTION_PROPERTY_NAME_DRIVER = "dbDriverClassName";

    private static final String USER_REPORT_CONNECTION_PROPERTY_NAME_USER = "dbUser";

    private static final String USER_REPORT_CONNECTION_PROPERTY_NAME_PASSWD = "dbPassword";

    private String systemReportPath;

    public UserDefinedConnection() {
        super();
    }

    public UserDefinedConnection(final String systemReportPath) {
        this();

        this.systemReportPath = systemReportPath;
    }

    @SuppressWarnings("finally")
    public Connection getUserConnection()
            throws BonitaReportException
    {

        final Properties connectionProps = loadUserConnectionProperties(systemReportPath);

        final String dbUrl = connectionProps.getProperty(USER_REPORT_CONNECTION_PROPERTY_NAME_URL);
        final String dbDriverClassName = connectionProps.getProperty(USER_REPORT_CONNECTION_PROPERTY_NAME_DRIVER);
        final String dbUser = connectionProps.getProperty(USER_REPORT_CONNECTION_PROPERTY_NAME_USER);
        final String dbPassword = connectionProps.getProperty(USER_REPORT_CONNECTION_PROPERTY_NAME_PASSWD);

        Connection conn = null;

        try {
            Class.forName(dbDriverClassName);
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            return conn;

        } catch (final ClassNotFoundException e) {
            throw new BonitaReportException("The driver for the user defined connection can not be found", e);
        } catch (final SQLException e) {
            throw new BonitaReportException("The user defined connection can not be created", e);
        }

    }

    private Properties loadUserConnectionProperties(final String systemReportPath)
            throws BonitaReportException
    {

        final File propsFile = new File(systemReportPath + System.getProperty("file.separator") + USER_REPORT_CONNECTION_FILE);
        if (propsFile.exists()) {

            try {
                final Properties props = new Properties();
                final FileInputStream fis = new FileInputStream(propsFile);
                props.load(fis);
                fis.close();

                return props;

            } catch (final IOException e) {
                throw new BonitaReportException(systemReportPath + USER_REPORT_CONNECTION_FILE + " is a corrupt file", e);
            }
        }

        return null;
    }

    // getter & setters
    public String getSystemReportPath() {
        return systemReportPath;
    }

    public void setSystemReportPath(final String systemReportPath) {
        this.systemReportPath = systemReportPath;
    }

}
