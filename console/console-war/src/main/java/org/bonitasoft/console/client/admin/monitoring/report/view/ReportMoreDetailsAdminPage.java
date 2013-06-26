package org.bonitasoft.console.client.admin.monitoring.report.view;

/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Date;

import org.bonitasoft.forms.client.view.common.URLUtils;
import org.bonitasoft.web.rest.model.monitoring.report.ReportItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.url.UrlOption;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Html;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

/**
 * @author Bastien Rohart
 * 
 */
public class ReportMoreDetailsAdminPage extends ReportQuickDetailsAdminPage {

    public static final String TOKEN = "reportmoredetails";

    public static final String URL_API = "runreport";

    private static final long ONE_WEEK = 604800;

    private ReportItem reportItem;

    public ReportMoreDetailsAdminPage() {
        addClass(CssClass.MORE_DETAILS);
    }

    public ReportMoreDetailsAdminPage(ReportItem reportItem) {
        this.reportItem = reportItem;
        addParameter(PARAMETER_ITEM_ID, reportItem.getId().toString());
    }

    @Override
    protected void buildToolbar(ReportItem report) {
    }

    @Override
    protected void defineTitle(ReportItem item) {
        setTitle("");
    }

    @Override
    protected void buildMetadatas(ReportItem item) {
    }

    private Button newBackButton() {
        return new Button(new JsId("back"), _("Back"),
                _("Go back to previous view"), new HistoryBackAction());
    }

    @Override
    public String defineToken() {
        return TOKEN;

    }

    @Override
    protected void buildBody(final ReportItem item) {
        String locale = AbstractI18n.getDefaultLocale().toString();
        final Container<Html> container = new Container<Html>();
        container.append(getLoader());
        addBody(container);

        try {
            RequestBuilder theRequestBuilder;
            // TODO modify servlet for it take id of the report
            final String url = buildReportFormURL(item, locale);
            theRequestBuilder = new RequestBuilder(RequestBuilder.GET, url);
            theRequestBuilder.setUser(Session.getUserId().toString());

            theRequestBuilder.setCallback(new RequestCallback() {

                @Override
                public void onError(Request aRequest, Throwable anException) {
                    // s.setHtml("onError" + anException.toString()
                    // + "<BR/>");
                }

                @Override
                public void onResponseReceived(Request aRequest,
                        Response aResponse) {
                    if (aResponse.getStatusCode() == Response.SC_OK) {
                        String response = aResponse.getText();
                        GWT.log(response);
                        response = response.substring(response.indexOf("<div class=\"report\">"), response.lastIndexOf("</div>"));
                        response = response.replaceAll("<img(.*)src=\"([^ ]*)\"(.*)([>|/>])", "<img$1src=\"$2&r=" + (Math.random()) + "\"$3$4");
                        container.empty();
                        container.append(new Html(response));

                        try {
                            removeReportStyle();
                            String localeDateFormat = _("mm/dd/yy");
                            reportDateRangePicker(localeDateFormat, "p_date_");
                            hookReportFormSubmition(localeDateFormat, "p_date_");
                            retrieveFieldsValues(getParams(url), localeDateFormat);
                            addToolbarLink(newBackButton());
                        } catch (Exception e) {
                            addToolbarLink(newBackButton());
                        }
                    } else {
                        addToolbarLink(newBackButton());
                        // html.setHtml("Failed<BR/>");
                    }
                }
            });

            theRequestBuilder.send();

        } catch (RequestException e) {
        }
    }

    /**
     * @return
     */
    private Html getLoader() {
        return new Html("<div id=\"initloader\">" +
                "<div class=\"loader\">" +
                "<img src=\"images/loader.gif\" />" +
                "</div>" +
                "</div>");
    }

    private String buildReportFormURL(final ReportItem item, String locale) {
        final StringBuilder frameURL = new StringBuilder()
                .append(GWT.getModuleBaseURL()).append("runreport")
                .append("?reportName=" + item.getName())
                .append("&locale=" + locale)
                .append("&reportFormat=HTML")
                .append("&p_date_from=" + oneWeekBefore())
                .append("&p_date_to=" + thisDay())
                .append("&_pf=" + URLUtils.getInstance().getHashParameter(UrlOption.PROFILE));
        return frameURL.toString();
    }

    private String getParams(String url) {
        return url.substring(url.lastIndexOf("?") + 1);
    }

    private native void reportDateRangePicker(String localeDateFormat, String prefix)
    /*-{
        $wnd.reportDateRangePicker(localeDateFormat, prefix);
    }-*/;

    private native void hookReportFormSubmition(String localeDateFormat, String prefix)
    /*-{
        $wnd.hookReportFormSubmition(localeDateFormat, prefix);
    }-*/;

    private native void retrieveFieldsValues(String params, String localeDateFormat)
    /*-{
        $wnd.retrieveFieldsValues(params, localeDateFormat);
    }-*/;

    private native void removeReportStyle()
    /*-{
        $wnd.removeReportStyle();
    }-*/;

    @SuppressWarnings(value = { "deprecation" })
    private Long oneWeekBefore() {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        long epoch = date.getTime() - ONE_WEEK * 1000;// TODO manage daylight saving time
        return epoch;
    }

    @SuppressWarnings(value = { "deprecation" })
    private Long thisDay() {
        Date date = new Date();
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date.getTime();
    }

}
