package org.bonitasoft.forms.server.util;

import java.util.logging.Logger;

public class FormLogger {

    private Logger logger;

    private String context;

    /**
     * 
     * Default Constructor.
     * 
     * @param classLog
     * @param context
     *            this class is userd to
     */
    public void FormLogger(Class<?> classLog) {
        logger = Logger.getLogger(classLog.getName());
    }

    public void log(String context) {

    }

    private String getUserId() {
        return "";
    }
}
