package org.bonitasoft.forms.server.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.server.accessor.api.EngineClientFactory;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;

public class FormContextUtil {

    private final Map<String, Object> context = new HashMap<String, Object>();

    private Map<String, Object> urlContext = new HashMap<String, Object>();

    private final APISession session;

    private ProcessDeploymentInfo processInfo = null;

    public FormContextUtil(Map<String, Object> context) {
        this.context.putAll(context);
        session = getAPISessionFromContext();
        if (context.get(FormServiceProviderUtil.URL_CONTEXT) != null) {
            urlContext.putAll(getUrlContext(context));
        }
    }

    public APISession getSession() {
        return session;
    }

    public Map<String, Object> getUrlContext() {
        return urlContext;
    }

    public Locale getLocale() {
        return (Locale) context.get(FormServiceProviderUtil.LOCALE);
    }

    public Long getUserId() {
        Long userID;
        if (urlContext.containsKey(FormServiceProviderUtil.USER) && urlContext.get(FormServiceProviderUtil.USER) != null) {
            userID = Long.valueOf(urlContext.get(FormServiceProviderUtil.USER).toString());
        } else {
            userID = session.getUserId();
        }
        return userID;
    }

    public String getUserName() {
        if (session != null && session.getUserName() != null) {
            return session.getUserName();
        } else {
            return "";
        }
    }

    public String getFormName() {
        return (String) urlContext.get(FormServiceProviderUtil.FORM_ID);
    }

    public String getProcessName() throws ProcessDefinitionNotFoundException, BPMEngineException {
        return getProcess().getName();
    }

    public String getProcessVersion() throws ProcessDefinitionNotFoundException, BPMEngineException {
        return getProcess().getVersion();
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, FormFieldValue> getSubmittedFields() {
        HashMap<String, FormFieldValue> submittedFields = new HashMap<String, FormFieldValue>();
        if (context.containsKey(FormServiceProviderUtil.FIELD_VALUES)) {
            submittedFields = (HashMap<String, FormFieldValue>) context.get(FormServiceProviderUtil.FIELD_VALUES);
        }
        return submittedFields;

    }

    private ProcessDeploymentInfo getProcess() throws BPMEngineException, ProcessDefinitionNotFoundException {
        if (processInfo == null) {
            if (getProcessDefinitionId() != null) {
                ProcessAPI engineClient;
                engineClient = new EngineClientFactory().getProcessAPI(session);
                processInfo = engineClient.getProcessDeploymentInfo(getProcessDefinitionId());

            }
        }
        return processInfo;
    }

    public Long getProcessDefinitionId() {
        if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
            return Long.valueOf(urlContext.get(FormServiceProviderUtil.PROCESS_UUID).toString());
        }
        return null;
    }

    /**
     * Retrieve the API session from the context
     * 
     * @param context
     *            the map of context
     * @return the engine API session
     */
    public APISession getAPISessionFromContext() {
        APISession session = (APISession) context.get(FormServiceProviderUtil.API_SESSION);
        if (session == null) {
            final String errorMessage = "There is no engine API session in the HTTP session.";
            if (Logger.getLogger(FormContextUtil.class.getName()).isLoggable(Level.SEVERE)) {
                Logger.getLogger(FormContextUtil.class.getName()).log(Level.SEVERE, errorMessage);
            }
        }
        return session;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getUrlContext(final Map<String, Object> context) {
        return (Map<String, Object>) context.get(FormServiceProviderUtil.URL_CONTEXT);
    }

    public void setUrlContext(final Map<String, Object> pUrlContext) {
        urlContext = pUrlContext;
    }

    public Long getTaskId() {
        if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
            return Long.valueOf(urlContext.get(FormServiceProviderUtil.TASK_UUID).toString());
        }
        return null;
    }

    public Long getProcessInstanceId() {
        if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
            return Long.valueOf(urlContext.get(FormServiceProviderUtil.INSTANCE_UUID).toString());
        }
        return null;
    }

    public String getTaskName() {
        String formName = getFormName();
        if (formName != null) {
            int formIdDelimiterPos = formName.lastIndexOf(FormServiceProviderUtil.FORM_ID_SEPARATOR);
            String taskDelimiter = "--";
            int taskDelimiterPos = formName.lastIndexOf(taskDelimiter) + taskDelimiter.length();
            if (formName != null && formIdDelimiterPos != -1 && taskDelimiterPos != -1) {
                return formName.substring(taskDelimiterPos, formIdDelimiterPos);
            }
        }
        return null;
    }
}
