package org.bonitasoft.console.client.admin.organization.users.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.mvp.ObservableModel;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInformationModels extends ObservableModel<CustomUserInformationModels> {

    private List<CustomUserInfoItem> items = new ArrayList<CustomUserInfoItem>();

    private String userId;

    private int page;

    private int size;

    public CustomUserInformationModels(String userId, int page, int size) {
        this.userId = userId;
        this.page = page;
        this.size = size;
        load(page, size, userId);
    }

    public List<CustomUserInfoItem> getInformation() {
        return items;
    }

    public void next() {
        load(++page, size, userId);
    }

    private void load(final int page, final int size, String userId) {
        APISearchRequest request = new APISearchRequest(CustomUserInfoDefinition.get());
        request.setPage(page);
        request.setResultsPerPage(size);
        request.addFilter(CustomUserInfoItem.ATTRIBUTE_USER_ID, userId);
        request.setCallback(new APICallback() {

            @Override
            public void onSuccess(int httpStatusCode, String response, Map<String, String> headers) {
                items.addAll(JSonItemReader.parseItems(response, CustomUserInfoDefinition.get()));
                notifyLoad(page, size, CustomUserInformationModels.this);
            }
        });
        request.run();
    }

    public void update(int index, String value) {
        if(!value.equals(items.get(index).getValue())) {
            items.get(index).setValue(value);
            notifyChange(this);
        }
    }
}
