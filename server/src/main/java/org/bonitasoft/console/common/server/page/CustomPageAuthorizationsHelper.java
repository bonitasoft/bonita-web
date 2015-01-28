package org.bonitasoft.console.common.server.page;

import javax.servlet.ServletException;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.business.application.ApplicationPageSearchDescriptor;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;

/**
 * @author Julien Mege
 */
public class CustomPageAuthorizationsHelper {

    private final GetUserRightsHelper getUserRightsHelper;
    private final ApplicationAPI applicationAPI;
    private final PageAPI pageApi;

    public CustomPageAuthorizationsHelper(final GetUserRightsHelper getUserRightsHelper, final ApplicationAPI applicationAPI, final PageAPI pageApi) {
        this.getUserRightsHelper = getUserRightsHelper;
        this.applicationAPI = applicationAPI;
        this.pageApi = pageApi;
    }

    public boolean isPageAuthorized(final String appId, final String pageName) throws ServletException,
    BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {

        if (StringUtil.isBlank(appId)) {
            return isPageAuthorizedInPortal(pageName);
        } else {
            return isPageAuthorizedInApplication(appId, pageName);
        }
    }

    private boolean isPageAuthorizedInPortal(final String pageName) throws ServletException {
        return getUserRightsHelper.getUserRights().contains(pageName);
    }

    private boolean isPageAuthorizedInApplication(final String applicationId, final String pageToken) throws BonitaHomeNotSetException,
    ServerAPIException, UnknownAPITypeException {
        try {
            return applicationAPI.searchApplicationPages(new SearchOptionsBuilder(0, 0)
                    .filter(ApplicationPageSearchDescriptor.APPLICATION_ID, applicationId)
                    .filter(ApplicationPageSearchDescriptor.PAGE_ID, pageApi.getPageByName(pageToken).getId())
                    .done()).getCount() > 0;
        } catch (final Exception e) {
            return false;
        }
    }

}
