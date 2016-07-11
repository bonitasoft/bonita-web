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

    public FormLogger(String className) {
        LOGGER = Logger.getLogger(className);
    }

    @Override
    public void log(Level level, String message, Map<String, Object> context) {
        this.log(level, message, null, context);
    }

    @Override
    public void log(Level level, String message, Throwable e, Map<String, Object> context) {
        if (!isLoggable(level)) {
            return;
        }
        FormContextUtil formContextUtil = new FormContextUtil(context);
        String prefixMessage = "";
        if (formContextUtil.getSession() != null && formContextUtil.getUserName() != null) {
            prefixMessage += "Username<" + formContextUtil.getUserName() + "> ";
        }
        if (formContextUtil.getFormName() != null) {
            prefixMessage += "Form<" + formContextUtil.getFormName() + "> ";
        }
        HashMap<String, FormFieldValue> submittedFields = formContextUtil.getSubmittedFields();
        if (!submittedFields.isEmpty()) {
            prefixMessage += "Submitted Fields<";
            String fieldStringRepresentation = getFormFieldStringRepresentation("", submittedFields);
            prefixMessage += fieldStringRepresentation + "> ";
        }
        try {
            // in case of process instantiation
            if (formContextUtil.getProcessDefinitionId() != null) {
                prefixMessage += "Process<" + formContextUtil.getProcessName();
                if (formContextUtil.getProcessVersion() != null) {
                    prefixMessage += " " + formContextUtil.getProcessVersion();
                }
                prefixMessage += "> ";
            } else if (formContextUtil.getTaskId() != null) {
                prefixMessage += "Task<" + formContextUtil.getTaskName() + "> ";
            }

        } catch (ProcessDefinitionNotFoundException e1) {
            prefixMessage += "Process<" + formContextUtil.getProcessDefinitionId() + "> ";
        } catch (BPMEngineException e1) {
            prefixMessage += "Process<" + formContextUtil.getProcessDefinitionId() + "> ";
        }
        if (!prefixMessage.isEmpty()) {
            if (message == null) {
                message = prefixMessage;
            } else {
                message = prefixMessage + " " + message;
            }
        }
        internalLog(level, message, e);
    }

    protected void internalLog(Level level, String message, Throwable e) {
        LOGGER.log(level, message, e);
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
}
