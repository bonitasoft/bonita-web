package org.bonitasoft.web.rest.server.api.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.identity.CustomUserInfoDefinition;
import org.bonitasoft.engine.identity.CustomUserInfoDefinitionCreator;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClient;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.i18n._;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 */
public class APICustomUserInfoDefinition extends ConsoleAPI<CustomUserInfoDefinitionItem> implements
        APIHasAdd<CustomUserInfoDefinitionItem>,
        APIHasSearch<CustomUserInfoDefinitionItem>,
        APIHasDelete {

    public static final String FIX_ORDER = "Fix order";

    private final CustomUserInfoConverter converter = new CustomUserInfoConverter();

    private CustomUserInfoEngineClientCreator engineClientCreator;

    public APICustomUserInfoDefinition(CustomUserInfoEngineClientCreator engineClientCreator) {
        this.engineClientCreator = engineClientCreator;
    }

    public CustomUserInfoDefinitionItem add(CustomUserInfoDefinitionItem definition) {
        return converter.convert(engineClientCreator.create(getEngineSession())
                .createDefinition(new CustomUserInfoDefinitionCreator(
                        definition.getName(),
                        definition.getDescription())));
    }

    public void delete(final List<APIID> ids) {
        for (APIID id : ids) {
            engineClientCreator.create(getEngineSession()).deleteDefinition(id.toLong());
        }
    }

    public ItemSearchResult<CustomUserInfoDefinitionItem> search(
            final int page,
            final int resultsByPage,
            final String search,
            final String orders,
            final Map<String, String> filters) {

        assertNull(search, new _("Search term are not supported by this api"));
        assertNull(filters, new _("Filters are not supported by this api"));
        if(!orders.equals(FIX_ORDER)) {
            throw new APIException(new _("Sort is not supported by this api"));
        }

        CustomUserInfoEngineClient client = engineClientCreator.create(getEngineSession());
        List<CustomUserInfoDefinitionItem> result = new ArrayList<CustomUserInfoDefinitionItem>();
        for (CustomUserInfoDefinition definition : client.listDefinitions(page * resultsByPage, resultsByPage)) {
            result.add(converter.convert(definition));
        }

        return new ItemSearchResult<CustomUserInfoDefinitionItem>(page, resultsByPage, client.countDefinitions(), result);
    }

    private void assertNull(Object object, _ message) {
        if(object != null) {
            throw new APIException(message);
        }
    }

    public String defineDefaultSearchOrder() {
        return FIX_ORDER;
    }
}
