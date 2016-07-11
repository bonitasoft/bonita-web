package org.bonitasoft.console.common.server.page;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.business.application.Application;
import org.bonitasoft.engine.business.application.ApplicationPageSearchDescriptor;
import org.bonitasoft.engine.business.application.ApplicationSearchDescriptor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
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

    public boolean isPageAuthorized(final String appToken, final String pageName) throws BonitaException {

        if (StringUtil.isBlank(appToken)) {
            return isPageAuthorizedInPortal(pageName);
        } else {
            return isPageAuthorizedInApplication(appToken, pageName);
        }
    }

    private boolean isPageAuthorizedInPortal(final String pageName) throws BonitaException {
        return getUserRightsHelper.getUserRights().contains(pageName);
    }

    private boolean isPageAuthorizedInApplication(final String applicationToken, final String pageToken) throws BonitaException {
        try {
            Long applicationId = getApplicationId(applicationToken);

            if(applicationId == null){
                return false;
            }

            return applicationAPI.searchApplicationPages(new SearchOptionsBuilder(0, 0)
                    .filter(ApplicationPageSearchDescriptor.APPLICATION_ID, applicationId)
                    .filter(ApplicationPageSearchDescriptor.PAGE_ID, pageApi.getPageByName(pageToken).getId())
                    .done()).getCount() > 0;
        } catch (final Exception e) {
            return false;
        }
    }

    private Long getApplicationId(String applicationToken) throws BonitaException{
        SearchResult<Application> applicationSResult = applicationAPI.searchApplications(new SearchOptionsBuilder(0, 1)
                .filter(ApplicationSearchDescriptor.TOKEN, applicationToken).done());

        if(applicationSResult.getResult().size()<1){
            return null;
        }

        return applicationSResult.getResult().get(0).getId();
    }

}
