package org.bonitasoft.web.rest.server.api.organization;

import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.identity.CustomUserInfoDefinitionCreator;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 */
public class APICustomUserInfoDefinition extends ConsoleAPI<CustomUserInfoDefinitionItem> implements
        APIHasAdd<CustomUserInfoDefinitionItem>,
        APIHasSearch<CustomUserInfoDefinitionItem>,
        APIHasDelete {

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
        return null;
    }

    public String defineDefaultSearchOrder() {
        return CustomUserInfoDefinitionItem.ATTRIBUTE_NAME;
    }
}
