package org.bonitasoft.reporting.form.components;

import java.util.HashMap;

import org.bonitasoft.reporting.utils.BonitaReportEngineContext;

public class FormComponent {

    private String name;

    private String action;

    private String reportName;

    private HashMap<String, Object> paramters;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(final String reportName) {
        this.reportName = reportName;
    }

    public HashMap<String, Object> getParamters() {
        return paramters;
    }

    public void setParamters(final HashMap<String, Object> paramters) {
        this.paramters = paramters;
    }

    public String getBodyBeginHtml() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<div class='report-form' id='report-form-ctn'>");
        sb.append("<form id='report-form' class='form' name='reportForm' action='" + getAction() + "' >");
        sb.append("<div id='report-form-entries' class='formentries'>");
        sb.append("	<input type='hidden' name='" + BonitaReportEngineContext.BONITA_REPORT_SERVLET_PARAM_REPORT_NAME + "' value='" + getReportName()
                + "'></input>");

        String v;
        for (final String k : getParamters().keySet()) {
            v = getParamters().get(k).toString();
            sb.append("	<input type='hidden' name='").append(k).append("' value='").append(v).append("'></input>");
        }

        return sb.toString();

    }

    public String getBodyEndHtml() {
        final StringBuffer sb = new StringBuffer();
        sb.append("</div>");
        sb.append("</form>");
        sb.append("</div>");

        return sb.toString();
    }

}
