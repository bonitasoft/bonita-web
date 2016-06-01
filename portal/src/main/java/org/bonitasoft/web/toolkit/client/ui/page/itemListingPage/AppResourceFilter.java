package org.bonitasoft.web.toolkit.client.ui.page.itemListingPage;

import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;

public class AppResourceFilter extends ItemListingResourceFilter {

    public AppResourceFilter(String... tablesToDisplay) {
        super(new APISearchRequest(ProcessDefinition.get()), ProcessItem.ATTRIBUTE_DISPLAY_NAME, tablesToDisplay);
    }

    public AppResourceFilter(APISearchRequest apiSearchRequest, String... tablesToDisplay) {
        super(apiSearchRequest, ProcessItem.ATTRIBUTE_DISPLAY_NAME, tablesToDisplay);
    }
}
