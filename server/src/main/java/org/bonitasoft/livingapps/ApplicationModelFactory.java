package org.bonitasoft.livingapps;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.business.application.Application;
import org.bonitasoft.engine.business.application.ApplicationSearchDescriptor;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.livingapps.exception.CreationException;
import org.bonitasoft.livingapps.menu.MenuFactory;

public class ApplicationModelFactory {

    private final ApplicationAPI applicationApi;
    private final PageAPI customPageApi;
    private final ProfileAPI profileApi;

    public ApplicationModelFactory(final ApplicationAPI applicationApi, final PageAPI customPageApi, final ProfileAPI profileApi) {
        this.applicationApi = applicationApi;
        this.customPageApi = customPageApi;
        this.profileApi = profileApi;
    }

    public ApplicationModel createApplicationModel(final String name) throws CreationException {

        try {
            final SearchResult<Application> result = applicationApi.searchApplications(
                    new SearchOptionsBuilder(0, 1)
                    .filter(ApplicationSearchDescriptor.TOKEN, name)
                    .done());

            if (result.getCount() == 0) {
                throw new CreationException("No application found with name " + name);
            }

            return new ApplicationModel(
                    applicationApi,
                    customPageApi,
                    profileApi,
                    result.getResult().get(0),
                    new MenuFactory(applicationApi));
        } catch (final SearchException e) {
            throw new CreationException("Error while searching for the application " + name, e);
        }
    }
}
