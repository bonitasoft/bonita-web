package org.bonitasoft.console.client.admin.organization.users.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoValueDefinition;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.APIUpdateRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.ApiSearchResultPager;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInformationModel {

    private String userId;

    private Map<APIID, IItem> changes = new HashMap<APIID, IItem>();

    public CustomUserInformationModel(String userId) {
        this.userId = userId;
    }

    public void update(CustomUserInfoItem item, String value) {
        CustomUserInfoItem change = new CustomUserInfoItem();
        change.setValue(value);
        changes.put(item.getId(), change);
    }

    public void flushChanges() {
        for (APIID id : changes.keySet()) {
            APIUpdateRequest request = new APIUpdateRequest(CustomUserInfoValueDefinition.get());
            request.setId(id);
            request.setItem(changes.get(id));
            request.run();
        }
        changes.clear();
    }

    public static abstract class Callback extends APICallback {

        @Override
        public void onSuccess(int httpStatusCode, String response, Map<String, String> headers) {
            ApiSearchResultPager pagination = ApiSearchResultPager.parse(headers.get("Content-Range"));
            onSuccess(
                    JSonItemReader.parseItems(response, CustomUserInfoDefinition.get()),
                    pagination.getCurrentPage(),
                    pagination.getNbResultsByPage(),
                    pagination.getNbTotalResults());
        }

        abstract void onSuccess(List<CustomUserInfoItem> information, int page, int pageSize, int total);
    }

    public void search(final int page, final int size, Callback callback) {
        APISearchRequest request = new APISearchRequest(CustomUserInfoDefinition.get());
        request.setPage(page);
        request.setResultsPerPage(size);
        request.addFilter(CustomUserInfoItem.ATTRIBUTE_USER_ID, userId);
        request.setCallback(callback);
        request.run();
    }
}
