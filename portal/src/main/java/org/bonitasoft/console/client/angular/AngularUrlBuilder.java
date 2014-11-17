package org.bonitasoft.console.client.angular;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

class AngularUrlBuilder {

    String url;

    public AngularUrlBuilder(final String url) {
        this.url = url;
    }

    /**
     * Parse provided query string to get parameter and append its value as url token
     * e.g
     * url: /admin/cases/list
     * queryString: ?caseId=2
     * become: /admin/cases/list/2
     *
     * @param param
     * @param queryString
     * @return
     */
    public AngularUrlBuilder appendQueryStringParameter(final String param, final String queryString) {
        if (queryString != null) {
            final MatchResult paramMatcher = RegExp.compile("(^|[&\\?#])" + param + "=([^&\\?#]*)([&\\?#]|$)").exec(queryString);
            if (paramMatcher != null && paramMatcher.getGroupCount() > 0) {
                url += "/" + paramMatcher.getGroup(2);
            }
        }
        return this;
    }

    public String build() {
        return "../portal.js/" + url;
    }
}