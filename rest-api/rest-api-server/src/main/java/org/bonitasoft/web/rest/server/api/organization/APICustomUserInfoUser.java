package org.bonitasoft.web.rest.server.api.organization;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.identity.CustomUserInfo;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClient;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator;
import org.bonitasoft.web.rest.server.framework.API;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.exception.APIAttributeMissingException;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.i18n._;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 */
public class APICustomUserInfoUser extends ConsoleAPI<CustomUserInfoItem> implements APIHasSearch<CustomUserInfoItem> {

    private CustomUserInfoEngineClientCreator engineClientCreator;

    private CustomUserInfoConverter converter = new CustomUserInfoConverter();

    public APICustomUserInfoUser(CustomUserInfoEngineClientCreator engineClientCreator) {
        this.engineClientCreator = engineClientCreator;
    }

    @Override
    public ItemSearchResult<CustomUserInfoItem> search(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        assertMandatory("userId", filters);

        CustomUserInfoEngineClient client = engineClientCreator.create(getEngineSession());
        List<CustomUserInfo> items = client.listCustomInformation(
                Long.parseLong(filters.get("userId")),
                page * resultsByPage,
                resultsByPage);

        List<CustomUserInfoItem> information = new ArrayList<CustomUserInfoItem>();
        for (CustomUserInfo item : items) {
            information.add(converter.convert(item));
        }
        return new ItemSearchResult<CustomUserInfoItem>(page, information.size(), client.countDefinitions(), information);
    }

    private void assertMandatory(String name, Map<String, String> filters) {
        if(!filters.containsKey(name)) {
            throw new APIException(new _("%name% filter is mandatory", new Arg("name", name)));
        }
    }

    @Override
    public String defineDefaultSearchOrder() {
        return "";
    }
}
