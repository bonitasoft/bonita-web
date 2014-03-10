package org.bonitasoft.forms.server.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.forms.client.model.FormFieldValue;

public class FormLogger implements IFormLogger {

    protected Logger LOGGER;

    protected static Map<String, Object> context = new HashMap<String, Object>();

    public FormLogger(String className) {
        LOGGER = Logger.getLogger(className);;
    }

    @Override
    public void log(Level level, String message) {
        logWithoutContext(level, message, null);
    }

    @Override
    public void log(Level level, String message, Throwable e, Map<String, Object> pcontext) {
        setContext(pcontext);
        this.log(level, message, e);
    }

    @Override
    public void log(Level level, String message, Map<String, Object> pcontext) {
        setContext(pcontext);
        this.log(level, message);

    }

    private void logWithoutContext(Level level, String message, Throwable e) {
        this.log(level, message, e);
    }

    @Override
    public void log(Level level, String message, Throwable e) {
        if (e == null)
            e = new Exception();

        FormContextUtil ctxu = new FormContextUtil(context);
        String prefixMessage = "";

        if (ctxu.getSession() != null && ctxu.getUserName() != null) {
            prefixMessage += "Username<" + ctxu.getUserName() + "> ";
        }

        if (ctxu.getFormName() != null) {
            prefixMessage += "Form<" + ctxu.getFormName() + "> ";
        }

        HashMap<String, FormFieldValue> submittedFields = ctxu.getSubmittedFields();
        if (!submittedFields.isEmpty()) {
            prefixMessage += "Submitted Fields<";
            String fieldStringRepresentation = getFormFieldStringRepresentation("", submittedFields);
            prefixMessage += fieldStringRepresentation + "> ";
        }

        try {
            // in case of process instanciation
            if (ctxu.getProcessDefinitionId() != null) {
                prefixMessage += "Process<" + ctxu.getProcessName();
                if (ctxu.getProcessVersion() != null) {
                    prefixMessage += " " + ctxu.getProcessVersion();
                }
                prefixMessage += "> ";
            } else if (ctxu.getTaskId() != null) {
                prefixMessage += "Task<" + ctxu.getTaskName() + "> ";
            }

        } catch (ProcessDefinitionNotFoundException e1) {
            prefixMessage += "Process definition not found " + e1.getMessage();
        } catch (BPMEngineException e1) {
            prefixMessage += e1.getMessage();
        }
        if (message == null) {
            message = "";
        }
        message = prefixMessage;

        if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, message, e);
        } else if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, message, e);
        } else if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, message, e);
        } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, message, e);
        }
    }

    protected String getFormFieldStringRepresentation(String returnedStr, Map<String, FormFieldValue> submittedFields) {
        int i = 0;
        for (Map.Entry<String, FormFieldValue> entry : submittedFields.entrySet()) {
            i = i + 1;
            FormFieldValue fieldValue = entry.getValue();
            String fieldName = entry.getKey();
            if (!entry.getValue().hasChildWidgets()) {
                returnedStr += formatLogField(fieldName, fieldValue);
            }
            if (submittedFields.size() > 1) {
                returnedStr += " ; ";
            }
        }
        return returnedStr;
    }

    protected String formatLogField(String fieldName, FormFieldValue fieldValue) {
        return fieldName + " (" + fieldValue.getValueType() + ")" + " => " + fieldValue.getValue();
    }

    @Override
    public boolean isLoggable(Level level) {
        return LOGGER.isLoggable(level);
    }

    public static void setContext(Map<String, Object> pcontext) {
        context = pcontext;
    }

}
